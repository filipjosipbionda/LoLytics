package com.rma.lolytics.ui.core.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.lolytics.data.repository.ddragon.DDragonRepository
import com.rma.lolytics.data.repository.firestore.FirestoreRepository
import com.rma.lolytics.ui.core.home.model.Champion
import com.rma.lolytics.ui.core.home.model.Match
import com.rma.lolytics.ui.core.home.model.Role
import com.rma.lolytics.ui.core.match.model.AddMatchDataScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddMatchViewModel(
    private val dragonRepository: DDragonRepository,
    private val firestoreRepository: FirestoreRepository,
): ViewModel() {
    private val _champions = MutableStateFlow<List<Champion>>(emptyList())
    val champions = _champions.asStateFlow()

    private val _uiState = MutableStateFlow(AddMatchDataScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchChampions()
    }

    private fun fetchChampions() {
        viewModelScope.launch {
            dragonRepository
                .fetchChampions()
                .onSuccess {
                    _champions.value = it
                }.onFailure {

                }
        }
    }

    fun updateKills(value: String) {
        _uiState.value = _uiState.value.copy(kills = value)
    }

    fun updateDeaths(value: String) {
        _uiState.value = _uiState.value.copy(deaths = value)
    }

    fun updateAssists(value: String) {
        _uiState.value = _uiState.value.copy(assists = value)
    }

    fun updateCs(value: String) {
        _uiState.value = _uiState.value.copy(cs = value)
    }

    fun updateGold(value: String) {
        _uiState.value = _uiState.value.copy(gold = value)
    }

    fun updateDuration(value: String) {
        _uiState.value = _uiState.value.copy(duration = value)
    }

    fun updateIsWin(value: Boolean) {
        _uiState.value = _uiState.value.copy(isWin = value)
    }

    fun updateRole(role: Role) {
        _uiState.value = _uiState.value.copy(selectedRole = role)
    }

    fun selectChampion(champion: Champion?) {
        _uiState.value = _uiState.value.copy(
            selectedChampion = champion,
            showChampionSheet = false
        )
    }

    fun showChampionPicker(show: Boolean) {
        _uiState.value = _uiState.value.copy(showChampionSheet = show)
    }

    fun updateTimestamp(timestamp: Long) {
        _uiState.value = _uiState.value.copy(timestamp = timestamp)
    }


    fun sendMatchData(match: Match) {
        viewModelScope.launch {

            firestoreRepository.sendMatch(match)
                .onSuccess {}
                .onFailure {}
        }
    }
}
