package com.rma.lolytics.data.repository.firestore

import android.net.Uri
import com.rma.lolytics.ui.core.home.model.Match
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    suspend fun sendMatch(match: Match): Result<Unit>

    suspend fun fetchMatchData(): Flow<List<Match>>

    suspend fun searchMatchesByChampion(champion: String): Flow<List<Match>>

    suspend fun fetchProfilePicture(): Result<String>

    suspend fun updateProfilePicture(imageUri: Uri): Result<String>

    suspend fun deleteMatch(id: Long): Result<Long>
}