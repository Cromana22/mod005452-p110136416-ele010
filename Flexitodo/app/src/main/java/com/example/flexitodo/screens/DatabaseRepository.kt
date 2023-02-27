package com.example.flexitodo.screens

import androidx.lifecycle.LiveData
import com.example.flexitodo.database.DatabaseInterface
import com.example.flexitodo.database.TodoItem
import com.example.flexitodo.database.TodoList

class DatabaseRepository(private val databaseInterface: DatabaseInterface) {
    val allTodoLists: LiveData<List<TodoList>> = databaseInterface.getTodoLists()

    fun getTodoFolders(key: Long): LiveData<List<String>> {
        return databaseInterface.getTodoFolders(key)
    }

    fun getTodoItems(key: Long, folder: String): LiveData<List<TodoItem>> {
        return databaseInterface.getTodoItems(key, folder)
    }

    fun getTodoItemDetails(key: Long): LiveData<TodoItem?> {
        return databaseInterface.getTodoItemDetails(key)
    }
}