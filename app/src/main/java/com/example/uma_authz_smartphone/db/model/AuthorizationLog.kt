package com.example.uma_authz_smartphone.db.model

import com.example.uma_authz_smartphone.data.model.AuthorizationLog
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PersistedName
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.datetime.Instant

@PersistedName(name = "AuthorizationLog")
class DbAuthorizationLog: RealmObject {
    @PersistedName("_id")
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()

    var isApproved: Boolean = true

    var resourceIds: RealmList<String> = realmListOf()

    var clientInfo: String = ""

    @PersistedName("timestamp")
    private var _timestamp: RealmInstant = RealmInstant.from(0, 0)
    var timestamp: Instant
        get() = _timestamp.toInstant()
        set(value) {
            _timestamp = value.toRealmInstant()
        }
}

internal fun RealmInstant.toInstant(): Instant =
    Instant.fromEpochSeconds(epochSeconds, nanosecondsOfSecond.toLong())

internal fun Instant.toRealmInstant(): RealmInstant =
    RealmInstant.from(epochSeconds, nanosecondsOfSecond)