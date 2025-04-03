package example.com.fourpixelhrapplication

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import example.com.fourpixelhrapplication.DashBoardSection.DashboardView
import example.com.fourpixelhrapplication.LoginSection.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            println("DEBUG: Navigated to LoginScreen")
            LoginScreen(navController)
        }

        composable(
            "dashboard/{userName}",
            arguments = listOf(navArgument("userName") { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "User"
            DashboardView(navController, userName)
        }
    }
}

