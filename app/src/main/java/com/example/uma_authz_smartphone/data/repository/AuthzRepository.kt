package com.example.uma_authz_smartphone.data.repository

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.uma_authz_smartphone.data.model.AccessToken
import com.example.uma_authz_smartphone.data.model.AuthorizationRequest
import com.example.uma_authz_smartphone.workers.RptIssueWorker
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.koin.core.component.KoinComponent
import java.util.concurrent.TimeUnit

class AuthzRepository:KoinComponent {
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
            val response = client.get("http://10.0.2.2:9010/queue/requests")
            println(response.status)
            val a: Test = response.body()
            println(a.a)
            client.close()
        }catch (e: Exception){
            println(e)
        }

        return listOf()
    }

}
@Serializable
data class Test(
    val a: String
)