package com.example.allgauth.ui


import android.app.Application
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.allgauth.AuthApi
import com.example.allgauth.DashboardViewModel
import com.example.allgauth.DashboardViewModelFactory

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Dashboard : Screen("dashboard")
}


@Composable
fun MainScreen(
    api: AuthApi,
    onGoogleLogin: () -> Unit,
    jwtToken: String? // ✅ use passed token instead of reading prefs
) {
    val navController = rememberNavController()

    // Navigate to Dashboard only if jwtToken exists
    LaunchedEffect(jwtToken) {
        if (!jwtToken.isNullOrEmpty()) {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Home.route) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(onLoginClick = onGoogleLogin, navController = navController)
        }

        composable(Screen.Dashboard.route) {
            val context = LocalContext.current
            val dashboardViewModel: DashboardViewModel = viewModel(
                factory = DashboardViewModelFactory(
                    application = context.applicationContext as Application,
                    api = api,
                    jwtToken = jwtToken ?: ""
                )
            )

            DashboardScreen(
                navController = navController,
                viewModel = dashboardViewModel,
                onLogout = {
                    dashboardViewModel.logout()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}
