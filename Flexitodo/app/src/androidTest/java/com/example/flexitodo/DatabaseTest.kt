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
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class DatabaseTest {

    private lateinit var databaseInterfaceTest: DatabaseInterfaceTest
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
        databaseInterfaceTest = db.databaseInterfaceTest
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun aInsertAndToggle() {
        // Insert List
        val todoList = TodoList()
        databaseInterfaceTest.insertList(todoList)

        val getTodoLists = databaseInterfaceTest.getTodoLists()
        assertEquals(getTodoLists.size, 1)
        Log.i("INSERTED", getTodoLists.toString())

        // Insert Item
        val listId = getTodoLists[0].listId
        val todoItem = TodoItem(listId = listId)
        databaseInterfaceTest.insertItem(todoItem)

        val getTodoItems = databaseInterfaceTest.getTodoItems(listId)
        assertEquals(getTodoItems.size, 1)
        Log.i("INSERTED", getTodoItems.toString())

        // Insert Attachment
        val itemId = getTodoItems[0].itemId
        val todoItemAttachment = TodoItemAttachment(itemId = itemId)
        databaseInterfaceTest.insertAttachment(todoItemAttachment)

        val getTodoAttachments = databaseInterfaceTest.getTodoItemAttachments(itemId)
        assertEquals(getTodoAttachments.size, 1)
        Log.i("INSERTED", getTodoAttachments.toString())

        //Toggle Completion Status
        var completionStatus = databaseInterfaceTest.getTodoItemDetails(itemId)?.itemComplete
        assertEquals(completionStatus, false)

        databaseInterfaceTest.changeComplete(itemId)
        completionStatus = databaseInterfaceTest.getTodoItemDetails(itemId)?.itemComplete
        assertEquals(completionStatus, true)

        databaseInterfaceTest.changeComplete(itemId)
        completionStatus = databaseInterfaceTest.getTodoItemDetails(itemId)?.itemComplete
        assertEquals(completionStatus, false)
    }
}