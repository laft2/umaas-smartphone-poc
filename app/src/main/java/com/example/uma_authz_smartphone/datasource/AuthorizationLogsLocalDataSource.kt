package com.example.uma_authz_smartphone.datasource

import com.example.uma_authz_smartphone.data.model.AuthorizationLog
import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.db.model.DbAuthorizationLog
import com.example.uma_authz_smartphone.db.model.DbPolicy
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class AuthorizationLogsLocalDataSource(private val realm: Realm) {

    fun fetchAllLogs(): List<DbAuthorizationLog>{
        return realm.query<DbAuthorizationLog>().find().toList()
    }

    fun fetchLogsAsFlow(): Flow<List<DbAuthorizationLog>> {
        return realm.query<DbAuthorizationLog>().asFlow().map { it.list }
    }
    suspend fun createLog(authorizationLog: AuthorizationLog):DbAuthorizationLog?{
        return realm.write {
            copyToRealm(
                DbAuthorizationLog().apply {
                    isApproved = authorizationLog.isApproved
                    resourceIds = realmListOf(*authorizationLog.resourceIds.toTypedArray())
                    clientInfo = authorizationLog.clientInfo
                    timestamp = authorizationLog.timestamp.toInstant(TimeZone.currentSystemDefault())
                }
            )
        }
    }
}

fun DbAuthorizationLog.toAuthorizationLog(): AuthorizationLog = AuthorizationLog(
    isApproved = this.isApproved,
    resourceIds = this.resourceIds.toList(),
    clientInfo = this.clientInfo,
    timestamp = this.timestamp.toLocalDateTime(TimeZone.currentSystemDefault()),
)