package com.example.uma_authz_smartphone.db.model

import com.example.uma_authz_smartphone.data.model.Policy
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class DbAuthorizationRequest: RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var resources: DbRequestedResource? = null
    var clientInfo: DbClientInfo? = null
    var isWaiting: Boolean = true
}

class DbRequestedResource: EmbeddedRealmObject {
    var resourceId: String = ""
    var resource: DbRegisteredResource? = null
    var scopes: RealmList<DbRequestedScope> = realmListOf()
}

class DbRequestedScope: EmbeddedRealmObject{
    var scope: String = ""
}

class DbClientInfo: EmbeddedRealmObject {
    var domain: String = ""
    var publicKey: String = ""
    var rptEndpoint: String = ""
}