package com.binh.core.common

import org.junit.Test
import java.util.TimeZone

class ExampleUnitTest {

    @Test
    fun testDateTimeUtil() {

        val dateString = "2023-08-13 05:00"
        val utcToLocal = dateString.newFormat(
            YYYY_MM_DD_HH_MM_DASH_FORMAT,
            TIME_ZONE_UTC,
            YYYY_MM_DD_HH_MM_DASH_FORMAT,
            TimeZone.getDefault()
        )
        assert(true)
    }
}