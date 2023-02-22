@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.flexitodo.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flexitodo.screens.*

@Composable
fun NavGraph (navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screens.TodoList.route)
    {
        composable(route = Screens.TodoList.route){ TodoList(navController) }
        composable(route = Screens.AddNew.route){ AddTodo(navController, viewModel()) }
        composable(route = Screens.Settings.route){ Settings(navController) }
    }
}