package com.example.flexitodo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.flexitodo.ui.theme.FlexitodoTheme
import com.example.flexitodo.*

@ExperimentalMaterial3Api
class Settings : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlexitodoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    Layout()
                }
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun Layout() {
        Scaffold(
            content = {
                Column {
                    Row {
                        Greeting("Settings")
                    }
                    Row {
                        NavButton(page = MainActivity::class.java, text = "Home")
                        NavButton(page = AddTodo::class.java, text = "Add New")
                    }
                }
            }
        )
    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @Composable
    fun NavButton(page: Class<*>, text: String) {
        Button(onClick = { changePage(this, page) }) { Text(text) }
    }
}
