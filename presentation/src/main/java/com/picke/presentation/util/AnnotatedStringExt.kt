package com.picke.presentation.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle

fun AnnotatedString.Builder.appendHighlighted(text: String, highlight: String, style: SpanStyle) {
    val start = text.indexOf(highlight)
    if (start < 0) {
        append(text)
        return
    }
    append(text.substring(0, start))
    withStyle(style) { append(highlight) }
    append(text.substring(start + highlight.length))
}