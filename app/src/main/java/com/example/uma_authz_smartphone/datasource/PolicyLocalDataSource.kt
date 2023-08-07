package com.example.uma_authz_smartphone.datasource

import android.util.Log
import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.db.model.DbPolicy
import com.example.uma_authz_smartphone.db.model.DbRegisteredScope
import io.realm.kotlin.Realm
import io.realm.kotlin.TypedRealm
import io.realm.kotlin.types.RealmUUID

class PolicyLocalDataSource(private val realm: Realm) {
    fun fetchPolicies(): List<DbPolicy>{
        return realm.query(DbPolicy::class).find().toList()
    }

    suspend fun createPolicy(policy: Policy){
        realm.write {
            copyToRealm(
                DbPolicy().apply {
                    scope = DbRegisteredScope().apply {
                        scope = policy.scope
                    }
                    type = policy.policyType.name
                }
            )
        }
    }

    suspend fun deletePolicy(policy: Policy){
        realm.write {
            val dbPolicy = fetchPolicy(policy)
            if(dbPolicy != null){
                delete(dbPolicy)
            }
        }
    }

    fun fetchPoliciesByScopes(){

    }
}

private fun TypedRealm.fetchPolicy(policy: Policy): DbPolicy?{
    return query(DbPolicy::class, "id == $0", RealmUUID.from(policy.id)).first().find()
}