package com.example.uma_authz_smartphone.data.model
import kotlinx.serialization.Serializable

@Serializable
data class AccessToken(
    val scopes: List<RequestedResource> = listOf(),
    val encrypted_key: String = "",
    val signed_key: String = "",

    val token_type: String = "Bearer",
)

@Serializable
data class AccessTokenForClient(
    val access_token: AccessToken = AccessToken(),
    val encrypted_key: String = "",
    val signed_key: String = "",
)