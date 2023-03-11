package com.example.flexitodo

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.compose.rememberNavController
import com.example.flexitodo.database.DatabaseInitializer
import com.example.flexitodo.ui.theme.FlexitodoTheme
import com.example.flexitodo.navigation.NavGraph
import com.example.flexitodo.screens.ContentTodoList
import com.example.flexitodo.screens.TopAppBarTodoList

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DatabaseInitializer().initDatabase(this)

        setContent {
            FlexitodoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}