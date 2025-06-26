package com.binh.core.data

import com.binh.core.data.network.NetworkResponse
import com.binh.core.data.user.User
import com.binh.core.data.util.toJson
import org.junit.Test

class JsonBuilderTest {

    @Test
    fun testObjectToJson() {
        val x = User(1,"", "", "", "", "")
        println(x.toJson())
        val y = NetworkResponse(x)
        println(y.toJson())
        assert(true)
    }
}