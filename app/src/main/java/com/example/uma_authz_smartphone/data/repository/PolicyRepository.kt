package com.example.uma_authz_smartphone.data.repository

import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.datasource.PolicyLocalDataSource
import com.example.uma_authz_smartphone.datasource.toRegisteredResource
import com.example.uma_authz_smartphone.db.model.DbPolicy
import io.realm.kotlin.ext.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PolicyRepository(private val policyLocalDataSource: PolicyLocalDataSource) {
    private val policies = FakeFactory.policies()

    init {
//        CoroutineScope(Dispatchers.IO).launch {
//            if (policyLocalDataSource.fetchPolicies().isEmpty()) {
//                policies.forEach {
//                    policyLocalDataSource.createPolicy(it)
//                }
//            }
//        }
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