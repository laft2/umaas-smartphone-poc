package com.example.uma_authz_smartphone.ui.authz

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.uma_authz_smartphone.R
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
fun AuthzScreen(
    viewModel: AuthzViewModel = koinViewModel(),
    modifier: Modifier = Modifier
){
    QSUriField(viewModel)
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