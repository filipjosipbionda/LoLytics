package com.rma.lolytics.data.repository.ddragon

import com.rma.lolytics.ui.core.home.model.Champion
import io.ktor.client.HttpClient

interface DDragonRepository {
    val httpClient: HttpClient?
    suspend fun fetchChampions(): Result<List<Champion>>
}