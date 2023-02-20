package com.example.flexitodo.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.flexitodo.database.DatabaseClass
import com.example.flexitodo.database.TodoList

class AddTodoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AddTodoRepository

    val allTodoLists: LiveData<List<TodoList>>

    init {
        val databaseInterface = DatabaseClass.getInstance(application).databaseInterface
        repository = AddTodoRepository(databaseInterface)
        allTodoLists = repository.allTodoLists
    }
}