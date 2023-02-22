package com.example.flexitodo.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.flexitodo.database.DatabaseClass
import com.example.flexitodo.database.TodoList

class DatabaseViewModel(application: Application) : AndroidViewModel(application) {
    private val databaseInterface = DatabaseClass.getInstance(application).databaseInterface
    private val repository: DatabaseRepository = DatabaseRepository(databaseInterface)

    fun allTodoLists(): LiveData<List<TodoList>> {
        return repository.allTodoLists
    }
}