package com.example.uma_authz_smartphone.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.data.repository.AuthzRepository
import com.example.uma_authz_smartphone.data.repository.PolicyRepository

class AuthorizeWorker(
    appContext: Context,
    workerParams: WorkerParameters,
):
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val authzRepository = AuthzRepository()
        val requests = authzRepository.fetchAuthorizationRequests()

        TODO("Not yet implemented")
    }
    private fun authorize(
        resourceId: String,
        scopes: List<String>,
    ): Boolean{
        val policyList = mutableListOf<Policy>()
        val policyRepository = PolicyRepository()
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
}