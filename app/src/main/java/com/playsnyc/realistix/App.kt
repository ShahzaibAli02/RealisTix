package com.playsnyc.realistix

import android.app.Application
import com.playsnyc.realistix.di.reposModule
import com.playsnyc.realistix.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application()
{

    override fun onCreate()
    {
        super.onCreate()
        setupKoin()
    }
    private fun setupKoin() {
        startKoin {
            androidContext(this@App)
//            modules(otherModules)
//            modules(networkModule)
//            modules(persistenceModule)
            modules(reposModule)
            modules(viewModelModule)
        }
    }
}

