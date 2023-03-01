package com.example.flexitodo.screens

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.flexitodo.database.DatabaseInterface
import com.example.flexitodo.database.TodoItem
import com.example.flexitodo.database.TodoItemAttachment
import com.example.flexitodo.database.TodoList

class DatabaseRepository(private val databaseInterface: DatabaseInterface) {
    suspend fun insertList(list: TodoList){
        databaseInterface.insertList(list)
    }

    suspend fun insertItem(item: TodoItem){
        databaseInterface.insertItem(item)
    }

    suspend fun insertAttachment(attachment: TodoItemAttachment){
        databaseInterface.insertAttachment(attachment)
    }

    suspend fun updateList(list: TodoList){
        databaseInterface.updateList(list)
    }

    suspend fun updateItem(item: TodoItem){
        databaseInterface.updateItem(item)
    }

    suspend fun delete(list: TodoList){
        databaseInterface.delete(list)
    }

    suspend fun delete(item: TodoItem){
        databaseInterface.delete(item)
    }

    suspend fun delete(attachment: TodoItemAttachment){
        databaseInterface.delete(attachment)
    }

    val allTodoLists: LiveData<List<TodoList>> = databaseInterface.getTodoLists()

    fun getTodoItems(key: Long, folder: String): LiveData<List<TodoItem>> {
        return databaseInterface.getTodoItems(key, folder)
    }

    fun getTodoFolders(key: Long): LiveData<List<String>> {
        return databaseInterface.getTodoFolders(key)
    }

    fun getTodoItemDetails(key: Long): LiveData<TodoItem?> {
        return databaseInterface.getTodoItemDetails(key)
    }

    fun getTodoItemAttachments(key: Long): LiveData<List<TodoItemAttachment>> {
        return databaseInterface.getTodoItemAttachments(key)
    }

    fun getAttachment(key: Long): TodoItemAttachment? {
        return databaseInterface.getAttachment(key)
    }

    suspend fun changeComplete(key: Long){
        databaseInterface.changeComplete(key)
    }

    suspend fun clearTodoItems(key: Long){
        databaseInterface.clearTodoItems(key)
    }

    suspend fun clearTodoItemAttachments(key: Long){
        databaseInterface.clearTodoItemAttachments(key)
    }
}