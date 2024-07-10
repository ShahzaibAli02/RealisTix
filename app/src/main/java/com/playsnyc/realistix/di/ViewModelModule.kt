package com.playsnyc.realistix.di


import com.playsnyc.realistix.ui.screens.attandes.AttandeListScreenViewModel
import com.playsnyc.realistix.ui.screens.auth.login.LoginScreenViewModel
import com.playsnyc.realistix.ui.screens.auth.signup.SignUpScreenViewModel
import com.playsnyc.realistix.ui.screens.contact.ContactScreenViewModel
import com.playsnyc.realistix.ui.screens.createevent.CreateEventScreenViewModel
import com.playsnyc.realistix.ui.screens.home.HomeViewModel
import com.playsnyc.realistix.ui.screens.myactivities.MyActivitiesScreenViewModel
import com.playsnyc.realistix.ui.screens.myconnections.MyConnectionScreenViewModel
import com.playsnyc.realistix.ui.screens.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { LoginScreenViewModel(get () ) }
  viewModel { SignUpScreenViewModel(get(),get()) }
  viewModel { ProfileViewModel(get()) }
  viewModel { CreateEventScreenViewModel(get()) }
  viewModel { HomeViewModel(get()) }
  viewModel { ContactScreenViewModel(get()) }
  viewModel { MyConnectionScreenViewModel(get()) }
  viewModel { MyActivitiesScreenViewModel(get()) }
  viewModel { AttandeListScreenViewModel(get()) }
}
