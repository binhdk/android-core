package com.binh.core.common.exts

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

const val NUMBER_FORMAT_THOUSAND = "#,###,###"

val Int.formatDecimal: String
    get() {
        val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
        formatter.applyPattern(NUMBER_FORMAT_THOUSAND)
        return formatDecimal.format(this)
    }

val Long.formatDecimal: String
    get() {
        val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
        formatter.applyPattern(NUMBER_FORMAT_THOUSAND)
        return formatDecimal.format(this)
    }
