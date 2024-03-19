package com.example.uma_authz_smartphone.db.model

import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.data.model.RegisteredResource
import io.realm.kotlin.ext.backlinks
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class DbRegisteredResource():RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var resourceId: String = ""
    var rs: DbResourceServer? = null
    var description: String = ""
    var resourceScopes: RealmList<DbRegisteredScope> = realmListOf()
    var iconUri: String = ""
    var name: String = ""
    var type: String = ""

    companion object{
        fun getTestValue(rs: DbResourceServer?): DbRegisteredResource{
            val res = DbRegisteredResource()
            res.resourceId = "test_resource"
            res.rs = rs
            res.description = "test resource"
            res.resourceScopes = realmListOf(
                DbRegisteredScope().apply { scope = "view" },
                DbRegisteredScope().apply { scope = "test" },
            )
            res.name = "test"
            res.type = "test type"
            return res
        }
    }

}

class DbRegisteredScope:RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    val resource: RealmResults<DbRegisteredResource> by backlinks(DbRegisteredResource::resourceScopes)
    var scope: String = ""
    val policy: RealmResults<DbPolicy> by backlinks(DbPolicy::scope)
}