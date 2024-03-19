package com.example.uma_authz_smartphone.ui.manage

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uma_authz_smartphone.db.model.DbPolicy
import org.koin.androidx.compose.koinViewModel

@Composable
fun ManageScreen(
    viewModel: ManageViewModel = koinViewModel(),
    modifier: Modifier = Modifier
){

}

@Composable
fun ManageCard(
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