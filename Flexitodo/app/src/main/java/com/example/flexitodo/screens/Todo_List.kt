@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.flexitodo.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.flexitodo.*
import com.example.flexitodo.components.CollapsibleList
import com.example.flexitodo.database.TodoItem
import com.example.flexitodo.database.TodoList
import com.example.flexitodo.navigation.Screens

@ExperimentalMaterial3Api
@Composable
fun TodoList(navController: NavController, viewModel: DatabaseViewModel) {
    Scaffold(
        topBar = { TopAppBarTodoList(navController, viewModel) },
        content = { paddingValues ->
            ContentTodoList(viewModel, paddingValues)
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun TopAppBarTodoList(navController: NavController, viewModel: DatabaseViewModel) {
    val todoListsState: State<List<TodoList>> = viewModel.allTodoLists().observeAsState(initial = emptyList())

    TopAppBar(
        title = {
            if (todoListsState.value.isEmpty()){ Text("Test") }
            else { Text(todoListsState.value[0].listName) }
        },
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
fun ContentTodoList(viewModel: DatabaseViewModel, paddingValues: PaddingValues) {
   val folders: State<List<String>> = viewModel.getTodoFolders(1L).observeAsState(initial = emptyList())

   LazyColumn(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
       if (folders.value.isEmpty()){ items(1){Text("Test") }}
       else {
           items(count = folders.value.size){
               folders.value.forEach { folder ->
                   val items: State<List<TodoItem>> = viewModel.getTodoItems(key = 1L, folder = folder)
                       .observeAsState(initial = emptyList())

                   Row{ CollapsibleList(folder, items.value, true) }
               }
           }
       }
   }
}