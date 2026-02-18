package com.example.allgauth.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.allgauth.AuthViewModel
import com.example.allgauth.api.ApiClient

object Routes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
}


@Composable
fun MainNavGraph(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    // ✅ create once (not on every recomposition)
    val api = remember {
        ApiClient.create("http://192.168.29.139:3000/") // MUST end with /
    }

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                vm = authViewModel,
                api = api,
                onLoggedIn = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                vm = authViewModel,
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                }
            )
        }
    }
}
