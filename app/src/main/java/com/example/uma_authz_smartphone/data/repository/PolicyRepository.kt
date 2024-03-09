package com.example.uma_authz_smartphone.data.repository

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.data.model.RegisteredResource
import com.example.uma_authz_smartphone.datasource.PolicyLocalDataSource
import com.example.uma_authz_smartphone.datasource.RegisteredResourceLocalDataSource
import com.example.uma_authz_smartphone.datasource.toRegisteredResource
import com.example.uma_authz_smartphone.db.model.DbPolicy
import com.example.uma_authz_smartphone.db.model.DbRegisteredResource
import io.realm.kotlin.ext.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PolicyRepository(
    private val policyLocalDataSource: PolicyLocalDataSource,
    private val resourceLocalDataSource: RegisteredResourceLocalDataSource
) {
    private val policies = FakeFactory.policies()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            generateTestPolicy()
        }
    }

    private suspend fun generateTestPolicy(){
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

    private suspend fun generateTestRegisteredResource(): DbRegisteredResource {
        return resourceLocalDataSource.createResource(
            RegisteredResource(
            resourceId = "test_resource_id",
            rsId = "",
            resourceScopes = listOf(
                "test",
                "view",
                "edit",
            ),
            description = "for test",
            name = "test resource",
        )
        )
    }

    fun getPolicyByScope(resourceId: String, scope: String): Policy?{
        return policyLocalDataSource.fetchPolicyByScope(resourceId, scope)?.toPolicy()
    }

    fun getAllPolicies(): List<Policy>{
//        return policies

        return policyLocalDataSource.fetchPolicies().map {
            it.toPolicy()
        }
    }
}

fun DbPolicy.toPolicy():Policy = Policy(
    id = id.toString(),
    resourceId = resource?.resourceId?:"",
    scope = scope?.scope?:"empty",
    policyType = Policy.PolicyType.valueOf(type),
    resource = resource?.toRegisteredResource(),
)