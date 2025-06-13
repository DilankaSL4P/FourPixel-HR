package com.fourpixel.fourpixelhrapplication

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.fourpixel.fourpixelhrapplication.DashBoardSection.DashboardView
import com.fourpixel.fourpixelhrapplication.HR.ApplyLeavesScreen
import com.fourpixel.fourpixelhrapplication.HR.LeavesScreen
import com.fourpixel.fourpixelhrapplication.LoginSection.LoginScreen
import com.fourpixel.fourpixelhrapplication.Work.AddNewTaskScreen
import com.fourpixel.fourpixelhrapplication.Work.NoticeBoardScreen
import com.fourpixel.fourpixelhrapplication.Work.ProjectListScreen
import com.fourpixel.fourpixelhrapplication.Work.TaskDetailScreen
import com.fourpixel.fourpixelhrapplication.Work.TaskListScreen
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import com.fourpixel.fourpixelhrapplication.HR.MonthlyAttendanceScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberAnimatedNavController() // use this instead of rememberNavController

    AnimatedNavHost(
        navController = navController,
        startDestination = "login",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500) // 500ms for slower transition
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        }
    ) {

        composable("login") {
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

        // Repeat for all other routes
        composable("leaves") {
            LeavesScreen(navController)
        }

        composable("tasks") {
            TaskListScreen(navController)
        }

        composable("projects") {
            ProjectListScreen(navController)
        }

        composable("applyLeave") {
            ApplyLeavesScreen(navController)
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
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val imageUrl = backStackEntry.arguments?.getString("imageUrl")
            val heading = backStackEntry.arguments?.getString("heading") ?: ""
            val startDate = backStackEntry.arguments?.getString("startDate") ?: ""
            val status = backStackEntry.arguments?.getString("status") ?: ""
            val dueDate = backStackEntry.arguments?.getString("dueDate") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            val assignedUser = backStackEntry.arguments?.getString("assignedUser") ?: ""
            TaskDetailScreen(taskId,name, imageUrl, startDate,heading, status, dueDate, assignedUser,description, navController)
        }

        composable("addTask") {
            AddNewTaskScreen(navController)
        }
        composable("attendance") {
            MonthlyAttendanceScreen(navController = navController)
        }
    }
}