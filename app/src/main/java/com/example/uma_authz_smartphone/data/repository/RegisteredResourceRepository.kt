package com.example.uma_authz_smartphone.data.repository

import com.example.uma_authz_smartphone.data.model.RegisteredResource

class RegisteredResourceRepository {
    private val resources = FakeFactory.registeredResources()
    fun getResource(resourceId: String): RegisteredResource?{
        for (resource in resources) {
            if(resource.resourceId == resourceId){
                return resource
            }
        }
        return null
    }

}