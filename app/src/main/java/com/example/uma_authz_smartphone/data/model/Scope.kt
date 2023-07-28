package com.example.uma_authz_smartphone.data.model

data class Scope(
    val accessType: AccessType,
    val resource: String
){
    enum class AccessType{
        READ, WRITE, BOTH
    }
}
