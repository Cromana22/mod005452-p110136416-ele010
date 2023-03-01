@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.flexitodo.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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

@ExperimentalMaterial3Api
@Composable
fun TodoList(navController: NavController, viewModel: DatabaseViewModel) {
    Scaffold(
        topBar = { TopAppBarTodoList(navController, viewModel) },
        content = { paddingValues ->
            ContentTodoList(viewModel, paddingValues, navController)
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
            IconButton(onClick = { navController.navigate("Settings") }) {
                Icon(Icons.Filled.Settings, null)
            }
            IconButton(onClick = { navController.navigate("Add_Todo") }) {
                Icon(Icons.Filled.Add, null)
            }
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun ContentTodoList(viewModel: DatabaseViewModel, paddingValues: PaddingValues, navController: NavController) {
    val folders: State<List<String>> = viewModel.getTodoFolders(1L).observeAsState(initial = emptyList())
    val folderOrder = listOf("Today",  "Tomorrow", "This Week", "This Month", "Sometime")

    LazyColumn(modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()) {
        if (folders.value.isEmpty()){
           item(1){
               Box(modifier = Modifier.fillMaxSize()){
                   Button(onClick = { navController.navigate("Add_Todo") }) {
                       Text("Click to add your first todo!")
                   }
               }
           }
       }

        else {
            item(2){
                for (folderName in folderOrder) {
                    folders.value.forEach { folder ->
                        if (folder == folderName){
                            val items: State<List<TodoItem>> = viewModel.getTodoItems(key = 1L, folder = folder)
                                .observeAsState(initial = emptyList())

                            Row { CollapsibleList(folder, items.value, true, navController, viewModel) }
                        }
                    }
                }
            }
        }
    }
}