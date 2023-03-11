package com.example.flexitodo.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flexitodo.R
import com.example.flexitodo.database.TodoItem
import com.example.flexitodo.screens.DatabaseViewModel
import kotlinx.coroutines.launch

@Composable
fun CollapsibleList(
    title: String,
    todoItems: List<TodoItem>,
    isExpanded: Boolean,
    navController: NavController,
    viewModel: DatabaseViewModel,
    toggleComplete: Boolean
) {
    val expanded = remember { mutableStateOf(isExpanded) }
    val arrow: ImageVector = if (expanded.value) { Icons.Filled.KeyboardArrowUp }
    else { Icons.Filled.KeyboardArrowDown }

    Box(Modifier.padding(10.dp)) {
        Column(Modifier.border(BorderStroke(2.dp, SolidColor(MaterialTheme.colorScheme.primary)))) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable(onClick = {
                        expanded.value = !expanded.value
                    })
                    .padding(8.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Icon(
                    imageVector = arrow,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            ExpandableView(todoItems, expanded.value, navController, viewModel, toggleComplete)
        }
    }
}

@Composable
fun ExpandableView(todoItemsList: List<TodoItem>, isExpanded: Boolean, navController: NavController, viewModel: DatabaseViewModel, toggleComplete: Boolean) {
    val coroutineScope = rememberCoroutineScope()
    var todoItems = todoItemsList

    if (!toggleComplete){
        var todoItemsTemp: List<TodoItem> = emptyList()

        todoItems.forEach{item ->
            if (!item.itemComplete)
                todoItemsTemp = todoItemsTemp + item
            }

        todoItems = todoItemsTemp
    }

    // Opening Animation
    val expandTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeIn(
            animationSpec = tween(300)
        )
    }

    // Closing Animation
    val collapseTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeOut(
            animationSpec = tween(300)
        )
    }

    AnimatedVisibility(
        visible = isExpanded,
        enter = expandTransition,
        exit = collapseTransition
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(15.dp)
        ) {
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Summary",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )

                    Text(
                        text = "Due",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.fillMaxWidth(1f)
                    )
                }
                todoItems.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {
                                val todoId = item.itemId
                                navController.navigate("Add_Todo?todoId=$todoId" )
                            })
                    ) {
                        Text(
                            text = item.itemSummary,
                            fontSize = 14.sp,
                            textDecoration = if (item.itemComplete) TextDecoration.LineThrough else TextDecoration.None,
                            fontWeight = if (item.itemDate is Long && item.itemDate!! <= System.currentTimeMillis()) FontWeight.Bold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onBackground,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        )

                        Text(
                            text = longToStringDate(item.itemDate),
                            fontSize = 14.sp,
                            textDecoration = if (item.itemComplete) TextDecoration.LineThrough else TextDecoration.None,
                            fontWeight = if (item.itemDate is Long && item.itemDate!! <= System.currentTimeMillis()) FontWeight.Bold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        )

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(0.dp)
                        ){
                            if (item.itemNotes != "" && item.itemNotes != null) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_insert_drive_file_24),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                )
                            }
                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Checkbox(
                                checked = item.itemComplete,
                                onCheckedChange = {
                                    coroutineScope.launch { viewModel.changeComplete(item.itemId) }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}