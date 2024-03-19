package com.example.uma_authz_smartphone.ui.policy

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uma_authz_smartphone.data.model.Policy
import com.example.uma_authz_smartphone.data.repository.PolicyRepository
import com.example.uma_authz_smartphone.data.repository.RegisteredResourceRepository
import com.example.uma_authz_smartphone.data.repository.toPolicy
import com.example.uma_authz_smartphone.datasource.PolicyLocalDataSource
import com.example.uma_authz_smartphone.db.model.DbPolicy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

@Composable
fun PolicyScreen(
    viewModel: PolicyViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    policyRepository: PolicyRepository = get(),

) {
    val policies by viewModel.policies.collectAsStateWithLifecycle()
    





    Column {
//        Button(onClick = { viewModel.authzTest() }) {
//            Text(text = "test")
//        }
//        Button(onClick = { viewModel.generateTestPolicy() }) {
//            Text(text = "gen test policy")
//        }
        for (policy in policies) {
            PolicyCard(policy = policy)
        }
    }
}

@Composable
fun PolicyCard(
    policy: DbPolicy,
){
    Row(
        Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start

    ){
        Log.d("authzTestFlow", policy.toString())
        val resource = policy.resource
        val fontSize = 20.sp
        Spacer(Modifier.padding(3.dp))
        Icon(Icons.Filled.Info, contentDescription = "rs_favicon")
        Spacer(modifier = Modifier.padding(10.dp))
        Column{
            if (resource?.resourceId != null) Text(text = "resource id:\t" + resource.resourceId, fontSize = fontSize)
            if (resource?.name != null) Text(text = "name:\t" + resource.name, fontSize = fontSize)
            Text(text = "scope:\t" + policy.scope!!.scope, fontSize = fontSize)
            Text(text = "policy type:\t" + policy.type, fontSize = fontSize)
        }
    }
}

@Preview
@Composable
fun PolicyCardPreview(
//    repo: PolicyRepository = get()
){
//    val policy = repo.getAllPolicies()[0]
//    PolicyCard(policy = policy)
    Column{

        Text("default")
        Text("test string", fontSize = 20.sp)
    }
}