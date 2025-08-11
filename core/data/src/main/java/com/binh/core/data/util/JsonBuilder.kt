@file:OptIn(ExperimentalStdlibApi::class)

package com.binh.core.data.util

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

fun getMoshiBuilder(): Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

inline fun <reified T> T.toJson(): String? = try {
    getMoshiBuilder().adapter<T>(typeOf<T>().javaType).toJson(this)
} catch (e: Exception) {
    Log.e("ObjectToJson", "e", e)
    null
}

inline fun <reified T> String.jsonToObject(): T? = try {
    getMoshiBuilder().adapter<T>(typeOf<T>().javaType).fromJson(this)
} catch (e: Exception) {
    Log.e("JsonToObject", "e", e)
    null
}
