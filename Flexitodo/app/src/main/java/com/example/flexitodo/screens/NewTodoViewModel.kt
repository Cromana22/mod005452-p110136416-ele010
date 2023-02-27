package com.example.flexitodo.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


class NewTodoViewModel(application: Application) : AndroidViewModel(application) {
    var summary: MutableLiveData<String> = MutableLiveData("")
    var folder: MutableLiveData<String> = MutableLiveData("Today")
    var datePicked: MutableLiveData<String?> = MutableLiveData("")
    var notes: MutableLiveData<String> = MutableLiveData("")
}