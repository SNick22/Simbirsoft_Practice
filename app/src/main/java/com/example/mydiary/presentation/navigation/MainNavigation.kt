package com.example.mydiary.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mydiary.presentation.screen.new_task.NewTaskScreen
import com.example.mydiary.presentation.screen.task_details.TaskDetailsScreen
import com.example.mydiary.presentation.screen.task_list.ToDoListScreen

sealed class Screen(
    val route: String
) {

    object TaskList : Screen(
        route = "task_list"
    )

    object TaskDetails : Screen(
        route = "task_details/{task}"
    )

    object NewTask : Screen(
        route = "new_task"
    )
}

@Composable
fun MyAppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: Screen = Screen.TaskList
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        composable(Screen.TaskList.route) { ToDoListScreen(navController) }
        composable(
            Screen.TaskDetails.route,
            arguments = listOf(navArgument("task") {
                type = NavType.IntType
            })
        ) {
            val taskId = it.arguments?.getInt("task") ?: 1
            TaskDetailsScreen(taskId, navController)
        }
        composable(Screen.NewTask.route) { NewTaskScreen(navController) }
    }
}
