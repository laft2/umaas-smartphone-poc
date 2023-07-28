package com.example.uma_authz_smartphone.data.model

data class AccessToken(
    val token: String = "",
    val scopes: List<Scope> = listOf(),
    val tokenType: String = "Bearer",
    val tokenPurpose: TokenPurpose = TokenPurpose.RPT
){
    enum class TokenPurpose{
        PAT, RPT
    }
}