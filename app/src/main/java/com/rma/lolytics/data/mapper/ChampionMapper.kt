package com.rma.lolytics.data.mapper

import com.rma.lolytics.data.model.ChampionDto
import com.rma.lolytics.data.model.getImageUrl
import com.rma.lolytics.ui.core.home.model.Champion

fun ChampionDto.toDomain(): Champion {
    return Champion(
        id = id,
        name = name,
        imageUrl = this.getImageUrl()
    )
}