package com.rma.lolytics.data.mapper

import com.rma.lolytics.data.model.MatchDto
import com.rma.lolytics.ui.core.home.model.Match
import com.rma.lolytics.ui.core.home.model.Role

fun Match.toFirestoreModel(): MatchDto {
    return MatchDto(
        id = this.id,
        champion = this.champion,
        role = this.role.name,
        kills = this.kills,
        deaths = this.deaths,
        assists = this.assists,
        cs = this.cs,
        goldEarned = this.goldEarned,
        matchDurationMinutes = this.matchDurationMinutes,
        win = this.isWin,
        timestamp = this.date,
        imageUrl = this.imageUrl
    )
}

fun MatchDto.toDomain(): Match {
    return Match(
        date = this.timestamp,
        id = this.id,
        champion = this.champion,
        role = Role.valueOf(this.role),
        kills = this.kills,
        deaths = this.deaths,
        assists = this.assists,
        cs = this.cs,
        goldEarned = this.goldEarned,
        matchDurationMinutes = this.matchDurationMinutes,
        isWin = this.win,
        imageUrl = this.imageUrl
    )
}