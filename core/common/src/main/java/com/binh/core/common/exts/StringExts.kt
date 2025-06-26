package com.binh.core.common.exts

import android.util.Patterns

val CharSequence.mightBePhone get() = Patterns.PHONE.matcher(this).matches()

val CharSequence.isValidEmail get() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Char.isSpecificCharacter(): Boolean {
    val type = Character.getType(this)
    return type in arrayOf(
        Character.SURROGATE.toInt(),
        Character.OTHER_SYMBOL.toInt(),
        Character.DASH_PUNCTUATION.toInt(),
        Character.START_PUNCTUATION.toInt(),
        Character.END_PUNCTUATION.toInt(),
        Character.CONNECTOR_PUNCTUATION.toInt(),
        Character.OTHER_PUNCTUATION.toInt(),
        Character.END_PUNCTUATION.toInt(),
        Character.MATH_SYMBOL.toInt(),
        Character.CURRENCY_SYMBOL.toInt(),
        Character.MODIFIER_SYMBOL.toInt(),
        Character.LINE_SEPARATOR.toInt(),
        Character.SPACE_SEPARATOR.toInt(),
        Character.PARAGRAPH_SEPARATOR.toInt(),
    )
}

fun CharSequence?.valueOr(default: CharSequence): CharSequence {
    return this ?: default
}