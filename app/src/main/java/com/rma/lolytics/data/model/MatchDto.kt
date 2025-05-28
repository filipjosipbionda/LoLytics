package com.rma.lolytics.data.model

data class MatchDto(
    val champion: String = "",
    val role: String = "",
    val kills: Int = 0,
    val deaths: Int = 0,
    val assists: Int = 0,
    val cs: Int = 0,
    val goldEarned: Int = 0,
    val matchDurationMinutes: Int = 0,
    val win: Boolean = true,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUrl: String = "",
)
