package com.example.uma_authz_smartphone.di

import com.example.uma_authz_smartphone.data.repository.AuthzRepository
import com.example.uma_authz_smartphone.data.repository.RegisteredResourceRepository
import com.example.uma_authz_smartphone.data.repository.ManageRepository
import com.example.uma_authz_smartphone.data.repository.PatRepository
import com.example.uma_authz_smartphone.data.repository.PolicyRepository
import com.example.uma_authz_smartphone.data.repository.RptRepository
import com.example.uma_authz_smartphone.datasource.AuthorizationRequestLocalDataSource
import com.example.uma_authz_smartphone.datasource.PolicyLocalDataSource
import com.example.uma_authz_smartphone.datasource.RegisteredResourceLocalDataSource
import com.example.uma_authz_smartphone.db.model.DbAuthorizationRequest
import com.example.uma_authz_smartphone.db.model.DbClientInfo
import com.example.uma_authz_smartphone.db.model.DbPolicy
import com.example.uma_authz_smartphone.db.model.DbRegisteredResource
import com.example.uma_authz_smartphone.db.model.DbRegisteredScope
import com.example.uma_authz_smartphone.db.model.DbRequestedResource
import com.example.uma_authz_smartphone.db.model.DbRequestedScope
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
                DbRequestedScope::class,
                DbRequestedResource::class,
                DbAuthorizationRequest::class,
                DbClientInfo::class,
                DbPolicy::class,
                DbRegisteredScope::class,
            ))
//                .deleteRealmIfMigrationNeeded()
                .inMemory()
                .build()
        )
    }
    single { PolicyLocalDataSource(get(), get()) }
    single { AuthorizationRequestLocalDataSource(get()) }
    single { RegisteredResourceLocalDataSource(get())}

    single { AuthzRepository(get(), get()) }
    single { ManageRepository() }
    single { PatRepository() }
    single { PolicyRepository(get()) }
    single { RptRepository() }
    single { RegisteredResourceRepository(get()) }
    worker { AuthorizeWorker(get(), get(), get(), get()) }
}