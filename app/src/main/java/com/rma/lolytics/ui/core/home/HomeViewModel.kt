package com.rma.lolytics.ui.core.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.lolytics.data.repository.auth.AuthRepository
import com.rma.lolytics.data.repository.ddragon.DDragonRepository
import com.rma.lolytics.data.repository.firestore.FirestoreRepository
import com.rma.lolytics.ui.core.home.model.Champion
import com.rma.lolytics.ui.core.home.model.Match
import com.rma.lolytics.ui.core.home.model.MatchListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal class HomeViewModel(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository,
    private val dDragonRepository: DDragonRepository,
): ViewModel() {
    private val _matches = MutableStateFlow<List<MatchListItem>>(emptyList())
    val matches = _matches.asStateFlow()

    private val _champions = MutableStateFlow<List<Champion>>(emptyList())
    val champions = _champions.asStateFlow()

    private val _selectedChampion = MutableStateFlow<Champion?>(null)
    val selectedChampion = _selectedChampion.asStateFlow()

    private val _profilePictureUrl = MutableStateFlow<String?>(null)
    val profilePictureUrl = _profilePictureUrl.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())


    init {
        fetchChampions()
        fetchMatchData()
        fetchProfilePicture()
    }

    private fun fetchProfilePicture() {
        viewModelScope.launch {
            firestoreRepository.fetchProfilePicture()
                .onSuccess {
                _profilePictureUrl.value = it
            }
        }
    }

    internal fun logout() {
        viewModelScope.launch {
            authRepository
                .logout()
                .onSuccess {

                }.onFailure {

                }
        }
    }

    private fun fetchChampions() {
        viewModelScope.launch {
            dDragonRepository.fetchChampions()
                .onSuccess {
                _champions.value = it
            }.onFailure {
                //TODO: implement error handling logic
            }
        }
    }

    private fun fetchMatchData() {
        viewModelScope.launch {
            firestoreRepository.fetchMatchData()
                .catch {
                }
                .collect { matches ->
                    _matches.value = groupMatchesByDate(matches)
                }
        }
    }

    private fun groupMatchesByDate(matches: List<Match>): List<MatchListItem> {

        return matches
            .sortedByDescending { it.date }
            .groupBy { dateFormatter.format(Date(it.date)) }
            .flatMap { (date, matchesForDate) ->
                listOf(MatchListItem.DateHeader(date)) +
                        matchesForDate.map { MatchListItem.MatchEntry(it) }
            }
    }

    fun setSearchQuery(champion: Champion?) {
        viewModelScope.launch {
            if (champion == null) {
                _selectedChampion.value = null
                firestoreRepository.fetchMatchData()
                    .collect { matches ->
                        _matches.value = groupMatchesByDate(matches)
                    }
            } else {
                _selectedChampion.value = champion
                firestoreRepository.searchMatchesByChampion(champion.name)
                    .collect { filteredMatches ->
                        _matches.value = groupMatchesByDate(filteredMatches)
                    }
            }
        }
    }
}