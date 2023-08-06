package com.example.uma_authz_smartphone

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.uma_authz_smartphone.di.appModule
import com.example.uma_authz_smartphone.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
//import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UmaAuthorizer : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@UmaAuthorizer)
            modules(listOf(appModule, viewModelModule))
//            workManagerFactory()
        }
    }
}