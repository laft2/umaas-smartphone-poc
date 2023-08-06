package com.example.uma_authz_smartphone.data.repository

import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.data.model.RegisteredResource

internal class FakeFactory {
    companion object{
        fun registeredResources(): List<RegisteredResource>{
            return listOf(
                RegisteredResource(
                    rsId = "rs_1",
                    resourceId = "7b727369647d",
                    resourceScopes = listOf("view", "crop", "lightbox")
                ),
                RegisteredResource(
                    rsId = "rs_1",
                    resourceId = "112210f47de98100",
                    resourceScopes = listOf(
                        "view",
                        "http://photoz.example.com/dev/scopes/print",
                    ),
                    description = "Collection of digital photographs",
                    iconUri = "http://www.example.com/icons/flower.png",
                    name = "Photo Album",
                    type = "http://www.example.com/rsrcs/photoalbum",
                ),
            )
        }
        fun policies():List<Policy>{
            return listOf(
                Policy(
                    id = "tekitouniikouyou",
                    resourceId = "7b727369647d",
                    scope = "view",
                    policyType = Policy.PolicyType.ACCEPT
                ),
                Policy(
                    id = "very tekitou uuid",
                    resourceId = "7b727369647d",
                    scope = "crop",
                    policyType = Policy.PolicyType.MANUAL,
                ),
                Policy(
                    id = "tekitou",
                    resourceId = "7b727369647d",
                    scope = "lightbox",
                    policyType = Policy.PolicyType.DENY,
                ),
                Policy(
                    id = "tekitou",
                    resourceId = "112210f47de98100",
                    scope = "view",
                    policyType = Policy.PolicyType.ACCEPT,
                ),
                Policy(
                    id = "tekitou",
                    resourceId = "112210f47de98100",
                    scope = "http://photoz.example.com/dev/scopes/print",
                    policyType = Policy.PolicyType.MANUAL,
                ),

            )
        }
    }

}