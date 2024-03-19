package com.example.uma_authz_smartphone.ui.authz

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.auth0.jwt.JWT
import com.example.uma_authz_smartphone.data.model.AccessToken
import com.example.uma_authz_smartphone.data.model.AccessTokenForClient
import com.example.uma_authz_smartphone.data.model.AuthorizationLog
import com.example.uma_authz_smartphone.data.model.AuthorizationRequest
import com.example.uma_authz_smartphone.data.model.ClientRequest
import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.data.model.RequestedResource
import com.example.uma_authz_smartphone.data.model.RequestedResources
import com.example.uma_authz_smartphone.data.repository.PolicyRepository
import com.example.uma_authz_smartphone.dataStore
import com.example.uma_authz_smartphone.datasource.AuthorizationLogsLocalDataSource
import com.example.uma_authz_smartphone.datasource.toAuthorizationLog
import com.example.uma_authz_smartphone.db.model.DbAuthorizationLog
import com.example.uma_authz_smartphone.workers.AuthorizeWorker
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HeadersBuilder
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AuthzViewModel(
    private val context: Context,
    private val authzLogsLocalDataSource: AuthorizationLogsLocalDataSource,
    private val policyRepository: PolicyRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(AuthzUiState())
    val uiState = _uiState.asStateFlow()

    private val qsUriKey = stringPreferencesKey("qsUri")

    val authorizationLogs: StateFlow<List<DbAuthorizationLog>> = authzLogsLocalDataSource.fetchLogsAsFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun updateQsUriUiState(newValue: String){
        _uiState.update {
            it.copy(
                qsUri = newValue
            )
        }
    }

    fun getQsUriPreference(): String{
        var res = ""
        runBlocking(Dispatchers.IO) {
            res = context.dataStore.data.map { it[qsUriKey]?:"" }.first()
        }
        return res
    }

    fun editQsUriPreference(newValue: String){
        viewModelScope.launch {
            context.dataStore.edit {
                Log.d("edit pref", newValue)
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
                        resource_scopes = listOf("view"),
                    )),
                    client_key = "test_client_key",
                    ),
                client_request = ClientRequest(
                    "test_grant_type",
                    "test_ticket",
                    client_info = "sample_client"
                )
            )
        )
        return authorizationRequests
    }


    private suspend fun obtainRequests(targetUri: String):List<AuthorizationRequest>{
        val client = HttpClient(OkHttp){
            install(ContentNegotiation) {
                json()
            }
        }
        try {
            val response = client.get(targetUri)
            println(response.status)
            val a: List<AuthorizationRequest> = response.body()
            println(a)
            client.close()
            return a
        }catch (e: Exception){
            println(e)
        }

        return listOf()
    }

    fun authorizeRequestsFromQS(){
        viewModelScope.launch(){
            val targetUri = getQsUriPreference()
            val requests = obtainRequests(targetUri)
            for (request in requests){
                val result = authorizeRequests(request.requested_scopes.resources)
                authzLogsLocalDataSource.createLog(AuthorizationLog(
                    isApproved = result,
                    resourceIds = request.requested_scopes.resources.map { it.resource_id },
                    timestamp = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    clientInfo = request.client_request.client_info,
                ))
                if(result){
                    SendAT(targetUri, request.requested_scopes.resources)
                }
            }
        }
    }

//    fun fetchAuthorizationLogs(): List<AuthorizationLog>{
//        val logs = authzLogsLocalDataSource.fetchAllLogs().map { it.toAuthorizationLog() }
//        return logs.sortedBy { it.timestamp }
//    }


    private suspend fun SendAT(targetUri: String, targetScopes: List<RequestedResource>):Boolean{
        val client = HttpClient(OkHttp){
            install(ContentNegotiation) {
                json()
            }
        }
        val accessToken = AccessToken(
            scopes = targetScopes,
            encrypted_key = "PK_RS", signed_key = "SK_AR"
        )
        try {
            val response = client.post(targetUri){
                contentType(ContentType.Application.Json)
                setBody(AccessTokenForClient(
                    access_token = accessToken,
                    encrypted_key = "PK_C",
                    signed_key = "SK_AC",
                ))
            }
            println(response.status)
            val responseCode = response.status.value
            client.close()
            return responseCode in 200..299
        }catch (e: Exception){
            println(e)
            return false
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
        // TODO: solve manual policy for policyList
        return false
    }

}