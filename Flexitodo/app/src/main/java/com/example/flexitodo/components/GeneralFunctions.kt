package com.example.flexitodo.components

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun LongToStringDate(long: Long): String {
    return SimpleDateFormat("dd-MM-yyyy").format(Date(long))
}

fun DateFormatUK(localDate: LocalDate): String {
    val formatters: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return localDate.format(formatters)
}

fun StringToLongDate(string: String): Long? {

    var longDate: Long? = null

    try {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val date = sdf.parse(string)
        longDate = date.time
    }
    catch (e: ParseException) { e.printStackTrace() }

    return longDate
}