package com.example.flexitodo.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.flexitodo.*
import com.example.flexitodo.navigation.Screens
import java.time.LocalDateTime

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddTodo(navController: NavController) {
    val viewModel: AddTodoViewModel = viewModel()

    Scaffold(
        topBar = { TopAppBarAddNew(navController) },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                ContentAddNew(viewModel)
            }
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun TopAppBarAddNew(navController: NavController) {
    TopAppBar(
        title = { Text("Add New Todo") },
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
fun ContentAddNew(viewModel: AddTodoViewModel) {
    val todoListsState = viewModel.allTodoLists.observeAsState()
    val numTodoLists = todoListsState.value?.size

    Row {
        LazyColumn {
            item{
                Text("This is some content.")
            }

            if (numTodoLists != null) {
                items(numTodoLists) { todoListsState.value!!.forEach {
                    todoList -> Text("${todoList.listName}")
                } }
            }

            item{
                Button(
                    onClick = {
                        val dt = LocalDateTime.now()
                        Log.i("Save", "Someone saved something at $dt.") },
                    content = { Text("Log Something")}
                )
            }

            item{
                Button(
                    onClick = {
                        Log.i("Save", "SOMETHING") },
                    content = { Text("Create a todo list")}
                )
            }
        }
    }
}