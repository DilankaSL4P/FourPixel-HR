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
import com.fourpixel.fourpixelhrapplication.Work.AddNewTaskScreen
import com.fourpixel.fourpixelhrapplication.Work.NoticeBoardScreen
import com.fourpixel.fourpixelhrapplication.Work.ProjectListScreen
import com.fourpixel.fourpixelhrapplication.Work.TaskDetailScreen
import com.fourpixel.fourpixelhrapplication.Work.TaskListScreen

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

        composable("tasks") {
            TaskListScreen(navController)
        }

        composable("projects") {
            ProjectListScreen(navController)
        }

        composable("noticeboard") {
            NoticeBoardScreen(navController)
        }
        composable(
            "taskDetail/{taskId}/{heading}/{status}/{dueDate}/{assignedUser}",
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType },
                navArgument("heading") { type = NavType.StringType },
                navArgument("status") { type = NavType.StringType },
                navArgument("dueDate") { type = NavType.StringType },
                navArgument("assignedUser") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
            val heading = backStackEntry.arguments?.getString("heading") ?: ""
            val status = backStackEntry.arguments?.getString("status") ?: ""
            val dueDate = backStackEntry.arguments?.getString("dueDate") ?: ""
            val assignedUser = backStackEntry.arguments?.getString("assignedUser") ?: ""

            TaskDetailScreen(taskId, heading, status, dueDate, assignedUser, navController)
        }

    }
}
