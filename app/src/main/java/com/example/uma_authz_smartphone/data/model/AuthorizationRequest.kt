package com.example.uma_authz_smartphone.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthorizationRequest(
    val ticket: String,
    val requested_scopes: RequestedResources,
    val client_request: ClientRequest,
)

@Serializable
data class AuthorizationRequestEncrypted(
    val ticket: String,
    val requested_scopes: String,
    val client_request: String,
)