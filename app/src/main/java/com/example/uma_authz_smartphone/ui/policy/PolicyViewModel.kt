package com.example.uma_authz_smartphone.ui.policy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.data.model.RegisteredResource
import com.example.uma_authz_smartphone.data.repository.AuthzRepository
import com.example.uma_authz_smartphone.datasource.PolicyLocalDataSource
import com.example.uma_authz_smartphone.datasource.RegisteredResourceLocalDataSource
import com.example.uma_authz_smartphone.db.model.DbPolicy
import com.example.uma_authz_smartphone.db.model.DbRegisteredResource
import com.example.uma_authz_smartphone.ui.manage.ManageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PolicyViewModel(
    val authzRepository: AuthzRepository,
    val policyLocalDataSource: PolicyLocalDataSource,
    val resourceLocalDataSource: RegisteredResourceLocalDataSource,
): ViewModel() {
    private val _uiState = MutableStateFlow(PolicyUiState())
    val uiState = _uiState.asStateFlow()

    val policies: StateFlow<List<DbPolicy>> = policyLocalDataSource.fetchPoliciesAsFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun authzTest(){
        viewModelScope.launch {
            authzRepository.testAuthorizationFlow()
        }
    }







}