package com.picke.presentation.ui.classroom.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.picke.presentation.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun rememberClassDeadlineText(deadline: LocalDateTime?): String {
    val pattern = stringResource(R.string.class_setting_deadline_format)
    return remember(deadline, pattern) {
        deadline?.format(DateTimeFormatter.ofPattern(pattern)).orEmpty()
    }
}