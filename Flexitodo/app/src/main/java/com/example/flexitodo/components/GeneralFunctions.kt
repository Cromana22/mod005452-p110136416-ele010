package com.example.flexitodo.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun longToStringDate(long: Long?): String {
    return if (long is Long) {
        SimpleDateFormat("dd-MM-yyyy").format(Date(long))
    }
    else {
        ""
    }
}

fun dateFormatUK(localDate: LocalDate): String {
    val formatters: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return localDate.format(formatters)
}

fun stringToLongDate(string: String): Long? {

    var longDate: Long? = null

    try {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val date = sdf.parse(string)
        longDate = date.time
    }
    catch (e: ParseException) {  }

    return longDate
}

fun longToStringDateApi(long: Long?): String {
    return if (long is Long) {
        SimpleDateFormat("yyyy-MM-dd").format(Date(long))
    }
    else {
        ""
    }
}