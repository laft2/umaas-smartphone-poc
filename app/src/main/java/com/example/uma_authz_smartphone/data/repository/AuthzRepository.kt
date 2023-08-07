package com.example.uma_authz_smartphone.data.repository

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.uma_authz_smartphone.data.model.AuthorizationRequest
import com.example.uma_authz_smartphone.data.model.AuthorizationRequestEncrypted
import com.example.uma_authz_smartphone.data.model.ClientRequest
import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.data.model.RequestedResources
import com.example.uma_authz_smartphone.db.model.DbAuthorizationRequest
import com.example.uma_authz_smartphone.db.model.DbPolicy
import com.example.uma_authz_smartphone.workers.RptIssueWorker
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonConfiguration
import org.koin.core.component.KoinComponent
import java.util.concurrent.TimeUnit

class AuthzRepository(
    val policyRepository: PolicyRepository
) {
    fun startAuthorizeRequestWorker(context: Context){
        val workRequest = PeriodicWorkRequestBuilder<RptIssueWorker>(
            15, TimeUnit.MINUTES,
            5, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "getRequestFromQS",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

    }
    suspend fun fetchAuthorizationRequests():List<AuthorizationRequest>{
        val client = HttpClient(OkHttp){
            install(ContentNegotiation) {
                json()
            }
        }
        try {
            val response = client.get("http://10.0.2.2:9010/queue/requests/test")
            println(response.status)
            val a: List<AuthorizationRequest> = response.body()
            println(a)
            client.close()
        }catch (e: Exception){
            println(e)
        }

        return listOf()
    }

    suspend fun testAuthorizationFlow(){
        val requests = fetchAuthorizationRequests()
        for (request in requests) {
            val resources = request.requested_scopes
            val clientInfo = request.client_request

        }
    }

    private fun authorize(
        resources: RequestedResources,
        clientReq: ClientRequest
    ): AuthorizationResult{
        val policyList = mutableListOf<Policy>()
        for (resource in resources.resources) {
            val resourceId = resource.resource_id
            val scopes = resource.resource_scopes

            for (scope in scopes) {
                val policy = policyRepository.getPolicyByScope(resourceId, scope) ?: return AuthorizationResult.DENY
                if (policy.policyType == Policy.PolicyType.DENY) {
                    return AuthorizationResult.DENY
                }
                if (policy.policyType == Policy.PolicyType.MANUAL) {
                    policyList.add(policy)
                }
            }
        }
        if(policyList.isEmpty()){
            return AuthorizationResult.ACCEPT
        }
        return AuthorizationResult.MANUAL
    }

    suspend fun manualAuthorize(requests: RequestedResources, clientReq: ClientRequest){

    }

}


enum class AuthorizationResult{
    ACCEPT, DENY, MANUAL
}
