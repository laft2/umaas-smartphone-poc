package com.example.uma_authz_smartphone.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientRequest(
    val grant_type: String,
    val ticket: String,
    val claim_token: String? = null,
    val claim_token_format: String? = null,
    val pct: String? = null,
    val rpt: String? = null,
    val scopes: String? = null,
//    val created_at: String
)
