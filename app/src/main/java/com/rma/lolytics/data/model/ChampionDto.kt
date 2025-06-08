package com.rma.lolytics.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ChampionListResponse(
    val data: Map<String, ChampionDto>
)

@Serializable
data class ChampionDto(
    val id: String,
    val name: String,
    val image: ChampionImageDto
)

@Serializable
data class ChampionImageDto(
    val full: String
)

fun ChampionDto.getImageUrl(version: String = "14.10.1"): String {
    return "https://ddragon.leagueoflegends.com/cdn/$version/img/champion/${this.image.full}"
}
