package com.saxipapsi.weathermap.utility.extension

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun String.toDateFormat(outputFormat: String = "MMM dd, yyyy", parseFormat : String = "yyyy-MM-dd"): String? {
    return try {
        if (this.trim().isNotEmpty()) {
            val dateFormat = SimpleDateFormat(parseFormat, Locale.getDefault())
            val parsedDate = dateFormat.parse(this)
            parsedDate?.let { SimpleDateFormat(outputFormat).format(it) }
        } else null
    } catch (e: ParseException) {
        "No date provided"
    }
}