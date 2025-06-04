package com.rma.lolytics.ui.core.home

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import coil3.compose.AsyncImage
import com.rma.lolytics.R
import com.rma.lolytics.ui.core.home.model.Match
import com.rma.lolytics.ui.core.home.model.MatchListItem
import com.rma.lolytics.ui.shared.ChampionPickerBottomSheet
import com.rma.lolytics.ui.shared.LolyticsAlertDialog
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    addMatch: () -> Unit,
    logout: () -> Unit,
    onBackPress: () -> Unit,
    openProfileSettings: (String?) -> Unit,
    modifier: Modifier = Modifier,
    ) {
    val homeViewModel = koinViewModel<HomeViewModel>()
    val groupedItems by homeViewModel.matches.collectAsState()
    val champions by homeViewModel.champions.collectAsState()
    var showChampionSheet by rememberSaveable { mutableStateOf(false) }
    val selectedChampion by homeViewModel.selectedChampion.collectAsState()
    val profilePicture by homeViewModel.profilePictureUrl.collectAsState()
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
    var matchToDeleteId: Long? by remember {
        mutableStateOf(null)
    }
    val isDeleted by homeViewModel.isDeleted.collectAsState()
    val context = LocalContext.current

    BackHandler {
        onBackPress()
    }

    RequestNotificationPermission()
    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            showNotification(
                context = context,
                title = "LoLytics",
                message = "Your match history has been updated - match deleted.",
            )
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = addMatch
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.home_screen_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                actions = {
                    IconButton(onClick = { showChampionSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                        )
                    }

                    AsyncImage(
                        fallback = painterResource(R.drawable.placeholder_lolytics),
                        placeholder = painterResource(R.drawable.placeholder_lolytics),
                        model = profilePicture,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable {
                                openProfileSettings(profilePicture)
                            },
                        contentScale = ContentScale.Crop
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            logout()
                            homeViewModel.logout()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null
                        )
                    }
                },

            )
        },
    ) { paddingValues ->
        when {
            groupedItems.isEmpty() && selectedChampion == null -> {
                Box(
                    modifier = modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        stringResource(R.string.home_screen_no_matches_button),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
            else -> {
                HomeScreenContent(
                    groupedItems = groupedItems,
                    modifier = modifier.padding(paddingValues),
                    onConfirmDelete = {
                        showDeleteDialog = true
                        matchToDeleteId = it
                    }
                )
            }
        }
    }

    if (showChampionSheet) {
        ChampionPickerBottomSheet(
            champions = champions,
            onSelect = {
                homeViewModel.setSearchQuery(it)
                showChampionSheet = false
            },
            onDismiss = {
                showChampionSheet = false
            },
            selectedChampion = selectedChampion,
        )
    }

    if (showDeleteDialog) {
        LolyticsAlertDialog(
            title = stringResource(R.string.delete_match_dialog),
            text = stringResource(R.string.delete_match_dialog_text),
            confirmButtonText = stringResource(R.string.delete_match_confirm_button_text),
            cancelButtonText = stringResource(R.string.delete_match_cancel_button_text),
            onConfirmRequest = {
                matchToDeleteId?.let {
                    homeViewModel.deleteMatch(it)
                }
                showDeleteDialog = false
            },
            onDismissRequest = {
                showDeleteDialog = false
            }
        )
    }
}

@Composable
private fun HomeScreenContent(
    groupedItems: List<MatchListItem>,
    modifier: Modifier = Modifier,
    onConfirmDelete: (Long) -> Unit,
) {
    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = lazyColumnState
    ) {
        groupedItems.forEach { item ->
            when (item) {
                is MatchListItem.DateHeader -> {
                    stickyHeader {
                        DateHeader(item.date)
                    }
                }
                is MatchListItem.MatchEntry -> {
                    item {
                        MatchItem(
                            match = item.match,
                            delete = {
                                onConfirmDelete(it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenContentPreview() {
    HomeScreen (
        logout = {},
        onBackPress = {},
        modifier = Modifier.fillMaxSize(),
        addMatch = {},
        openProfileSettings = {},
    )
}

@SuppressLint("SuspiciousIndentation")
@Composable
private fun MatchItem(
    match: Match,
    delete: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }


    val density = LocalDensity.current

        Card (
            modifier = modifier
                .onSizeChanged {
                    itemHeight = with(density) {
                        it.height.toDp()
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(true) {
                        detectTapGestures(
                            onLongPress = {
                                isContextMenuVisible = true
                                pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                            }
                        )
                    }
            ) {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    leadingContent = {
                        AsyncImage(
                            model = match.imageUrl,
                            contentDescription = match.champion,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                    },
                    headlineContent = {
                        Text(
                            text = match.champion,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    supportingContent = {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = match.role.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "K/D/A: ${match.kills}/${match.deaths}/${match.assists}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "CS: ${match.cs}   •   Gold: ${match.goldEarned}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    trailingContent = {
                        Text(
                            text = if (match.isWin) "Victory" else "Defeat",
                            color = if (match.isWin) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                )
            }

            DropdownMenu(
                expanded = isContextMenuVisible,
                onDismissRequest = {
                    isContextMenuVisible = false
                },
                offset = pressOffset.copy(
                    y = pressOffset.y - itemHeight
                )
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Delete"
                        )
                    },
                    onClick = {
                        delete(match.id)
                        isContextMenuVisible = false
                    }
                )
            }
        }
}



@Composable
internal fun DateHeader(
    date: String,
    modifier: Modifier = Modifier,
) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(start = 16.dp),
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
}

@SuppressLint("ServiceCast")
fun showNotification(
    context: Context,
    title: String,
    message: String,
    ) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val notification = NotificationCompat.Builder(context, "default_channel_id")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    notificationManager.notify(1, notification)
}

@Composable
fun RequestNotificationPermission() {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { _ ->

        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }
}
