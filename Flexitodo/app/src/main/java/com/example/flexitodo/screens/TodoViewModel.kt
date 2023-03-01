package com.example.flexitodo.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


class TodoViewModel(application: Application) : AndroidViewModel(application) {
    val toggleComplete: MutableLiveData<Boolean> = MutableLiveData(true)
}