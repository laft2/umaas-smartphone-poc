package com.example.uma_authz_smartphone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uma_authz_smartphone.ui.authz.AuthzScreen
import com.example.uma_authz_smartphone.ui.manage.ManageScreen
import com.example.uma_authz_smartphone.ui.policy.PolicyScreen
import com.example.uma_authz_smartphone.ui.theme.Uma_authz_smartphoneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Uma_authz_smartphoneTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                val items: List<String> = listOf("authorize", "manage", "policy")

                
                Scaffold(
                    bottomBar = {BottomNavigationBar(items = items, navController = navController)}
                ) {
                    val modifier = Modifier.padding(it)
                    NavHost(navController, startDestination = "authorize"){
                        composable("authorize"){
                            AuthzScreen(modifier = modifier)
                        }
                        composable("manage"){
                            ManageScreen(modifier = modifier)
                        }
                        composable("policy"){
                            PolicyScreen(modifier = modifier)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<String>,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier,
    ) {
        var selectedItem by remember { mutableStateOf(0) }
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = item) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item)
                }
            )
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Uma_authz_smartphoneTheme {
        Greeting("Android")
    }
}