package com.rma.lolytics.ui.core.match.model

import com.rma.lolytics.ui.core.home.model.Champion
import com.rma.lolytics.ui.core.home.model.Role

data class AddMatchDataScreenState(
    val kills: String = "",
    val deaths: String = "",
    val assists: String = "",
    val cs: String = "",
    val gold: String = "",
    val duration: String = "",
    val isWin: Boolean = false,
    val selectedRole: Role = Role.MID,
    val selectedChampion: Champion? = null,
    val showChampionSheet: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)