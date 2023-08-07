package com.example.uma_authz_smartphone.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientRequest(
    val id: Int,
    val grant_type: String,
    val ticket: String,
    val claim_token: String?,
    val claim_token_format: String?,
    val pct: String?,
    val rpt: String?,
    val scopes: String?,
//    val created_at: String
)
