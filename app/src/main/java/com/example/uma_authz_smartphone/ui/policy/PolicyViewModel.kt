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

    init{
        generateTestPolicy()
    }

    fun generateTestPolicy(){

        viewModelScope.launch {
            generateTestRegisteredResource()
            val policy = Policy(
                id = "test",
                policyType = Policy.PolicyType.MANUAL,
                scope = "test",
                resourceId = "test_resource_id"
            )
            val dbPolicy = policyLocalDataSource.createPolicy(policy)
            Log.d("testAuthorizationFlow", dbPolicy.toString())
            val policy2 = policy.copy(scope = "view", policyType = Policy.PolicyType.ACCEPT)
            val dbPolicy2 = policyLocalDataSource.createPolicy(policy2)
            val policy3 = policy.copy(scope = "edit", policyType = Policy.PolicyType.DENY)
            policyLocalDataSource.createPolicy(policy3)
            Log.d("testAuthorizationFlow", dbPolicy2.toString())
        }
    }

    private suspend fun generateTestRegisteredResource(): DbRegisteredResource {
        return resourceLocalDataSource.createResource(RegisteredResource(
            resourceId = "test_resource_id",
            rsId = "",
            resourceScopes = listOf(
                "test",
                "view",
                "edit",
            ),
            description = "for test",
            name = "test resource",
        ))
    }


}