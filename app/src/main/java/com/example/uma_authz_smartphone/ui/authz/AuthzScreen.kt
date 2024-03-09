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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uma_authz_smartphone.R
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
    val qsUri by viewModel.qsUri.collectAsState(initial = "")

    OutlinedTextField(
        value = qsUri,
        onValueChange = { viewModel.onQsUriFieldChange(it) },
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
    // TODO: implement, for test below
    AuthorizedRequestCard(textString = "for test")
}

@Composable
fun AuthorizedRequestCard(textString: String){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth(0.9f),
    ) {
        Text(text = textString)
    }
}