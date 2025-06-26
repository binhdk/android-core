package com.binh.core.common

data class ChangedObjects<T, K>(
    private val objects: MutableMap<K, Pair<T, MutableSet<Long>>> = mutableMapOf(),
    private val codes: MutableSet<Long> = mutableSetOf()
) {

    fun registerCode(code: Long) {
        codes.add(code)
    }

    fun removeCode(code: Long) {
        codes.remove(code)
        objects.forEach {
            it.value.second.removeAll { it == code }
        }
    }

    fun put(key: K, t: T) {
        objects[key] = Pair(t, mutableSetOf<Long>().apply { addAll(codes) })
    }

    fun remove(key: K) {
        objects.remove(key)
    }

    fun get(key: K): Pair<T, MutableSet<Long>>? {
        return objects[key]
    }

    fun getChangedObject(key: K, code: Long): T? {
        return get(key)?.let {
            if (it.second.find { it == code } != null) {
                it.second.remove(code)
                it.first
            } else {
                null
            }

        }
    }

    fun getChangedObjects(code: Long): MutableSet<T> {
        val data = objects.values.filter { it.second.firstOrNull { it == code } != null }
            .map { it.first }.toMutableSet()
        objects.forEach {
            it.value.second.removeAll { it == code }
        }
        return data
    }

}