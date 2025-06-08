package com.rma.lolytics.ui.core.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.lolytics.data.repository.firestore.FirestoreRepository
import com.rma.lolytics.ui.core.home.model.Champion
import com.rma.lolytics.ui.core.home.model.Match
import com.rma.lolytics.ui.core.profile.model.ChampionStat
import com.rma.lolytics.ui.core.profile.model.UserStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileSettingsViewModel(
    private val firestoreRepository: FirestoreRepository,
): ViewModel() {

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _userStats = MutableStateFlow<UserStats?>(null)
    val userStats = _userStats.asStateFlow()

    init {
        fetchMatches()
        fetchUsername()
    }

    internal fun updateProfilePicture(uri: Uri) {
        viewModelScope.launch {
            firestoreRepository.updateProfilePicture(
                imageUri = uri
            )
        }
    }

    internal fun updateUsername(username: String) {
        viewModelScope.launch {
            firestoreRepository.updateUsername(username = username ).onSuccess {
                fetchUsername()
            }
        }
    }

    private fun fetchUsername() {
        viewModelScope.launch {
            firestoreRepository.fetchUsername().onSuccess {
                _username.value = it
            }
        }
    }

    private fun fetchMatches() {
        viewModelScope.launch {
            firestoreRepository.fetchMatchData()
                .collect { matchList ->
                    calculateStats(matchList)
                }
        }
    }

    fun changePassword(password: String) {
        firestoreRepository.changePassword(password)
    }

    private fun calculateStats(matches: List<Match>) {
        val (wins, losses) = calculateWinStats(matches)
        val (kills, deaths, assists) = calculateKdaStats(matches)
        val avgKda = calculateAvgKda(kills, deaths, assists)
        val winrate = calculateWinRate(wins, matches.size)
        val topChampions = calculateTopChampions(matches)
        val maxWinstreak = calculateMaxWinStreak(matches)

        _userStats.value = UserStats(
            wins = wins,
            losses = losses,
            winRate = winrate,
            totalKills = kills,
            totalDeaths = deaths,
            totalAssists = assists,
            avgKda = avgKda,
            topChampions = topChampions,
            maxWinStreak = maxWinstreak
        )
    }

    private fun calculateWinStats(matches: List<Match>): Pair<Int, Int> {
        val wins = matches.count { it.isWin }
        val losses = matches.size - wins
        return wins to losses
    }

    private fun calculateKdaStats(matches: List<Match>): Triple<Int, Int, Int> {
        val totalKills = matches.sumOf { it.kills }
        val totalDeaths = matches.sumOf { it.deaths }
        val totalAssists = matches.sumOf { it.assists }
        return Triple(totalKills, totalDeaths, totalAssists)
    }

    private fun calculateAvgKda(kills: Int, deaths: Int, assists: Int): Float {
        return if (deaths == 0) (kills + assists).toFloat() else (kills + assists).toFloat() / deaths
    }

    private fun calculateWinRate(wins: Int, total: Int): Float {
        return if (total == 0) 0f else wins.toFloat() / total * 100
    }

    private fun calculateTopChampions(matches: List<Match>): List<ChampionStat> {
        return matches
            .groupingBy { it.champion to it.imageUrl }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(5)
            .map { (championData, count) ->
                val (championName, imageUrl) = championData
                val champion = Champion(
                    id = championName,
                    name = championName,
                    imageUrl = imageUrl
                )
                ChampionStat(champion = champion, matchesPlayed = count)
            }
    }

    private fun calculateMaxWinStreak(matches: List<Match>): Int {
        return matches
            .sortedBy { it.date }
            .fold(0 to 0) { (max, current), match ->
                if (match.isWin) {
                    val newCurrent = current + 1
                    max.coerceAtLeast(newCurrent) to newCurrent
                } else {
                    max to 0
                }
            }.first
    }

}