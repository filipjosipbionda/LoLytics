package com.rma.lolytics.ui.core.home.model

data class Match(
    val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val champion: String,
    val imageUrl: String,
    val role: Role,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val isWin: Boolean,
    val cs: Int,
    val goldEarned: Int,
    val matchDurationMinutes: Int,
)

