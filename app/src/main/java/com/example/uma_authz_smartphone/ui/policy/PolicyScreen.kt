package com.example.uma_authz_smartphone.ui.policy

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.data.repository.AuthzRepository
import com.example.uma_authz_smartphone.data.repository.PolicyRepository
import com.example.uma_authz_smartphone.data.repository.RegisteredResourceRepository
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

@Composable
fun PolicyScreen(
    viewModel: PolicyViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    policyRepository: PolicyRepository = get()
) {
    val policies = policyRepository.getAllPolicies()

    Column {
        for (policy in policies) {
            PolicyCard(policy = policy)
        }
    }
}

@Composable
fun PolicyCard(policy: Policy){
    Row(
        Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start

    ){
        val resourceRepository = RegisteredResourceRepository()
        val resource = resourceRepository.getResource(policy.resourceId)
        Spacer(Modifier.padding(3.dp))
        Icon(Icons.Filled.Info, contentDescription = "rs_favicon")
        Spacer(modifier = Modifier.padding(10.dp))
        Column{
            if (resource?.name != null) Text(text = resource.name)
            Text(text = policy.scope)
            Text(text = policy.policyType.toString())
        }
    }
}

@Preview
@Composable
fun PolicyCardPreview(
    repo: PolicyRepository = get()
){
    val policy = repo.getAllPolicies()[0]
    PolicyCard(policy = policy)
}