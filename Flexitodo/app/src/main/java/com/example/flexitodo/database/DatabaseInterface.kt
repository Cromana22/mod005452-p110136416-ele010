package com.example.flexitodo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// This is your list of queries you can run against the database.
@Dao
interface DatabaseInterface {

    //Add New
    @Insert
    fun insertList(list: TodoList)
    @Insert
    fun insertItem(item: TodoItem)
    @Insert
    fun insertAttachment(attachment: TodoItemAttachment)

    @Update
    fun update(list: TodoList)
    @Update
    fun update(item: TodoItem)

    @Delete
    fun delete(list: TodoList)
    @Delete
    fun delete(item: TodoItem)
    @Delete
    fun delete(attachment: TodoItemAttachment)

    // Get a list of all lists.
    @Query("SELECT * from todo_list")
    fun getTodoLists(): List<TodoList>

    // Get all items in a list for main page, today first.
    @Query("SELECT * from todo_item WHERE list_id = :key ORDER BY item_date ASC")
    fun getTodoItems(key: Long): List<TodoItem>

    // Get all details of a item for item display / edit page.
    @Query("SELECT * from todo_item WHERE itemId = :key")
    fun getTodoItemDetails(key: Long): TodoItem?

    // Get all attachments of a item for item display / edit page.
    @Query("SELECT * from todo_item_attachment WHERE item_id = :key")
    fun getTodoItemAttachments(key: Long): List<TodoItemAttachment>

    // Get attachment details so can view or delete the actual file
    @Query("SELECT * from todo_item_attachment WHERE attachmentId = :key")
    fun getAttachment(key: Long): TodoItemAttachment?

    // Clear all todos from a list
    @Query("DELETE FROM todo_item WHERE list_id = :key")
    fun clearTodoItems(key: Long)

    // Clear all attachments from a item
    @Query("DELETE FROM todo_item_attachment WHERE item_id = :key")
    fun clearTodoItemAttachments(key: Long)
}