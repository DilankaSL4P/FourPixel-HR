package com.fourpixel.fourpixelhrapplication

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.fourpixel.fourpixelhrapplication.DashBoardSection.DashboardView
import com.fourpixel.fourpixelhrapplication.HR.LeavesScreen
import com.fourpixel.fourpixelhrapplication.LoginSection.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            println("DEBUG: Navigated to LoginScreen")
            LoginScreen(navController)
        }

        composable(
            "dashboard/{userName}/{userImageUrl}/{userRole}",
            arguments = listOf(
                navArgument("userName") { type = NavType.StringType },
                navArgument("userImageUrl") { type = NavType.StringType },
                navArgument("userRole") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "User"
            val userImageUrl = backStackEntry.arguments?.getString("userImageUrl") ?: ""
            val userRole = backStackEntry.arguments?.getString("userRole") ?: ""
            DashboardView(navController, userName, userImageUrl, userRole)
        }

        composable("leaves") {
            LeavesScreen()
        }
    }
}
