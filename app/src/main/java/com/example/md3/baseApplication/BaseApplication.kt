package com.example.md3.baseApplication

import android.app.Application
import apiModule
import com.codenicely.gimbook.saudi.einvoice.data.di.*
import com.example.md3.data.di.repositoryModule
import com.example.md3.data.di.viewModelModule
import com.example.md3.utils.AuthUtils
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication  : Application(){
    override fun onCreate() {
        super.onCreate()
        context = this
        startKoin{
            androidContext(this@BaseApplication)
            modules(apiModule, persistenceModule, repositoryModule, viewModelModule)
        }
        AuthUtils.init(this)

    }

    companion object{
        var context : BaseApplication? = null
    }

}