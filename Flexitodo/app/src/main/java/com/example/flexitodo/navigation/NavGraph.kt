@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.flexitodo.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flexitodo.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph (navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = "Todo_List")
    {
        composable(route = "Todo_List"){ TodoList(navController, viewModel(), viewModel()) }
        composable(
            route = "Add_Todo?todoId={todoId}",
            arguments = listOf(navArgument("todoId") {
                defaultValue = null
                nullable = true
            })) {
            backStackEntry ->
            val temp = backStackEntry.arguments?.getString("todoId")
            var todoId: Long? = null
            if (temp != null) { todoId = temp.toLong() }

            AddEditTodo(navController, viewModel(), todoId, viewModel())
        }
        composable(route = "Settings"){ Settings(navController) }
    }
}