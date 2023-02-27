package com.example.flexitodo.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flexitodo.*
import com.example.flexitodo.R
import com.example.flexitodo.components.LongToStringDate
import com.example.flexitodo.components.DateFormatUK
import com.example.flexitodo.database.TodoItem
import com.marosseleng.compose.material3.*
import java.util.*

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddTodo(navController: NavController, viewModel: DatabaseViewModel, todoId: Long?) {
    Scaffold(
        topBar = { TopAppBarAddNew(navController, todoId) },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                ContentAddNew(viewModel, todoId)
            }
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun TopAppBarAddNew(navController: NavController, todoId: Long?) {
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
            Button(onClick = { navController.navigate("Todo_List") }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_save_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Text("  SAVE")
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun ContentAddNew(viewModel: DatabaseViewModel, todoId: Long?) {
    val todoItem: State<TodoItem?>
    val expanded = remember { mutableStateOf(false) }
    val datePickerShown = remember { mutableStateOf(false) }
    val summary = rememberSaveable { mutableStateOf("") }
    val folder = rememberSaveable { mutableStateOf("Today") }
    val datePicked = rememberSaveable { mutableStateOf("") }
    val notes = rememberSaveable { mutableStateOf("") }

    if (todoId !== null) {
        todoItem = viewModel.getTodoItemDetails(todoId).observeAsState(initial = null)

        if (todoItem.value is TodoItem) {
            summary.value = todoItem.value!!.itemSummary
            folder.value = todoItem.value!!.itemFolder
            datePicked.value = LongToStringDate(todoItem.value!!.itemDate)
            notes.value = todoItem.value!!.itemNotes
        }
    }

    Row {
        LazyColumn(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            item(1){
                OutlinedTextField(
                    value = summary.value,
                    onValueChange = { summary.value = it },
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
                        DropdownMenuItem(text = { Text("Today") }, onClick = { folder.value = "Today" } )
                        DropdownMenuItem(text = { Text("Tomorrow") }, onClick = { folder.value = "Tomorrow" } )
                        DropdownMenuItem(text = { Text("This Week") }, onClick = { folder.value = "This Week" } )
                        DropdownMenuItem(text = { Text("This Month") }, onClick = { folder.value = "This Month" } )
                        DropdownMenuItem(text = { Text("Sometime") }, onClick = { folder.value = "Sometime" } )
                    }
                }
            }  //Folder Field
            item(3){
                 Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
                     TextField(
                         value = datePicked.value,
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
                         onClick = { datePicked.value = "" },
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
                                 datePicked.value = DateFormatUK(localDate)
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
                    value = notes.value,
                    onValueChange = { notes.value = it },
                    label = { Text("Notes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }  //Notes Field
        }
    }
}
