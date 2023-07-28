package com.example.uma_authz_smartphone.data.model


data class Policy(
    val policyType: PolicyType
){
    enum class PolicyType {
        ACCEPT, DENY, MANUAL
    }
}