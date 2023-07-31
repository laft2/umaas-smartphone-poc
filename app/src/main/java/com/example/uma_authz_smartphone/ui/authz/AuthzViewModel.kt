package com.example.uma_authz_smartphone.ui.authz

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uma_authz_smartphone.data.repository.AuthzRepository
import com.example.uma_authz_smartphone.dataStore
import com.example.uma_authz_smartphone.ui.manage.ManageUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthzViewModel(
    private val context: Context,
    private val repository: AuthzRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AuthzUiState())
    val uiState = _uiState.asStateFlow()

    private val qsUriKey = stringPreferencesKey("qsUri")

    init{
        viewModelScope.launch {
            repository.fetchAuthorizationRequests()
        }
    }

    val qsUri: Flow<String> = context.dataStore.data.map {
        it[qsUriKey] ?: ""
    }

    fun onQsUriFieldChange(newValue: String){

        viewModelScope.launch {
            context.dataStore.edit {
                it[qsUriKey] = newValue
            }
        }
    }
}