package com.rma.lolytics.data.repository.ddragon

import com.rma.lolytics.data.mapper.toDomain
import com.rma.lolytics.data.model.ChampionListResponse
import com.rma.lolytics.ui.core.home.model.Champion
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DDragonRepositoryImpl(
    override val httpClient: HttpClient
): DDragonRepository {
    override suspend fun fetchChampions(): Result<List<Champion>> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val response = httpClient
                    .get("https://ddragon.leagueoflegends.com/cdn/14.10.1/data/en_US/champion.json")
                    .body<ChampionListResponse>()
                return@withContext response.data.values.toList().map { it.toDomain() }
            }
        }
    }
}