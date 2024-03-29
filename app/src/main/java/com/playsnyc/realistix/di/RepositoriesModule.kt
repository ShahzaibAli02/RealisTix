package com.playsnyc.realistix.di


import com.playsnyc.realistix.repositories.AuthRepository
import com.playsnyc.realistix.repositories.DataRepository
import com.playsnyc.realistix.repositories.FireStoreRepository
import com.playsnyc.realistix.repositories.SharedPref
import com.playsnyc.realistix.ui.screens.auth.login.LoginScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val reposModule = module {
    single { FireStoreRepository(get()) }
    single { AuthRepository(get()) }
    single { SharedPref(androidContext()) }
    single { DataRepository(get(),get()) }
}
