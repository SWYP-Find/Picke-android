package com.picke.presentation.ui.classroom.classcreate.component.deadline

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

private const val DATE_RANGE_DAYS = 365
private const val HOURS_PER_DAY = 24
private const val MINUTES_PER_HOUR = 60

class ClassDeadlinePickerState(
    private val today: LocalDate,
    private val minDeadline: LocalDateTime,
    dateIndex: Int,
    hour: Int,
    minute: Int
) {
    var dateIndex by mutableIntStateOf(dateIndex)
        private set
    var hour by mutableIntStateOf(hour)
        private set
    var minute by mutableIntStateOf(minute)
        private set

    val minHour: Int
        get() = if (dateIndex == 0) minDeadline.hour else 0
    val minMinute: Int
        get() = if (dateIndex == 0 && hour == minDeadline.hour) minDeadline.minute else 0

    val hourLabels: List<String> = List(HOURS_PER_DAY) { it.toString() }
    val minuteLabels: List<String> = List(MINUTES_PER_HOUR) { it.toString().padStart(2, '0') }

    val selectedDeadline: LocalDateTime
        get() = maxOf(
            LocalDateTime.of(today.plusDays(dateIndex.toLong()), LocalTime.of(hour, minute)),
            minDeadline
        )

    fun dateLabels(todayLabel: String, dateFormat: String): List<String> {
        val formatter = DateTimeFormatter.ofPattern(dateFormat, Locale.KOREAN)
        return List(DATE_RANGE_DAYS) { offset ->
            if (offset == 0) todayLabel else today.plusDays(offset.toLong()).format(formatter)
        }
    }

    fun selectDate(index: Int) {
        dateIndex = index
        clampToMinDeadline()
    }

    fun selectHour(value: Int) {
        hour = value
        clampToMinDeadline()
    }

    fun selectMinute(value: Int) {
        minute = value
        clampToMinDeadline()
    }

    private fun clampToMinDeadline() {
        val clamped = selectedDeadline
        hour = clamped.hour
        minute = clamped.minute
    }

    companion object {
        fun of(deadline: LocalDateTime, now: LocalDateTime): ClassDeadlinePickerState {
            val today = now.toLocalDate()
            val minDeadline = now.truncatedTo(ChronoUnit.MINUTES)
            val initialDeadline = maxOf(deadline, minDeadline)
            return ClassDeadlinePickerState(
                today = today,
                minDeadline = minDeadline,
                dateIndex = ChronoUnit.DAYS.between(today, initialDeadline.toLocalDate()).toInt()
                    .coerceIn(0, DATE_RANGE_DAYS - 1),
                hour = initialDeadline.hour,
                minute = initialDeadline.minute
            )
        }

        val Saver: Saver<ClassDeadlinePickerState, Any> = listSaver(
            save = { state ->
                listOf(
                    state.today.toString(),
                    state.minDeadline.toString(),
                    state.dateIndex,
                    state.hour,
                    state.minute
                )
            },
            restore = { saved ->
                ClassDeadlinePickerState(
                    today = LocalDate.parse(saved[0] as String),
                    minDeadline = LocalDateTime.parse(saved[1] as String),
                    dateIndex = saved[2] as Int,
                    hour = saved[3] as Int,
                    minute = saved[4] as Int
                )
            }
        )
    }
}

@Composable
fun rememberClassDeadlinePickerState(deadline: LocalDateTime): ClassDeadlinePickerState =
    rememberSaveable(saver = ClassDeadlinePickerState.Saver) {
        ClassDeadlinePickerState.of(deadline = deadline, now = LocalDateTime.now())
    }