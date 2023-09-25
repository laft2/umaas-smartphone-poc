package com.example.uma_authz_smartphone.data.model


data class Policy(
    val id: String,
    val resourceId: String,
    val scope: String,
    val policyType: PolicyType,
    val resource: RegisteredResource? = null,
){
    enum class PolicyType {
        ACCEPT, DENY, MANUAL
    }
}