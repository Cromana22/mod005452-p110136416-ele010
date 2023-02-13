package com.example.flexitodo.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.flexitodo.*
import com.example.flexitodo.navigation.Screens

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Settings(navController: NavController) {
    Scaffold(
        topBar = { TopAppBarSettings(navController) },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                ContentSettings()
            }
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun TopAppBarSettings(navController: NavController) {
    TopAppBar(
        title = { Text("Settings") },
        navigationIcon = {
            IconButton(onClick = { navController.navigate(Screens.TodoList.route) {
                popUpTo(Screens.TodoList.route)
            } }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun ContentSettings() {
    Row {
        Text("This is some content")
    }
}