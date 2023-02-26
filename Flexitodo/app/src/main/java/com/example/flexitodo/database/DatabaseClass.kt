package com.example.flexitodo.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// This is the constructor.
@Database(entities = [TodoList::class, TodoItem::class, TodoItemAttachment::class], version = 1, exportSchema = false)
abstract class DatabaseClass : RoomDatabase() {

    abstract val databaseInterface: DatabaseInterface
    abstract val databaseInterfaceTest: DatabaseInterfaceTest


    companion object {

        @Volatile
        private var INSTANCE: DatabaseClass? = null

        fun getInstance(context: Context): DatabaseClass {
            synchronized(this) {
                var instance = INSTANCE
                Log.i("database", instance.toString())

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseClass::class.java,
                        "flexitodo-db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    //insert default list
                    val todoList = TodoList(listName = "My Todos")
                    val databaseInterface = instance.databaseInterface
                    databaseInterface.insertList(todoList)
                    val todoItem = TodoItem(listId = 1L)
                    databaseInterface.insertItem(todoItem)

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}