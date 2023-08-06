package com.example.uma_authz_smartphone.db.model

import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.data.model.RegisteredResource
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class DbRegisteredResource():RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var rs: DbResourceServer? = null
    var description: String = ""
    var resourceScopes: RealmList<String> = realmListOf()
    var iconUri: String = ""
    var name: String = ""
    var type: String = ""
}