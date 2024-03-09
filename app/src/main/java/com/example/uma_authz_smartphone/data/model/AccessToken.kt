package com.example.uma_authz_smartphone.data.model

data class AccessToken(
    val token: String = "",
    val requestedResources: List<RequestedResource> = listOf(),
    val tokenType: String = "Bearer",
    val tokenPurpose: TokenPurpose = TokenPurpose.RPT
){
    enum class TokenPurpose{
        PAT, RPT
    }
}