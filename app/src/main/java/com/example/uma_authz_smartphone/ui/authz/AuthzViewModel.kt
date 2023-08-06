package com.example.uma_authz_smartphone.ui.authz

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.data.repository.AuthzRepository
import com.example.uma_authz_smartphone.data.repository.PolicyRepository
import com.example.uma_authz_smartphone.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AuthzViewModel(
    private val context: Context,
    private val authzRepository: AuthzRepository,
//    private val policyRepository: PolicyRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(AuthzUiState())
    val uiState = _uiState.asStateFlow()

    private val qsUriKey = stringPreferencesKey("qsUri")

    init{
        viewModelScope.launch {
//            authzRepository.fetchAuthorizationRequests()
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


//    fun authorize(
//        resourceId: String,
//        scopes: List<String>,
//    ): Boolean{
//        val policyList = mutableListOf<Policy>()
//        for (scope in scopes) {
//            val policy = policyRepository.getPolicyByScope(resourceId, scope) ?: return false
//            if(policy.policyType == Policy.PolicyType.DENY){
//                return false
//            }
//            if(policy.policyType == Policy.PolicyType.MANUAL){
//                policyList.add(policy)
//            }
//        }
//        if(policyList.isEmpty()){
//            return true
//        }
//        // TODO: solve manual policy
//        return false
//    }
}