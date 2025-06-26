package com.fourpixel.fourpixelhrapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.fourpixel.fourpixelhrapplication.dashboardsection.DashboardView
import com.fourpixel.fourpixelhrapplication.hr.ApplyLeavesScreen
import com.fourpixel.fourpixelhrapplication.hr.LeavesScreen
import com.fourpixel.fourpixelhrapplication.loginsection.LoginScreen
import com.fourpixel.fourpixelhrapplication.work.AddNewTaskScreen
import com.fourpixel.fourpixelhrapplication.work.NoticeBoardScreen
import com.fourpixel.fourpixelhrapplication.work.ProjectListScreen
import com.fourpixel.fourpixelhrapplication.work.TaskDetailScreen
import com.fourpixel.fourpixelhrapplication.work.TaskListScreen
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import com.fourpixel.fourpixelhrapplication.hr.MonthlyAttendanceScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import java.net.URLDecoder

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
            val userName = URLDecoder.decode(backStackEntry.arguments?.getString("userName") ?: "User", "UTF-8")
            val userImageUrl = backStackEntry.arguments?.getString("userImageUrl") ?: ""
            val userRole = backStackEntry.arguments?.getString("userRole") ?: ""
            DashboardView(navController, userName, userImageUrl, userRole)
        }

        composable("leaves") {
            LeavesScreen(navController)
        }

        composable("tasks") {
            TaskListScreen(navController)
        }

        composable(
            "projects/{userName}",
            arguments = listOf(navArgument("userName") { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            ProjectListScreen(navController, userName)
        }


        composable(
            "applyLeave/{token}/{userId}",
            arguments = listOf(
                navArgument("token") { type = NavType.StringType },
                navArgument("userId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            ApplyLeavesScreen(token, userId, navController)
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