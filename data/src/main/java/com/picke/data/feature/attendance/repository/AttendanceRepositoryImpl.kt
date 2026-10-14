package com.picke.data.feature.attendance.repository

import com.picke.data.common.model.toResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.attendance.datasource.AttendanceApi
import com.picke.data.feature.attendance.model.toDomain
import com.picke.domain.feature.attendance.model.AttendanceBoard
import com.picke.domain.feature.attendance.model.WeeklyAttendance
import com.picke.domain.feature.attendance.repository.AttendanceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceRepositoryImpl @Inject constructor(
    private val attendanceApi: AttendanceApi
) : AttendanceRepository {

    override suspend fun checkAttendance(): Result<AttendanceBoard> = apiCall {
        attendanceApi.checkAttendance()
            .toResult("출석 체크를 하지 못했습니다.")
            .map { it.toDomain() }
    }

    override suspend fun getWeeklyAttendance(): Result<WeeklyAttendance> = apiCall {
        attendanceApi.getWeeklyAttendance()
            .toResult("이번 주 출석 현황을 불러오지 못했습니다.")
            .map { it.toDomain() }
    }
}