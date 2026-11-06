package com.picke.data.feature.attendance.model

import com.picke.domain.feature.attendance.model.AttendanceBoard
import com.picke.domain.feature.attendance.model.WeeklyAttendance
import com.picke.domain.feature.attendance.model.WeeklyAttendanceDay
import com.picke.domain.feature.attendance.model.WeeklyAttendanceDayStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 출석 체크 API Response
 */
@Serializable
data class AttendanceCheckResponseDto(
    @SerialName("user_tag")
    val userTag: String,
    @SerialName("attended_at")
    val attendedAt: String,
    @SerialName("points_earned")
    val pointsEarned: Int,
    @SerialName("streak_bonus_earned")
    val streakBonusEarned: Boolean,
    @SerialName("streak_bonus_points")
    val streakBonusPoints: Int,
    @SerialName("consecutive_days")
    val consecutiveDays: Int,
    @SerialName("total_points")
    val totalPoints: Int
)

fun AttendanceCheckResponseDto.toDomain(): AttendanceBoard {
    return AttendanceBoard(
        pointsEarned = pointsEarned,
        streakBonusEarned = streakBonusEarned,
        streakBonusPoints = streakBonusPoints,
        consecutiveDays = consecutiveDays,
        totalPoints = totalPoints
    )
}

/**
 * 이번 주 출석 현황 조회 API Response
 */
@Serializable
data class WeeklyAttendanceResponseDto(
    @SerialName("user_tag")
    val userTag: String,
    @SerialName("week_start_date")
    val weekStartDate: String,
    @SerialName("consecutive_days")
    val consecutiveDays: Int,
    @SerialName("is_streak_achieved")
    val isStreakAchieved: Boolean,
    @SerialName("weekly_attendance")
    val weeklyAttendance: List<WeeklyAttendanceDayDto>,
    @SerialName("streak_reward_points")
    val streakRewardPoints: Int
)

@Serializable
data class WeeklyAttendanceDayDto(
    val day: String,
    val date: String,
    val status: String,
    val points: Int
)

fun WeeklyAttendanceResponseDto.toDomain(): WeeklyAttendance {
    return WeeklyAttendance(
        userTag = userTag,
        weekStartDate = weekStartDate,
        consecutiveDays = consecutiveDays,
        isStreakAchieved = isStreakAchieved,
        days = weeklyAttendance.map { it.toDomain() },
        streakRewardPoints = streakRewardPoints
    )
}

fun WeeklyAttendanceDayDto.toDomain(): WeeklyAttendanceDay {
    return WeeklyAttendanceDay(
        day = day,
        date = date,
        status = when (status) {
            "ATTENDED" -> WeeklyAttendanceDayStatus.ATTENDED
            "MISSED" -> WeeklyAttendanceDayStatus.MISSED
            else -> WeeklyAttendanceDayStatus.UPCOMING
        },
        points = points
    )
}