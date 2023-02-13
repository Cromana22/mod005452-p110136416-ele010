package com.example.flexitodo.navigation

sealed class Screens(val route: String) {
    object TodoList: Screens("Todo_List")
    object AddNew: Screens("Add_Todo")
    object Settings: Screens("Settings")
}