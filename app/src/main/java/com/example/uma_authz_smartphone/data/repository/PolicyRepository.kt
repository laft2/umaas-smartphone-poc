package com.example.uma_authz_smartphone.data.repository

import com.example.uma_authz_smartphone.data.model.Policy

class PolicyRepository {
    private val policies = FakeFactory.policies()
    fun getPolicyByScope(resourceId: String, scope: String): Policy?{
        for (policy in policies) {
            if(policy.resourceId == resourceId && policy.scope == scope){
                return policy
            }
        }
        return null
    }
}