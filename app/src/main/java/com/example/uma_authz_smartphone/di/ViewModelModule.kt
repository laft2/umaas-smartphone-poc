package com.example.uma_authz_smartphone.di

import com.example.uma_authz_smartphone.ui.manage.ManageViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val viewModelModule = module {
    viewModel { ManageViewModel(get()) }
}