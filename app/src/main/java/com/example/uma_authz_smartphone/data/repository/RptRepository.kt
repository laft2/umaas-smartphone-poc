package com.example.uma_authz_smartphone.data.repository

import com.example.uma_authz_smartphone.data.model.AccessToken
import com.example.uma_authz_smartphone.data.model.Client
import com.example.uma_authz_smartphone.data.model.ResourceServer
import java.time.LocalDateTime

class RptRepository {
    fun createRpt(): AccessToken{
        var rpt = AccessToken(
            tokenPurpose = AccessToken.TokenPurpose.RPT
        )
        return rpt
    }
    private fun getTokenString(
        rs: ResourceServer,
        client: Client,
        expireTime: LocalDateTime,

    ):String{
        return ""
    }
}