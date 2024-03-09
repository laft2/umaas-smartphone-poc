package com.example.uma_authz_smartphone.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uma_authz_smartphone.ui.authz.AuthzViewModel
import com.example.uma_authz_smartphone.ui.manage.ManageViewModel
import com.example.uma_authz_smartphone.ui.policy.PolicyViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val viewModelModule = module {
    viewModel { AuthzViewModel(get(), get(), get()) }
    viewModel { ManageViewModel() }
    viewModel { PolicyViewModel(get(), get(), get()) }
}