package com.example.uma_authz_smartphone.ui.authz

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.uma_authz_smartphone.data.model.AuthorizationRequest
import com.example.uma_authz_smartphone.data.model.ClientRequest
import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.data.model.RequestedResource
import com.example.uma_authz_smartphone.data.model.RequestedResources
import com.example.uma_authz_smartphone.data.repository.AuthzRepository
import com.example.uma_authz_smartphone.data.repository.PolicyRepository
import com.example.uma_authz_smartphone.dataStore
import com.example.uma_authz_smartphone.workers.AuthorizeWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AuthzViewModel(
    private val context: Context,
    private val authzRepository: AuthzRepository,
    private val policyRepository: PolicyRepository,
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

    private suspend fun fakeObtainRequests(targetUri: String): List<AuthorizationRequest>{
        // QS mock
        var authorizationRequests = mutableListOf<AuthorizationRequest>()
        authorizationRequests.add(
            AuthorizationRequest(
                "test_ticket",
                RequestedResources(
                    resources = listOf(RequestedResource(
                        resource_id = "test_resource_id",
                        resource_scopes = listOf(""),
                    )),
                    client_key = "test_client_key",
                    ),
                client_request = ClientRequest(
                    "test_grant_type",
                    "test_ticket"
                )
            )
        )
        return authorizationRequests
    }
    fun authorizeRequestsFromQS(){
        val targetUri = uiState.value.qsUri
        viewModelScope.launch(){
            val requests = fakeObtainRequests(targetUri)
            for (request in requests){
                val result = authorizeRequests(request.requested_scopes.resources)
                if (result){
                    // TODO: implement for showing and sending results to client via QS
                }else{
                    // TODO: implement
                }
            }
        }
    }

    private fun authorizeRequests(
        resources: List<RequestedResource>
    ):Boolean{
        var authorizationResult = true
        resources.forEach {
            if(!authorizeRequest(it.resource_id, it.resource_scopes)){
                authorizationResult = false
            }
        }
        return authorizationResult
    }
    private fun authorizeRequest(
        resourceId: String,
        scopes: List<String>,
    ): Boolean{
        val policyList = mutableListOf<Policy>()
        for (scope in scopes) {
            val policy = policyRepository.getPolicyByScope(resourceId, scope) ?: return false
            if(policy.policyType == Policy.PolicyType.DENY){
                return false
            }
            if(policy.policyType == Policy.PolicyType.MANUAL){
                policyList.add(policy)
            }
        }
        if(policyList.isEmpty()){
            return true
        }
        // TODO: solve manual policy
        return false
    }

    fun <T: ListenableWorker> enqueueWork(data: Data){
        val workName = "workName"
        val workManager = WorkManager.getInstance(context)
        val a = OneTimeWorkRequestBuilder<AuthorizeWorker>()
            .setInputData(data)
            .build()
            .also {
                workManager.enqueueUniqueWork(
                    workName,
                    ExistingWorkPolicy.APPEND,
                    it
                )
            }

    }
}