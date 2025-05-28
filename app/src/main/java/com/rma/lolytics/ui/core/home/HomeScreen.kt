package com.rma.lolytics.ui.core.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rma.lolytics.R
import com.rma.lolytics.ui.core.home.model.Match
import com.rma.lolytics.ui.core.home.model.MatchListItem
import com.rma.lolytics.ui.shared.ChampionPickerBottomSheet
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun HomeScreen(
    addMatch: () -> Unit,
    logout: () -> Unit,
    onBackPress: () -> Unit,
    openProfileSettings: (String?) -> Unit,
    modifier: Modifier = Modifier,
    ) {
    val homeViewModel = koinViewModel<HomeViewModel>()
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val groupedItems by homeViewModel.matches.collectAsState()
    val champions by homeViewModel.champions.collectAsState()
    var showChampionSheet by rememberSaveable { mutableStateOf(false) }
    val selectedChampion by homeViewModel.selectedChampion.collectAsState()
    val profilePicture by homeViewModel.profilePictureUrl.collectAsState()

    BackHandler {
        onBackPress()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButtonMenu(
                expanded = fabMenuExpanded,
                button = {
                    FloatingActionButton(
                        onClick = {
                            fabMenuExpanded = !fabMenuExpanded
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    }
                }
            ) {
                FloatingActionButtonMenuItem(
                    onClick = addMatch,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    },
                    text = {}
                )

                FloatingActionButtonMenuItem(
                    onClick = {
                        showChampionSheet = true
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null
                        )
                    },
                    text = {}
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

}

@Composable
private fun HomeScreenContent(
    groupedItems: List<MatchListItem>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        state = rememberLazyListState()
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
                        MatchItem(match = item.match)
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

@Composable
private fun MatchItem(match: Match) {
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
