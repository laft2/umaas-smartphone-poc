package com.example.uma_authz_smartphone.data.repository

import com.example.uma_authz_smartphone.data.model.RegisteredResource
import com.example.uma_authz_smartphone.datasource.RegisteredResourceLocalDataSource
import com.example.uma_authz_smartphone.db.model.DbRegisteredResource

class RegisteredResourceRepository(
    val resourceLocalDataSource: RegisteredResourceLocalDataSource
) {
    private val resources = FakeFactory.registeredResources()
    fun getResource(resourceId: String): RegisteredResource?{
        return resourceLocalDataSource.fetchResourceByResourceId(resourceId)?.toRegisteredResource()
    }

}

private fun DbRegisteredResource.toRegisteredResource(): RegisteredResource = RegisteredResource(
    resourceId = resourceId,
    rsId = rs?.id?.toString()?:"",
    resourceScopes = resourceScopes.map { it.scope },
    description = description,
    iconUri = iconUri,
    name = name,
    type = type,
)