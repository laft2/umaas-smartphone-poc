package com.example.uma_authz_smartphone.data.model

data class RegisteredResource(
    val resourceId: String,
    val rsId: String,
    val resourceScopes: List<String> = listOf(),
    val description: String? = null,
    val iconUri: String? = null,
    val name: String? = null,
    val type: String? = null,
)