package com.example.uma_authz_smartphone.ui.authz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uma_authz_smartphone.R
import com.example.uma_authz_smartphone.data.model.AuthorizationLog
import com.example.uma_authz_smartphone.datasource.toAuthorizationLog
import kotlinx.coroutines.flow.collect
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
fun AuthzScreen(
    viewModel: AuthzViewModel = koinViewModel(),
    modifier: Modifier = Modifier.padding(32.dp)
){
    Column {
        QSUriField(viewModel)
        ObtainRequestsButton(viewModel)
        Text(text = "Logs:")
        AuthorizedRequestsCards(viewModel)
    }
}

@Composable
fun QSUriField(viewModel: AuthzViewModel){
    var qsUri by rememberSaveable{ mutableStateOf(viewModel.getQsUriPreference())}

    OutlinedTextField(
        value = qsUri,
        onValueChange = {
            qsUri = it
            viewModel.editQsUriPreference(it)
                        },
        label = { Text(stringResource(R.string.queuing_server)) },
    )
}

@Composable
fun ObtainRequestsButton(viewModel: AuthzViewModel){
    Button(
        onClick = {viewModel.authorizeRequestsFromQS()}
    ){
        Text(text = "Obtain requests from QS")
    }
}

@Composable
fun AuthorizedRequestsCards(viewModel: AuthzViewModel){
    val authorizationLogs by viewModel.authorizationLogs.collectAsStateWithLifecycle()
    for (log in authorizationLogs){
        AuthorizationLogCard(log.toAuthorizationLog())
    }
}

@Composable
fun AuthorizationLogCard(authorizationLog: AuthorizationLog){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth(0.9f),
    ) {
        Text(text = "${authorizationLog.timestamp}")
        Text("approved: ${authorizationLog.isApproved}")
        Text("target resources: ${authorizationLog.resourceIds}")
        Text("by ${authorizationLog.clientInfo}")
    }
}