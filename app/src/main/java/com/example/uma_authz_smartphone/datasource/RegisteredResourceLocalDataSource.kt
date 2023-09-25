package com.example.uma_authz_smartphone.datasource

import com.example.uma_authz_smartphone.data.model.RegisteredResource
import com.example.uma_authz_smartphone.db.model.DbPolicy
import com.example.uma_authz_smartphone.db.model.DbRegisteredResource
import com.example.uma_authz_smartphone.db.model.DbRegisteredScope
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList

class RegisteredResourceLocalDataSource(private val realm:Realm) {
    fun fetchResources(): List<DbRegisteredResource>{
        return realm.query(DbRegisteredResource::class).find().toList()
    }

    fun fetchResourceByResourceId(resourceId: String) :DbRegisteredResource?{
        return realm.query(DbRegisteredResource::class, "resourceId == $0", resourceId).first().find()
    }

    suspend fun createResource(resource: RegisteredResource): DbRegisteredResource {
        return realm.write {
            val scopes = resource.resourceScopes.map {
                copyToRealm(
                    DbRegisteredScope().apply {
                        scope = it
                    }
                )
            }
            return@write copyToRealm(
                DbRegisteredResource().apply {
                    resourceId = resource.resourceId
                    description = resource.description?:""
                    iconUri = resource.iconUri?:""
                    name = resource.name?:""
                    type = resource.type?:""
                    resourceScopes = scopes.toRealmList()
                }
            )
        }
    }

    private suspend fun createScopes(scopes: List<String>){
        scopes.map {

        }
    }

}
fun DbRegisteredResource.toRegisteredResource(): RegisteredResource = RegisteredResource(
    resourceId = resourceId,
    rsId = "",
    resourceScopes = resourceScopes.map {
        it.scope
    },
    description = description,
    iconUri = iconUri,
    name = name,
    type = type,
)