package com.example.flexitodo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

// This is the constructor.
@Database(entities = [TodoList::class, TodoItem::class, TodoItemAttachment::class], version = 2, exportSchema = false)
abstract class DatabaseClass : RoomDatabase() {

    abstract val databaseInterface: DatabaseInterface
    abstract val databaseInterfaceTest: DatabaseInterfaceTest


    companion object {

        @Volatile
        private var INSTANCE: DatabaseClass? = null

        fun getInstance(context: Context): DatabaseClass {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseClass::class.java,
                        "sqlite.db"
                    )
                        .fallbackToDestructiveMigration()
                        //.createFromAsset("sqlite.db")
                        .build()

//                    val todoList = TodoList(listName = "My Todos")
//                    val databaseInterface = instance.databaseInterfaceTest
//                    databaseInterface.insertList(todoList)
//                    val todoItem1 = TodoItem(listId = 1L, itemSummary = "Example Todo 1")
//                    val todoItem2 = TodoItem(listId = 1L, itemSummary = "Example Todo 2")
//                    databaseInterface.insertItem(todoItem1)
//                    databaseInterface.insertItem(todoItem2)

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}