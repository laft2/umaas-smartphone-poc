package com.example.uma_authz_smartphone.data.repository

import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.datasource.PolicyLocalDataSource
import com.example.uma_authz_smartphone.db.model.DbPolicy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PolicyRepository(private val policyLocalDataSource: PolicyLocalDataSource) {
    private val policies = FakeFactory.policies()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (policyLocalDataSource.fetchPolicies().isEmpty()) {
                policies.forEach {
                    policyLocalDataSource.createPolicy(it)
                }
            }
        }
    }


    fun getPolicyByScope(resourceId: String, scope: String): Policy?{

        for (policy in policies) {
            if(policy.resourceId == resourceId && policy.scope == scope){
                return policy
            }
        }
        return null
    }

    fun getAllPolicies(): List<Policy>{
//        return policies
        return policyLocalDataSource.fetchPolicies().map {
            it.toPolicy()
        }
    }
}

private fun DbPolicy.toPolicy():Policy = Policy(
    id = id.toString(),
    resourceId = "",
    scope = scope?.scope?:"empty",
    policyType = Policy.PolicyType.valueOf(type)
)