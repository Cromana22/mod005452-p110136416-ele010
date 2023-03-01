package com.example.flexitodo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

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
                        .build()

//                    val todoList = TodoList(listName = "My Todos")
//                    val databaseInterface = instance.databaseInterfaceTest
//                    databaseInterface.insertList(todoList)

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}