package com.example.flexitodo.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.flexitodo.database.DatabaseClass
import com.example.flexitodo.database.TodoItem
import com.example.flexitodo.database.TodoList

class DatabaseViewModel(application: Application) : AndroidViewModel(application) {
    private val databaseInterface = DatabaseClass.getInstance(application).databaseInterface
    private val repository: DatabaseRepository = DatabaseRepository(databaseInterface)

    fun allTodoLists(): LiveData<List<TodoList>> {
        return repository.allTodoLists
    }

    fun getTodoFolders(key: Long): LiveData<List<String>> {
        return repository.getTodoFolders(key)
    }

    fun getTodoItems(key: Long, folder: String): LiveData<List<TodoItem>> {
        return repository.getTodoItems(key, folder)
    }

    fun getTodoItemDetails(key: Long): LiveData<TodoItem?> {
        return databaseInterface.getTodoItemDetails(key)
    }
}