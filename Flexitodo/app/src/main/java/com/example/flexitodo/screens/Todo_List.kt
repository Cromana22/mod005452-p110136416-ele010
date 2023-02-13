@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.flexitodo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.flexitodo.*
import com.example.flexitodo.navigation.Screens

@ExperimentalMaterial3Api
@Composable
fun TodoList(navController: NavController) {
    Scaffold(
        topBar = { TopAppBarTodoList(navController) },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                ContentTodoList()
            }
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun TopAppBarTodoList(navController: NavController) {
    TopAppBar(
        title = { Text("This is a title") },
        actions = {
            IconButton(onClick = {/* Do Something*/ }) {
                Icon(Icons.Filled.Notifications, null)
            }
            IconButton(onClick = { navController.navigate(Screens.Settings.route) }) {
                Icon(Icons.Filled.Settings, null)
            }
            IconButton(onClick = { navController.navigate(Screens.AddNew.route) }) {
                Icon(Icons.Filled.Add, null)
            }
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun ContentTodoList() {
    Row {
        Text("This is some content.")
    }
}