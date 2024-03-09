package com.example.uma_authz_smartphone.datasource

import com.example.uma_authz_smartphone.data.model.AuthorizationRequest
import com.example.uma_authz_smartphone.db.model.DbAuthorizationRequest
import com.example.uma_authz_smartphone.db.model.DbClientInfo
import com.example.uma_authz_smartphone.db.model.DbRegisteredResource
import com.example.uma_authz_smartphone.db.model.DbRequestedResource
import com.example.uma_authz_smartphone.db.model.DbRequestedScope
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.realmListOf

class AuthorizationRequestLocalDataSource(private val realm: Realm) {
    fun fetchRequests(): List<DbAuthorizationRequest> {
        return realm.query(DbAuthorizationRequest::class).find().toList()
    }

    fun fetchRequestsWaiting(){
        realm.query(DbAuthorizationRequest::class, "isWaiting == true").find().toList()
    }

    suspend fun createManualRequest(authorizationRequest: AuthorizationRequest){
        val reqResources = authorizationRequest.requested_scopes.resources
        val realmResources = realmListOf<DbRequestedResource>()
        for(reqResource in reqResources){
            val realmRequestedScopes = realmListOf<DbRequestedScope>()
            for(reqScope in reqResource.resource_scopes){
                realmRequestedScopes.add(
                    DbRequestedScope().apply {
                        scope = reqScope
                    }
                )
            }
            realmResources.add(
                DbRequestedResource().apply {
                    resourceId = reqResource.resource_id
                    scopes = realmRequestedScopes
                }
            )
        }
        realm.write {
            copyToRealm(
                DbAuthorizationRequest().apply {
                    isWaiting = true
                    resources = realmResources
                    clientInfo = DbClientInfo().apply {
                        domain = "test_domain.example.com"
                        publicKey = "test"
                        rptEndpoint = "test_domain.example.com/callback/rpt"
                    }
                }
            )
        }
    }


    fun getRegisteredResource(resourceId: String): DbRegisteredResource? {
        return realm.query(DbRegisteredResource::class, "resourceId == $1", resourceId).first().find()
    }
}