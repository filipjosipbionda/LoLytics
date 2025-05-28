package com.rma.lolytics.ui.core.home.model

sealed class MatchListItem {
    data class DateHeader(val date: String) : MatchListItem()
    data class MatchEntry(val match: Match) : MatchListItem()
}