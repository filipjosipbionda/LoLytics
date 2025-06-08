package com.rma.lolytics.ui.core.profile.model

data class UserStats(
    val wins: Int,
    val losses: Int,
    val winRate: Float,
    val totalKills: Int,
    val totalDeaths: Int,
    val totalAssists: Int,
    val avgKda: Float,
    val topChampions: List<ChampionStat>,
    val maxWinStreak: Int
)
