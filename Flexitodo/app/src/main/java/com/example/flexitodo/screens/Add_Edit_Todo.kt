package com.example.flexitodo.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flexitodo.*
import com.example.flexitodo.R
import com.example.flexitodo.components.dateFormatUK
import com.example.flexitodo.components.longToStringDate
import com.example.flexitodo.components.stringToLongDate
import com.example.flexitodo.database.TodoItem
import com.marosseleng.compose.material3.*
import kotlinx.coroutines.launch
import java.util.*


@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditTodo(navController: NavController, viewModel: DatabaseViewModel, todoId: Long?, newTodoViewModel: NewTodoViewModel) {

    val openD = newTodoViewModel.openDialog.observeAsState()

    Scaffold(
        topBar = { TopAppBarAddNew(navController, todoId, newTodoViewModel, viewModel) },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                if (openD.value == true && todoId is Long){
                    DeleteConfirm(newTodoViewModel, navController, todoId, viewModel)
                }

                ContentAddNew(viewModel, todoId, newTodoViewModel)
            }
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun TopAppBarAddNew(navController: NavController, todoId: Long?, newTodoViewModel: NewTodoViewModel, viewModel: DatabaseViewModel) {
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        title = {
            if (todoId != null) {
                Text("Edit Todo")
            } else {
                Text("Add Todo")
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigate("Todo_List") {
                popUpTo("Todo_List")
            } }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
        actions = {
            Button(onClick = {
                if (todoId is Long) {
                    val item = TodoItem(
                        itemId = todoId,
                        listId = 1L,
                        itemSummary = newTodoViewModel.summary.value.toString(),
                        itemFolder = newTodoViewModel.folder.value.toString(),
                        itemDate = stringToLongDate(newTodoViewModel.datePicked.value.toString()),
                        itemNotes = newTodoViewModel.notes.value)
                    coroutineScope.launch { viewModel.updateItem(item) }

                } else {
                    val item = TodoItem(
                        listId = 1L,
                        itemSummary = newTodoViewModel.summary.value.toString(),
                        itemFolder = newTodoViewModel.folder.value.toString(),
                        itemDate = stringToLongDate(newTodoViewModel.datePicked.value.toString()),
                        itemNotes = newTodoViewModel.notes.value)
                    coroutineScope.launch { viewModel.insertItem(item) }
                }
                navController.navigate("Todo_List"){
                    popUpTo("Todo_List") { inclusive = true }
                }
            }){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_save_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Text("  SAVE")
            }
            if (todoId is Long) {
                Button(onClick = {
                    newTodoViewModel.openDialog.value = true
                })
                {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_delete_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun ContentAddNew(viewModel: DatabaseViewModel, todoId: Long?, newTodoViewModel: NewTodoViewModel) {
    val todoItem: State<TodoItem?>
    val expanded = remember { mutableStateOf(false) }
    val datePickerShown = remember { mutableStateOf(false) }

    val summary = newTodoViewModel.summary.observeAsState(initial = null)
    val folder = newTodoViewModel.folder.observeAsState(initial = "Today")
    val datePicked = newTodoViewModel.datePicked.observeAsState(initial = null)
    val notes = newTodoViewModel.notes.observeAsState(initial = null)

    if (todoId !== null) {
        todoItem = viewModel.getTodoItemDetails(todoId).observeAsState(initial = null)

        if (todoItem.value is TodoItem) {
            newTodoViewModel.summary.value = todoItem.value!!.itemSummary
            newTodoViewModel.folder.value = todoItem.value!!.itemFolder
            newTodoViewModel.datePicked.value = longToStringDate(todoItem.value!!.itemDate)
            newTodoViewModel.notes.value = todoItem.value!!.itemNotes
        }
    }

    Row {
        LazyColumn(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            item(1){
                OutlinedTextField(
                    value = summary.value.toString(),
                    onValueChange = { newTodoViewModel.summary.value = it },
                    label = { Text("Summary") },
                    modifier = Modifier.fillMaxWidth()
                )
            }  //Summary Field
            item(2){
                ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {expanded.value = !expanded.value} ) {
                    TextField(
                        value = folder.value,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = "Folder") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false } ) {
                        DropdownMenuItem(text = { Text("Today") }, onClick = { newTodoViewModel.folder.value = "Today" } )
                        DropdownMenuItem(text = { Text("Tomorrow") }, onClick = { newTodoViewModel.folder.value = "Tomorrow" } )
                        DropdownMenuItem(text = { Text("This Week") }, onClick = { newTodoViewModel.folder.value = "This Week" } )
                        DropdownMenuItem(text = { Text("This Month") }, onClick = { newTodoViewModel.folder.value = "This Month" } )
                        DropdownMenuItem(text = { Text("Sometime") }, onClick = { newTodoViewModel.folder.value = "Sometime" } )
                    }
                }
            }  //Folder Field
            item(3){
                 Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
                     TextField(
                         value = datePicked.value.toString(),
                         onValueChange = { },
                         enabled = false,
                         readOnly = true,
                         label = { Text(text = "Due Date") },
                         trailingIcon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = null) },
                         colors = ExposedDropdownMenuDefaults.textFieldColors(),
                         modifier = Modifier
                             .clickable(onClick = { datePickerShown.value = true })
                             .weight(1f)
                     )
                     Button(
                         onClick = { newTodoViewModel.datePicked.value = "" },
                         shape = CircleShape,
                         modifier = Modifier.size(50.dp),
                         contentPadding = PaddingValues(0.dp)
                     ){
                         Icon(
                             painter = painterResource(id = R.drawable.baseline_event_busy_24),
                             contentDescription = null,
                             tint = MaterialTheme.colorScheme.onPrimary
                         )
                     }
                     if (datePickerShown.value){
                         com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog(
                             onDismissRequest = { datePickerShown.value = false },
                             onDateChange = { localDate ->
                                 newTodoViewModel.datePicked.value = dateFormatUK(localDate)
                                 datePickerShown.value = false
                            },
                             title = { Text("Select a due date:")},
                             locale = Locale("en", "UK")
                         )
                     }

                 }
            }  //Date Field
            item(4){
                OutlinedTextField(
                    value = notes.value.toString(),
                    onValueChange = { newTodoViewModel.notes.value = it },
                    label = { Text("Notes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }  //Notes Field
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun DeleteConfirm(newTodoViewModel: NewTodoViewModel, navController: NavController, todoId: Long, viewModel: DatabaseViewModel) {
    val coroutineScope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = { newTodoViewModel.openDialog.value = false },
        title = {
            Text(text = "Are you sure you want to delete this?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val item = TodoItem(
                        itemId = todoId,
                        listId = 1L,
                        itemSummary = newTodoViewModel.summary.value.toString(),
                        itemFolder = newTodoViewModel.folder.value.toString(),
                        itemDate = stringToLongDate(newTodoViewModel.datePicked.value.toString()),
                        itemNotes = newTodoViewModel.notes.value)

                    coroutineScope.launch { viewModel.delete(item) }

                    newTodoViewModel.openDialog.value = false

                    navController.navigate("Todo_List"  ){
                        popUpTo("Todo_List") { inclusive = true }
                    }
                },
                content = { Text("Yes") }
            )
        },
        dismissButton = {
            TextButton(
                onClick = { newTodoViewModel.openDialog.value = false },
                content = { Text("No") }
            )
        }
    )
}