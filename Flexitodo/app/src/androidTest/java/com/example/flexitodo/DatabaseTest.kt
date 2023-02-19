package com.example.flexitodo

import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.flexitodo.database.*
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var databaseInterface: DatabaseInterface
    private lateinit var db: DatabaseClass

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, DatabaseClass::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        databaseInterface = db.databaseInterface
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGet() {
        val todoList = TodoList()
        databaseInterface.insertList(todoList)
        val queryTest1 = databaseInterface.getTodoLists()
        assertEquals(queryTest1.size, 1)
        Log.i("INSERTED", queryTest1.toString())

        val todoItem = TodoItem(listId = queryTest1[queryTest1.size-1].listId)
        databaseInterface.insertItem(todoItem)
        val queryTest2 = databaseInterface.getTodoItems(queryTest1[queryTest1.size-1].listId)
        assertEquals(queryTest2.size, 1)
        Log.i("INSERTED", queryTest2.toString())

        val todoItemAttachment = TodoItemAttachment(itemId = queryTest2[queryTest2.size-1].itemId)
        databaseInterface.insertAttachment(todoItemAttachment)
        val queryTest3 = databaseInterface.getTodoItemAttachments(queryTest2[queryTest2.size-1].itemId)
        assertEquals(queryTest3.size, 1)
        Log.i("INSERTED", queryTest3.toString())
    }
}