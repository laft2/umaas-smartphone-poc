package com.example.uma_authz_smartphone.datasource

import com.example.uma_authz_smartphone.data.model.AuthorizationRequest
import com.example.uma_authz_smartphone.data.model.RequestedResource
import com.example.uma_authz_smartphone.db.model.DbAuthorizationRequest
import com.example.uma_authz_smartphone.db.model.DbRegisteredResource
import com.example.uma_authz_smartphone.db.model.DbRequestedResource
import io.realm.kotlin.Realm

class AuthorizationRequestLocalDataSource(private val realm: Realm) {
    fun fetchRequests(): List<DbAuthorizationRequest> {
        return realm.query(DbAuthorizationRequest::class).find().toList()
    }

    fun fetchRequestsWaiting(){
        realm.query(DbAuthorizationRequest::class, "isWaiting == true").find().toList()
    }

    suspend fun createManualRequest(authorizationRequest: AuthorizationRequest){
        val resources = authorizationRequest.requested_scopes.resources
        realm.write {
            copyToRealm(
                DbAuthorizationRequest().apply {
                    isWaiting = true


                }
            )
        }
    }


    fun getRegisteredResource(resourceId: String): DbRegisteredResource? {
        return realm.query(DbRegisteredResource::class, "resourceId == $1", resourceId).first().find()
    }
}