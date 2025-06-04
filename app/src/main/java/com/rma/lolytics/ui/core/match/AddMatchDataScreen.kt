package com.rma.lolytics.ui.core.match

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rma.lolytics.R
import com.rma.lolytics.ui.core.home.model.Champion
import com.rma.lolytics.ui.core.home.model.Match
import com.rma.lolytics.ui.core.home.model.Role
import com.rma.lolytics.ui.core.match.model.AddMatchDataScreenState
import com.rma.lolytics.ui.shared.ChampionPickerBottomSheet
import org.koin.androidx.compose.koinViewModel
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddMatchScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val addMatchViewModel = koinViewModel<AddMatchViewModel>()
    val champions by addMatchViewModel.champions.collectAsState()
    val uiState by addMatchViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.add_match_data_top_bar_title),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        AddMatchDataScreenContent(
            uiState = uiState,
            champions = champions,
            modifier = modifier.padding(padding),
            onMatchSaved = { match ->
                addMatchViewModel.sendMatchData(match)
                navigateBack()
            },
            viewModel = addMatchViewModel
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMatchDataScreenContent(
    uiState: AddMatchDataScreenState,
    viewModel: AddMatchViewModel,
    onMatchSaved: (Match) -> Unit,
    champions: List<Champion> = emptyList(),
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .imePadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = uiState.kills,
            onValueChange = viewModel::updateKills,
            label = { Text(stringResource(R.string.add_match_data_kills_placeholder)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = uiState.deaths,
            onValueChange = viewModel::updateDeaths,
            label = { Text(stringResource(R.string.add_match_data_deaths_placeholder)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = uiState.assists,
            onValueChange = viewModel::updateAssists,
            label = { Text(stringResource(R.string.add_match_data_assists_placeholder)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = uiState.cs,
            onValueChange = viewModel::updateCs,
            label = { Text(stringResource(R.string.add_match_data_cs_placeholder)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = uiState.gold,
            onValueChange = viewModel::updateGold,
            label = { Text(stringResource(R.string.add_match_data_gold_placeholder)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = uiState.duration,
            onValueChange = viewModel::updateDuration,
            label = { Text(stringResource(R.string.add_match_data_duration_placeholder)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        MatchDatePickerField(
            timestamp = uiState.timestamp,
            onDateSelected = viewModel::updateTimestamp
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = uiState.isWin,
                onCheckedChange = viewModel::updateIsWin
            )
            Text(stringResource(R.string.add_match_data_victory_text))
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = uiState.selectedRole.name,
                onValueChange = {},
                label = { Text(stringResource(R.string.add_match_data_role_placeholder)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    )
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Role.entries.forEach { role ->
                    DropdownMenuItem(
                        text = { Text(role.name) },
                        onClick = {
                            viewModel.updateRole(role)
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedButton(
            onClick = { viewModel.showChampionPicker(true) },
            modifier = Modifier.fillMaxWidth()
        ) {
            val champ = uiState.selectedChampion
            if (champ != null) {
                AsyncImage(
                    model = champ.imageUrl,
                    contentDescription = champ.name,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(champ.name)
            } else {
                Text(stringResource(R.string.add_match_data_select_champion_text))
            }
        }

        Button(
            onClick = {
                uiState.selectedChampion?.let {
                    val match = Match(
                        champion = it.name,
                        role = uiState.selectedRole,
                        kills = uiState.kills.toIntOrNull() ?: 0,
                        deaths = uiState.deaths.toIntOrNull() ?: 0,
                        assists = uiState.assists.toIntOrNull() ?: 0,
                        cs = uiState.cs.toIntOrNull() ?: 0,
                        goldEarned = uiState.gold.toIntOrNull() ?: 0,
                        matchDurationMinutes = uiState.duration.toIntOrNull() ?: 0,
                        isWin = uiState.isWin,
                        date = uiState.timestamp,
                        imageUrl = it.imageUrl
                    )
                    onMatchSaved(match)
                }
            },
            enabled = uiState.selectedChampion != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.add_match_data_save_match_text))
        }
    }

    if (uiState.showChampionSheet) {
        ChampionPickerBottomSheet(
            champions = champions,
            onSelect = viewModel::selectChampion,
            onDismiss = { viewModel.showChampionPicker(false) },
            selectedChampion = uiState.selectedChampion,
        )
    }
}

@Composable
fun MatchDatePickerField(
    timestamp: Long?,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showModal by remember { mutableStateOf(false) }

    val formattedDate = timestamp?.let { millis ->
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        formatter.format(Date(millis))
    } ?: ""

    OutlinedTextField(
        value = formattedDate,
        onValueChange = {},
        readOnly = true,
        label = { Text("Match Date") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = null)
        },
        modifier = modifier
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (up != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            initialDate = timestamp,
            onDateSelected = {
                onDateSelected(it)
                showModal = false
            },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    initialDate: Long? = null,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDate)

   DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                datePickerState.selectedDateMillis?.let(onDateSelected)
            }) {
                Text(stringResource(R.string.add_match_data_date_picker_confirm_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.add_match_data_date_picker_cancel_button))
            }
        }
    ) {
        DatePicker(state = datePickerState, showModeToggle = false)
    }
}



