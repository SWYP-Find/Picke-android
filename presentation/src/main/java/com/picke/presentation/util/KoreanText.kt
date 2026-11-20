package com.picke.presentation.util

private const val FINAL_CONSONANT_CYCLE = 28
private const val FINAL_CONSONANT_DIGITS = "013678"

fun String.hasFinalConsonant(): Boolean {
    val lastChar = lastOrNull { it.isLetterOrDigit() } ?: return false
    return when (lastChar) {
        in '가'..'힣' -> (lastChar - '가') % FINAL_CONSONANT_CYCLE != 0
        in '0'..'9' -> lastChar in FINAL_CONSONANT_DIGITS
        else -> false
    }
}