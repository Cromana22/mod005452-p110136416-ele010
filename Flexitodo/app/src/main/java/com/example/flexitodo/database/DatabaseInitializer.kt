package com.example.flexitodo.database

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseInitializer {

    fun initDatabase(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseClass.getInstance(context)
        }
    }
}