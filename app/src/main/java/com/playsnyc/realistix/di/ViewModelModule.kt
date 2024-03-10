package com.playsnyc.realistix.di


import com.playsnyc.realistix.ui.screens.auth.login.LoginScreenViewModel
import com.playsnyc.realistix.ui.screens.auth.signup.SignUpScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { LoginScreenViewModel(get () ) }
  viewModel { SignUpScreenViewModel(get(),get()) }
}
