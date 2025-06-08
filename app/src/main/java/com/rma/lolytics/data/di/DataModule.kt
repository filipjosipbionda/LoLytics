package com.rma.lolytics.data.di

import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.rma.lolytics.data.repository.auth.AuthRepository
import com.rma.lolytics.data.repository.auth.AuthRepositoryImpl
import com.rma.lolytics.data.repository.ddragon.DDragonRepository
import com.rma.lolytics.data.repository.ddragon.DDragonRepositoryImpl
import com.rma.lolytics.data.repository.firestore.FirestoreRepository
import com.rma.lolytics.data.repository.firestore.FirestoreRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule = module {
    single {
        HttpClient(CIO) {
            expectSuccess = true
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    single {
        FirebaseApp.initializeApp(get())
    }

    single {
        Firebase.auth
    }

    single {
        Firebase.firestore
    }

    single {
        Firebase.storage
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            firebaseAuth = get(),
        )
    }

    single<DDragonRepository> {
        DDragonRepositoryImpl(
            httpClient = get()
        )
    }

    single<FirestoreRepository> {
        FirestoreRepositoryImpl(
            firestore = get(),
            firebaseAuth = get(),
            firebaseStorage = get()
        )
    }
}