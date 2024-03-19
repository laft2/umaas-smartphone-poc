package com.example.uma_authz_smartphone.data.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmUUID
import kotlinx.datetime.LocalDateTime

class AuthorizationLog(
    val isApproved: Boolean,
    val resourceIds: List<String>,
    val clientInfo: String,
    val timestamp: LocalDateTime,
)