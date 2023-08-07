package com.example.uma_authz_smartphone.di

import com.example.uma_authz_smartphone.data.repository.AuthzRepository
import com.example.uma_authz_smartphone.data.repository.RegisteredResourceRepository
import com.example.uma_authz_smartphone.data.repository.ManageRepository
import com.example.uma_authz_smartphone.data.repository.PatRepository
import com.example.uma_authz_smartphone.data.repository.PolicyRepository
import com.example.uma_authz_smartphone.data.repository.RptRepository
import com.example.uma_authz_smartphone.datasource.PolicyLocalDataSource
import com.example.uma_authz_smartphone.db.model.DbPolicy
import com.example.uma_authz_smartphone.db.model.DbRegisteredResource
import com.example.uma_authz_smartphone.db.model.DbResourceServer
import com.example.uma_authz_smartphone.workers.AuthorizeWorker
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val appModule = module{
    single {
        Realm.open(
            RealmConfiguration.Builder(setOf(
                DbPolicy::class,
                DbResourceServer::class,
                DbRegisteredResource::class,
            ))
                .deleteRealmIfMigrationNeeded()
                .build()
        )
    }
    single { PolicyLocalDataSource(get()) }

    single { AuthzRepository(get()) }
    single { ManageRepository() }
    single { PatRepository() }
    single { PolicyRepository(get()) }
    single { RptRepository() }
    single { RegisteredResourceRepository() }
    worker { AuthorizeWorker(get(), get(), get(), get()) }
}