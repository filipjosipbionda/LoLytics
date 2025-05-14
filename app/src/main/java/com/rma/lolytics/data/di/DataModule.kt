package com.rma.lolytics.data.di

import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.rma.lolytics.data.repository.AuthRepository
import com.rma.lolytics.data.repository.AuthRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single {
        FirebaseApp.initializeApp(get())
    }

    single {
        Firebase.auth
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            firebaseAuth = get()
        )
    }
}