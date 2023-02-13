@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.flexitodo.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flexitodo.screens.AddTodo
import com.example.flexitodo.screens.TodoList
import com.example.flexitodo.screens.Settings

@Composable
fun NavGraph (navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screens.TodoList.route)
    {
        composable(route = Screens.TodoList.route){ TodoList(navController) }
        composable(route = Screens.AddNew.route){ AddTodo(navController) }
        composable(route = Screens.Settings.route){ Settings(navController) }
    }
}