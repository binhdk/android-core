package com.binh.core.common.exts


fun <T> Set<T>?.orEmptySet(): Set<T> = this ?: mutableSetOf()

fun <T> List<T>?.orEmptyList(): List<T> = this ?: mutableListOf()

fun <K, V> Map<K, V>?.orEmptyMap(): Map<K, V> = this ?: mutableMapOf()
