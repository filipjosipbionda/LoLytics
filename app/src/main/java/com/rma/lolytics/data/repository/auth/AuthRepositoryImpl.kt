package com.rma.lolytics.data.repository.auth

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
): AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                Tasks.await(
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override fun checkIfUserIsLogged(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                Tasks.await(
                    firebaseAuth.sendPasswordResetEmail(email)
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                Tasks.await(
                    firebaseAuth.createUserWithEmailAndPassword(
                        email,
                        password
                    )
                )
                Result.success(Unit)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
