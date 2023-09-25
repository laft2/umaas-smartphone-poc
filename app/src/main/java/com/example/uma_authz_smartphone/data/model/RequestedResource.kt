package com.example.uma_authz_smartphone.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestedResources(
    val resources: List<RequestedResource>,
    val client_key: String, // public key
)

@Serializable
data class RequestedResource(
    val resource_id: String,
    val resource_scopes: List<String>
)
