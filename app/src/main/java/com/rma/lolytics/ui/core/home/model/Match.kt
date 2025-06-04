package com.rma.lolytics.ui.core.home.model

import kotlin.random.Random

data class Match(
    val id: Long = Random.nextLong(),
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

