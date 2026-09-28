package com.picke.presentation.util

fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}

fun formatSpeed(speed: Float): String = "%.2f".format(speed).removeSuffix("0")