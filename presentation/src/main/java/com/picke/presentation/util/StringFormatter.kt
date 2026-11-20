package com.picke.presentation.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}

fun formatSpeed(speed: Float): String = "%.2f".format(speed).removeSuffix("0")

fun formatRate(rate: Float): String = "%.1f".format(rate).removeSuffix(".0")

fun formatCount(count: Int): String = "%,d".format(count)

fun formatDate(date: LocalDate): String = date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))