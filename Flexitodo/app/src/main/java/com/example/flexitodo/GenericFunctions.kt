package com.example.flexitodo

import android.app.Activity
import android.content.Context
import android.content.Intent
fun Context.changePage(activity: Activity, page: Class<*>) {
    val i = Intent(activity, page)
    startActivity(i)
}