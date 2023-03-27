package com.example.flexitodo.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.example.flexitodo.*
import com.example.flexitodo.R
import com.example.flexitodo.components.dateFormatUK
import com.example.flexitodo.components.longToStringDate
import com.example.flexitodo.components.longToStringDateApi
import com.example.flexitodo.components.stringToLongDate
import com.example.flexitodo.database.TodoItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.*
import com.marosseleng.compose.material3.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*
import okhttp3.*
import java.io.IOException
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

                ContentAddEdit(viewModel, todoId, newTodoViewModel)
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
                Box(modifier = Modifier.width(5.dp)) //spacer between buttons.
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

@SuppressLint("MissingPermission")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class)
@ExperimentalMaterial3Api
@Composable
fun ContentAddEdit(viewModel: DatabaseViewModel, todoId: Long?, newTodoViewModel: NewTodoViewModel) {
    val todoItem: State<TodoItem?>
    val expanded = remember { mutableStateOf(false) }
    val datePickerShown = remember { mutableStateOf(false) }
    val isDateSet = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val summary = newTodoViewModel.summary.observeAsState(initial = null)
    val folder = newTodoViewModel.folder.observeAsState(initial = "Today")
    val datePicked = newTodoViewModel.datePicked.observeAsState(initial = null)
    val notes = newTodoViewModel.notes.observeAsState(initial = null)

    val location = remember { mutableStateOf<Location?>(null) }
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val apiKey = "LMRPVJN73ZGNK8BSMX6MGJ3F7"
    val apiURL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"+
            location.value?.latitude + "," + location.value?.longitude+"/"+
            longToStringDateApi(stringToLongDate(datePicked.value.toString())) +"?key=" + apiKey

    if (todoId !== null) {
        todoItem = viewModel.getTodoItemDetails(todoId).observeAsState(initial = null)

        if (todoItem.value is TodoItem) {
            newTodoViewModel.summary.value = todoItem.value!!.itemSummary
            newTodoViewModel.folder.value = todoItem.value!!.itemFolder
            newTodoViewModel.datePicked.value = longToStringDate(todoItem.value!!.itemDate)
            newTodoViewModel.notes.value = todoItem.value!!.itemNotes

            if (newTodoViewModel.datePicked.value !== null && newTodoViewModel.datePicked.value !== "") {
                isDateSet.value = true
            }
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
                         onClick = {
                             newTodoViewModel.datePicked.value = ""
                             isDateSet.value = false },
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
                                 isDateSet.value = true
                            },
                             title = { Text("Select a due date:")},
                             locale = Locale("en", "UK")
                         )
                     }

                 }
            }  //Date Field
            item(4){
                if (todoId is Long){
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()){
                        Button(
                            onClick = {
                                val date = stringToLongDate(newTodoViewModel.datePicked.value.toString())
                                val intent = Intent(Intent.ACTION_INSERT)
                                    .setData(CalendarContract.Events.CONTENT_URI)
                                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date)
                                    .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                                    .putExtra(CalendarContract.Events.TITLE, newTodoViewModel.summary.value.toString())
                                    .putExtra(CalendarContract.Events.DESCRIPTION, newTodoViewModel.notes.value.toString())
                                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                                startActivity(context, intent, null)
                            },
                            modifier = Modifier.fillMaxWidth(0.8f),
                            shape = RectangleShape,
                            enabled = isDateSet.value)
                        {
                            Text("Add Reminder To Calendar")
                        }
                    }
                }
            }  //Add to Calendar Button
            item(5){
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()){
                    Button(
                        onClick = {
                            if (permissionState.hasPermission) {

                                fusedLocationClient.lastLocation.addOnSuccessListener { locationResponse: Location? ->
                                    if (locationResponse != null) {
                                        location.value = locationResponse
                                        apiCall(apiURL, newTodoViewModel)
                                    }
                                    else {
                                        Toast.makeText(context, "Location request unsuccessful. Please try again.", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                // Request location permission
                                ActivityCompat.requestPermissions(
                                    context as Activity,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    1
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        shape = RectangleShape,
                        enabled = isDateSet.value)
                    {
                        Text("Add expected weather to Notes.")
                    }
                }
            }  //Get Weather Button
            item(6){
                OutlinedTextField(
                    value = notes.value.toString(),
                    onValueChange = { newTodoViewModel.notes.value = it },
                    label = { Text("Notes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }  //Notes Field
            item(7){
                Column{
                    Row{
                        Text("Attachments (not implemented)")
                    }
                    Box{
                        Button(
                            onClick = {  },
                            shape = CircleShape,
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.TopEnd)
                                .offset((-5).dp, (-25).dp)
                                .zIndex(1f),
                            contentPadding = PaddingValues(0.dp)
                        ){
                            Icon(Icons.Filled.Add, null, tint = MaterialTheme.colorScheme.onPrimary)
                        }

                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 200.dp)
                                .heightIn(max = 1000.dp),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 100.dp),
                                modifier = Modifier
                                    .padding(
                                        top = 10.dp,
                                        bottom = 25.dp,
                                        start = 10.dp,
                                        end = 10.dp
                                    )
                                    .offset(y = 15.dp)
                                    .align(Alignment.CenterHorizontally)
                            ){
                                items(6){
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_insert_drive_file_24),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .defaultMinSize(100.dp, 100.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }  //Attachments Field
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

fun apiCall(apiURL: String, newTodoViewModel: NewTodoViewModel) {
    val client = OkHttpClient()
    val request = Request.Builder().url(apiURL).build()
    Log.i("JEN", apiURL)

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("WEATHER", "Api Call Failed with error $e")
        }

        override fun onResponse(call: Call, response: Response) {
            val json = response.body?.string() ?: ""
            val weather = parseJson(json)

            if (newTodoViewModel.notes.value === null || newTodoViewModel.notes.value === "" ) {
                newTodoViewModel.notes.postValue("The weather on " + newTodoViewModel.datePicked.value +
                        " is expected to be: " + weather + ".")
            }
            else {
                newTodoViewModel.notes.postValue(newTodoViewModel.notes.value + "\nThe weather on " +
                        newTodoViewModel.datePicked.value + " is expected to be: " +
                        weather + ".")
            }
        }
    })
}

fun parseJson(json: String): String {
    val root = Json.parseToJsonElement(json)
    var weather = "Unknown"
    val conditions = root.jsonObject["days"]?.jsonArray?.map {
        it.jsonObject["conditions"]?.jsonPrimitive?.contentOrNull
    }

    if (conditions != null && conditions.isNotEmpty()) {
        weather = conditions[0].toString()
    }

    return weather
}