package com.example.uma_authz_smartphone.db.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class DbResourceServer:RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var publicKey: String = ""

    companion object{
        fun getTestValue(): DbResourceServer{
            return DbResourceServer()
        }
    }
}