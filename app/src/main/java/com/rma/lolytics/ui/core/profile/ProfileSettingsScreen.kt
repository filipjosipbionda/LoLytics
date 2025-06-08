package com.rma.lolytics.ui.core.profile

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rma.lolytics.R
import com.rma.lolytics.ui.core.profile.model.UserStats
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileSettingScreen(
    profilePictureUrl: String?,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: ProfileSettingsViewModel = koinViewModel<ProfileSettingsViewModel>()
    var imageUrl by remember {
        mutableStateOf(profilePictureUrl)
    }
    var isEditModeOn by rememberSaveable {
        mutableStateOf(false)
    }
    val username by viewModel.username.collectAsState()
    val stats by viewModel.userStats.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.profile)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    Switch(
                        checked = isEditModeOn,
                        onCheckedChange = {
                            isEditModeOn = !isEditModeOn
                        },
                        thumbContent = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    )
                },
            )
        }) { paddingValues ->
        ProfileSettingsScreenContent(
            updateProfilePicture = {
                imageUrl = it.toString()
                viewModel.updateProfilePicture(it)
            },
            imageUrl = profilePictureUrl,
            isEditModeOn = isEditModeOn,
            changeUsername = {username ->
                isEditModeOn = !isEditModeOn
                viewModel.updateUsername(username)
            },
            changePassword = {
                isEditModeOn = false
                viewModel.changePassword(it)
            },
            username = username,
            stats = stats,
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun ProfileSettingsScreenContent(
    username: String,
    imageUrl: String?,
    updateProfilePicture: (Uri) -> Unit,
    isEditModeOn: Boolean,
    changeUsername: (String) -> Unit,
    changePassword: (String) -> Unit,
    stats: UserStats?,
    modifier: Modifier = Modifier,
) {
    val toastText = stringResource(R.string.profile_picture_settings_uploading_image)
    val context = LocalContext.current
    var usernameText by rememberSaveable { mutableStateOf(username) }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            updateProfilePicture(it)
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            AsyncImage(
                model = imageUrl,
                fallback = painterResource(R.drawable.placeholder_lolytics),
                placeholder = painterResource(R.drawable.placeholder_lolytics),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape),
                contentScale = ContentScale.Crop
            )
            this@Column.AnimatedVisibility(
                visible = isEditModeOn,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-8).dp, y = (-8).dp)
                    .background(Color.White, CircleShape)
            ) {
                IconButton(
                    onClick = { launcher.launch("image/*") }
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }

        Crossfade(
            targetState = isEditModeOn,
            label = "EditModeCrossfade"
        ) { editMode ->
            if (editMode) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = stringResource(R.string.profile_details_change_username),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    OutlinedTextField(
                        value = usernameText,
                        onValueChange = { usernameText = it },
                        readOnly = !isEditModeOn,
                        placeholder = {
                            Text(text = username.ifBlank { stringResource(R.string.profile_screen_username) })
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        trailingIcon = {
                            if (usernameText.isNotBlank() && isEditModeOn) {
                                IconButton(onClick = { changeUsername(usernameText) }) {
                                    Icon(imageVector = Icons.Default.Done, contentDescription = null)
                                }
                            }
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = stringResource(R.string.profile_change_password_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.profile_new_password)) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = {
                            Text(
                                text = stringResource(R.string.register_confirm_password)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        trailingIcon = {
                            if (password.isNotBlank() && password == confirmPassword) {
                                IconButton(onClick = {
                                    changePassword(password)
                                    password = ""
                                    confirmPassword = ""
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    )
                }
            } else {
                stats?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = username,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            stringResource(R.string.profile_screen_your_stats),
                            style = MaterialTheme.typography.titleMedium)

                        Spacer(modifier = Modifier.height(16.dp))

                        StatIconRow(Icons.Default.ThumbUp,
                            stringResource(R.string.profile_screen_wins), it.wins)
                        StatIconRow(Icons.Default.ThumbDown,
                            stringResource(R.string.profile_screen_losses), it.losses)
                        StatIconRow(Icons.Default.SportsKabaddi,
                            stringResource(R.string.profile_screen_kills), it.totalKills)
                        StatIconRow(Icons.Default.People,
                            stringResource(R.string.profile_screen_assists), it.totalAssists)
                        StatIconRow(Icons.Default.HeartBroken,
                            stringResource(R.string.profile_screen_deaths), it.totalDeaths)

                        HorizontalDivider(Modifier.padding(vertical = 16.dp))

                        StatIconRow(
                            Icons.Default.Percent,
                            stringResource(R.string.profile_screen_winrate),
                            "${it.winRate.toInt()}%"
                        )
                        StatIconRow(
                            Icons.AutoMirrored.Filled.TrendingUp,
                            stringResource(R.string.profile_screen_highest_winstreak),
                            it.maxWinStreak,
                        )
                        StatIconRow(
                            Icons.AutoMirrored.Filled.ShowChart,
                            stringResource(R.string.profile_screen_average_kda),
                            String.format("%.2f", it.avgKda)
                        )

                        HorizontalDivider(Modifier.padding(vertical = 16.dp))

                        Text(
                            stringResource(R.string.top_5_most_played_champs),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        it.topChampions.forEachIndexed { index, champStat ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = champStat.champion.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    "${index + 1}. ${champStat.champion.name}",
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    "${champStat.matchesPlayed}x",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

    @Composable
   private fun StatIconRow(icon: ImageVector, label: String, value: Any) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(label, modifier = Modifier.weight(1f))
            Text(value.toString(), style = MaterialTheme.typography.bodyMedium)
        }
    }
