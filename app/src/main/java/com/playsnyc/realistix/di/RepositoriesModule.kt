package com.playsnyc.realistix.di


import com.playsnyc.realistix.repositories.AuthRepository
import com.playsnyc.realistix.repositories.FireStoreRepository
import com.playsnyc.realistix.ui.screens.auth.login.LoginScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val reposModule = module {
  single { FireStoreRepository() }
  single { AuthRepository() }
}
