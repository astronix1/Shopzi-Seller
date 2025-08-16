package com.example.shopziseller

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    GlobalNavigation.navController = navController

    val isLoggedIn = Firebase.auth.currentUser != null
    val std = if(isLoggedIn) "homescreen" else "login"
    NavHost(navController = navController, startDestination = std) {
        composable("login") {
            LoginPage(modifier)
        }

        composable("homescreen") {
            HomeScreen(modifier)
        }
    }
}

object GlobalNavigation{
    lateinit var navController: NavHostController
}