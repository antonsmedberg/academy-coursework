package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.login

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class LoginDest(val route: String) {
    data object Login : LoginDest("login")
    data object Register : LoginDest("register")
}

@Composable
fun LoginNavRoot(onLoggedIn: () -> Unit) {
    val nav = rememberNavController()
    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = nav, startDestination = LoginDest.Login.route) {
            composable(LoginDest.Login.route) {
                LoginScreen(
                    onLoginSuccess = onLoggedIn,
                    onGoRegister = { nav.navigate(LoginDest.Register.route) }
                )
            }
            composable(LoginDest.Register.route) {
                RegisterScreen(onRegistered = { nav.popBackStack() })
            }
        }
    }
}