package com.rma.lolytics.data.repository

interface AuthRepository {
    suspend fun login(
            email: String,
            password: String,
        ): Result<Unit>

    fun checkIfUserIsLogged(): Boolean
}