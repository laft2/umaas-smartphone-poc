package com.example.uma_authz_smartphone.db.model

import com.example.uma_authz_smartphone.data.model.Policy
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PersistedName
import io.realm.kotlin.types.annotations.PrimaryKey

@PersistedName(name = "Policy")
class DbPolicy(): RealmObject{
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var resource: DbRegisteredResource? = null
    var scope: DbRegisteredScope? = null
    var type: String = Policy.PolicyType.MANUAL.name

    companion object{
        fun getTestValue(resource: DbRegisteredResource?): DbPolicy {
            val res = DbPolicy()
            res.resource = resource
            res.scope = resource?.resourceScopes?.get(0)
            return res
        }
    }

}