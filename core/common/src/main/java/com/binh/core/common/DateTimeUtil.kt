package com.binh.core.common

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

const val YYYY_MM_DD_HH_MM_DASH_FORMAT = "yyyy-MM-dd HH:mm"
const val YYYY_MM_DD_DASH_FORMAT = "yyyy-MM-dd"
const val YYYY_MM_DD_DOT_FORMAT = "yyyy.MM.dd"
const val DATE_TIME_ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
private const val TAG_DATE_FORMAT = "DATE_FORMAT"

val TIME_ZONE_UTC: TimeZone get() = TimeZone.getTimeZone("UTC")

fun String.toDate(pattern: String): Date? {
    return try {
        SimpleDateFormat(pattern, Locale.US).parse(this)
    } catch (e: ParseException) {
        Log.e(TAG_DATE_FORMAT, "e", e)
        null
    }
}

fun String.toDate(pattern: String, timeZone: TimeZone): Date? {
    return try {
        val formatter = SimpleDateFormat(pattern, Locale.US)
        formatter.timeZone = timeZone
        formatter.parse(this)
    } catch (e: ParseException) {
        Log.e(TAG_DATE_FORMAT, "e", e)
        null
    }
}

fun String.newFormat(inputPattern: String, outPutPattern: String): String? {
    return try {
        val inputFormat = SimpleDateFormat(inputPattern, Locale.US)
        val date = inputFormat.parse(this)
        val outputFormat = SimpleDateFormat(outPutPattern, Locale.US)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        Log.e(TAG_DATE_FORMAT, "e", e)
        null
    }
}

fun String.newFormat(
    inputPattern: String,
    inputTimeZone: TimeZone,
    outPutPattern: String,
    outputTimeZone: TimeZone
): String? {
    return try {
        val inputFormat = SimpleDateFormat(inputPattern, Locale.US)
        inputFormat.timeZone = inputTimeZone
        val date = inputFormat.parse(this)
        val outputFormat = SimpleDateFormat(outPutPattern, Locale.US)
        outputFormat.timeZone = outputTimeZone
        outputFormat.format(date!!)
    } catch (e: Exception) {
        Log.e(TAG_DATE_FORMAT, "e", e)
        null
    }
}

fun String.localDateToUtcString(inputPattern: String): String? {
    return newFormat(
        inputPattern,
        TimeZone.getDefault(),
        DATE_TIME_ISO_8601_FORMAT,
        TIME_ZONE_UTC
    )
}

fun Date.toString(outputPattern: String): String? {
    return try {
        SimpleDateFormat(outputPattern, Locale.US).format(this)
    } catch (e: Exception) {
        Log.e(TAG_DATE_FORMAT, "e", e)
        null
    }
}

fun Date.toString(outputPattern: String, timeZone: TimeZone): String? {
    val formatter = SimpleDateFormat(outputPattern, Locale.US)
    formatter.timeZone = timeZone
    return try {
        formatter.format(this)
    } catch (e: Exception) {
        Log.e(TAG_DATE_FORMAT, "e", e)
        null
    }
}

val String.utcStringToLocalDate get() = toDate(DATE_TIME_ISO_8601_FORMAT, TIME_ZONE_UTC)

val Date.toUtcString get() = toString(DATE_TIME_ISO_8601_FORMAT, TIME_ZONE_UTC)