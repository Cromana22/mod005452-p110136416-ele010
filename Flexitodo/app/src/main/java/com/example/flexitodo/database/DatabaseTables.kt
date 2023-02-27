package com.example.flexitodo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Tables, with default values to load a row. Initialise Ints with -1.
@Entity(tableName = "todo_list")
data class TodoList(
    @PrimaryKey(autoGenerate = true)
    var listId: Long = 0L,

    @ColumnInfo(name = "list_name")
    var listName: String = "My Todo List",
)

@Entity(tableName = "todo_item", foreignKeys = [
    (ForeignKey(entity = TodoList::class,
        parentColumns = ["listId"],
        childColumns = ["list_id"],
        onDelete = ForeignKey.CASCADE))], indices = [Index("itemId")])
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    var itemId: Long = 0L,

    @ColumnInfo(name = "list_id")
    var listId: Long = 0L,

    @ColumnInfo(name = "item_summary")
    var itemSummary: String = "Todo",

    @ColumnInfo(name = "item_folder" )
    var itemFolder: String = "Sometime",

    @ColumnInfo(name = "item_date")
    var itemDate: Long? = System.currentTimeMillis(),

    @ColumnInfo(name = "item_notes")
    var itemNotes: String? = "",

    @ColumnInfo(name = "item_complete")
    var itemComplete: Boolean = false,
)

@Entity(tableName = "todo_item_attachment", foreignKeys = [
    (ForeignKey(entity = TodoItem::class,
        parentColumns = ["itemId"],
        childColumns = ["item_id"],
        onDelete = ForeignKey.CASCADE))], indices = [Index("attachmentId")])
data class TodoItemAttachment(
    @PrimaryKey(autoGenerate = true)
    var attachmentId: Long = 0L,

    @ColumnInfo(name = "item_id")
    var itemId: Long = 0L,

    @ColumnInfo(name = "attachment_filename")
    var attachmentFilename: String = "Attachment.txt",
)