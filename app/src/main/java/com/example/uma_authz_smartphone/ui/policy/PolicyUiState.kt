package com.example.uma_authz_smartphone.ui.policy

import com.example.uma_authz_smartphone.data.model.Policy

data class PolicyUiState(
    val policyList: List<Policy> = listOf()
)