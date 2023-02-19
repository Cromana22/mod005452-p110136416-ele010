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
    fun aInsertAndToggle() {
        // Insert List
        val todoList = TodoList()
        databaseInterface.insertList(todoList)

        val getTodoLists = databaseInterface.getTodoLists()
        assertEquals(getTodoLists.size, 1)

        // Insert Item
        val listId = getTodoLists[0].listId
        val todoItem = TodoItem(listId = listId)
        databaseInterface.insertItem(todoItem)

        val getTodoItems = databaseInterface.getTodoItems(listId)
        assertEquals(getTodoItems.size, 1)

        // Insert Attachment
        val itemId = getTodoItems[0].itemId
        val todoItemAttachment = TodoItemAttachment(itemId = itemId)
        databaseInterface.insertAttachment(todoItemAttachment)

        val getTodoAttachments = databaseInterface.getTodoItemAttachments(itemId)
        assertEquals(getTodoAttachments.size, 1)

        //Toggle Completion Status
        var completionStatus = databaseInterface.getTodoItemDetails(itemId)?.itemComplete
        assertEquals(completionStatus, false)

        databaseInterface.changeComplete(itemId)
        completionStatus = databaseInterface.getTodoItemDetails(itemId)?.itemComplete
        assertEquals(completionStatus, true)

        databaseInterface.changeComplete(itemId)
        completionStatus = databaseInterface.getTodoItemDetails(itemId)?.itemComplete
        assertEquals(completionStatus, false)
    }
}