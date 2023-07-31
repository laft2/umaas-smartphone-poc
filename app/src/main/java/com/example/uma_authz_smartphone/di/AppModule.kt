package com.example.uma_authz_smartphone.di

import com.example.uma_authz_smartphone.data.repository.AuthzRepository
import com.example.uma_authz_smartphone.data.repository.ManageRepository
import com.example.uma_authz_smartphone.data.repository.PatRepository
import com.example.uma_authz_smartphone.data.repository.PolicyRepository
import com.example.uma_authz_smartphone.data.repository.RptRepository
import org.koin.dsl.module

val appModule = module{
    single { AuthzRepository() }
    single { ManageRepository() }
    single { PatRepository() }
    single { PolicyRepository() }
    single { RptRepository() }
}