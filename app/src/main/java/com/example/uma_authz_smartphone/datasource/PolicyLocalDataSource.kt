package com.example.uma_authz_smartphone.datasource

import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.db.model.DbPolicy
import com.example.uma_authz_smartphone.db.model.DbRegisteredScope
import io.realm.kotlin.Realm
import io.realm.kotlin.TypedRealm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmUUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PolicyLocalDataSource(
    private val realm: Realm,
    private val resourceLocalDataSource: RegisteredResourceLocalDataSource
) {
    fun fetchPolicies(): List<DbPolicy>{
        return realm.query<DbPolicy>().find().toList()
    }

    fun fetchPoliciesAsFlow(): Flow<List<DbPolicy>> {
        return realm.query<DbPolicy>().asFlow().map { it.list }
    }

    suspend fun createPolicy(policy: Policy): DbPolicy?{
        val fetchedResource = resourceLocalDataSource.fetchResourceByResourceId(policy.resourceId)
        fetchedResource?: return null
        val fetchedScope: DbRegisteredScope? = fetchedResource.resourceScopes.find {
            it.scope == policy.scope
        }
        fetchedScope?: return null
        return realm.write {
            copyToRealm(
                DbPolicy().apply {
                    scope = findLatest(fetchedScope)
                    type = policy.policyType.name
                    resource = findLatest(fetchedResource)
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

    fun fetchPolicyByScope(resourceId: String, scope: String): DbPolicy? {
        return realm.query(
            DbPolicy::class,
            "resource.resourceId == $0 AND scope.scope == $1",
            resourceId,
            scope
        ).first().find()
    }
}

private fun TypedRealm.fetchPolicy(policy: Policy): DbPolicy?{
    return query(DbPolicy::class, "id == $0", RealmUUID.from(policy.id)).first().find()
}