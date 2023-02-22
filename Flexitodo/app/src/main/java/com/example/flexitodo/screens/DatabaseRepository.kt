package com.example.flexitodo.screens

import androidx.lifecycle.LiveData
import com.example.flexitodo.database.DatabaseInterface
import com.example.flexitodo.database.TodoList

class DatabaseRepository(private val databaseInterface: DatabaseInterface) {
    val allTodoLists: LiveData<List<TodoList>> = databaseInterface.getTodoLists()
}