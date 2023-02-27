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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flexitodo.database.TodoItem

@Composable
fun CollapsibleList(title: String, todoItems: List<TodoItem>, isExpanded: Boolean) {
    val expanded = remember { mutableStateOf(isExpanded) }
    val arrow: ImageVector = if (expanded.value) {
        Icons.Filled.KeyboardArrowUp
    }
    else {
        Icons.Filled.KeyboardArrowDown
    }

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
            ExpandableView(todoItems = todoItems, isExpanded = expanded.value)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CollapsibleListPreview() {
    val todoItem1 = TodoItem(listId = 1L, itemSummary = "Todo1")
    val todoItem2 = TodoItem(listId = 1L, itemSummary = "Todo2")
    val todoItem3 = TodoItem(listId = 1L, itemSummary = "Todo3")
    val items = listOf(todoItem1, todoItem2, todoItem3)
    CollapsibleList("Sometime", todoItems = items, true)
}

@Composable
fun ExpandableView(todoItems: List<TodoItem>, isExpanded: Boolean) {
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
            Column{
                todoItems.forEach { item ->
                    Text(text = item.itemSummary,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ExpandableViewPreview() {
    val todoItem1 = TodoItem(listId = 1L, itemSummary = "Todo1")
    val todoItem2 = TodoItem(listId = 1L, itemSummary = "Todo2")
    val todoItem3 = TodoItem(listId = 1L, itemSummary = "Todo3")
    val items = listOf(todoItem1, todoItem2, todoItem3)
    ExpandableView(items, true)
}