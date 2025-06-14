package com.rma.lolytics.data.repository.firestore

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.snapshots
import com.google.firebase.storage.FirebaseStorage
import com.rma.lolytics.data.mapper.toDomain
import com.rma.lolytics.data.mapper.toFirestoreModel
import com.rma.lolytics.data.model.MatchDto
import com.rma.lolytics.ui.core.home.model.Match
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage
): FirestoreRepository {
    override suspend fun sendMatch(match: Match) =
            withContext(Dispatchers.IO) {
               return@withContext try {
                    val uid = firebaseAuth.currentUser?.uid
                        ?: throw IllegalStateException("User not logged in")

                   firestore.collection("users")
                       .document(uid)
                       .collection("matches")
                       .document(match.id.toString())
                       .set(match.toFirestoreModel())
                       .await()

                    return@withContext Result.success(Unit)
                } catch (e: Exception) {
                    e.printStackTrace()
                   Result.failure(e)
                }
            }

    override suspend fun fetchMatchData(): Flow<List<Match>> = channelFlow {
        val collection = firestore.collection("users")
            .document(firebaseAuth.uid!!)
            .collection("matches")

        try {
            val cacheSnapshot = collection.get(Source.CACHE).await()
            val cachedMatches = cacheSnapshot.toObjects(MatchDto::class.java).map { it.toDomain() }
            send(cachedMatches)

            collection.snapshots()
                .map { snapshot -> snapshot.toObjects(MatchDto::class.java).map { it.toDomain() } }
                .collect { matches ->
                    send(matches)
                }
        } catch (e: Exception) {
            close(e)
        }
    }

    override suspend fun searchMatchesByChampion(champion: String): Flow<List<Match>> =
        firestore.collection("users")
            .document(firebaseAuth.uid!!)
            .collection("matches")
            .whereEqualTo("champion", champion)
            .snapshots()
            .flowOn(Dispatchers.IO)
            .map { snapshot ->
                snapshot.toObjects(MatchDto::class.java).map { it.toDomain() }
            }

    override suspend fun fetchProfilePicture(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val uid = firebaseAuth.uid
                ?: return@withContext Result.failure(IllegalStateException("User not logged in"))

            val imageRef = firebaseStorage.reference.child("profile_pictures/$uid.jpg")

            val downloadUrl = imageRef.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfilePicture(imageUri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            val uid = firebaseAuth.uid
                ?: return@withContext Result.failure(IllegalStateException("User not logged in"))

            val imageRef = firebaseStorage.reference.child("profile_pictures/$uid.jpg")

            imageRef
                .putFile(imageUri)
                .await()

            val downloadUrl = imageRef.downloadUrl
                .await()
                .toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMatch(id: Long): Result<Long> = withContext(Dispatchers.IO) {
        try {
           val uuid = firebaseAuth.uid ?:
           return@withContext Result.failure(IllegalStateException("User not logged in"))

           firestore.collection("users")
               .document(uuid)
               .collection("matches")
               .document(id.toString())
               .delete()

               return@withContext Result.success(id)

        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun updateUsername(username: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val uid = firebaseAuth.uid ?: return@withContext Result.failure(IllegalStateException("User not logged in"))

            val userData = mapOf("username" to username)

            firestore.collection("users")
                .document(uid)
                .set(userData, com.google.firebase.firestore.SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchUsername(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val uid = firebaseAuth.uid
                ?: return@withContext Result.failure(IllegalStateException("User not logged in"))

            val snapshot = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            val username = snapshot.getString("username")
                ?: return@withContext Result.failure(IllegalStateException("Username not found"))

            Result.success(username)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override  fun changePassword(password: String) {
        firebaseAuth.currentUser?.updatePassword(password)
    }
}

