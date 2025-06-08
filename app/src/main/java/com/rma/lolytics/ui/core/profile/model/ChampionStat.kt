package com.rma.lolytics.ui.core.profile.model

import com.rma.lolytics.ui.core.home.model.Champion

data class ChampionStat(
    val champion: Champion,
    val matchesPlayed: Int
)
