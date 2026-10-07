package com.picke.data.feature.alarm.repository

import com.picke.data.common.model.toResult
import com.picke.data.common.model.toUnitResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.alarm.datasource.AlarmApi
import com.picke.data.feature.alarm.model.toDomainModel
import com.picke.domain.feature.alarm.model.AlarmDetailBoard
import com.picke.domain.feature.alarm.model.AlarmPageBoard
import com.picke.domain.feature.alarm.repository.AlarmRepository
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmApi: AlarmApi
) : AlarmRepository {

    override suspend fun getAlarms(category: String, page: Int, size: Int): Result<AlarmPageBoard> = apiCall {
        alarmApi.getAlarms(category, page, size)
            .toResult("알림 목록을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun hasUnreadAlarms(category: String?): Result<Boolean> = apiCall {
        alarmApi.getUnreadExists(category)
            .toResult("미읽음 알림 여부를 불러오지 못했습니다.")
            .map { it.hasUnread ?: false }
    }

    override suspend fun getAlarmDetail(notificationId: Long): Result<AlarmDetailBoard> = apiCall {
        alarmApi.getAlarmDetail(notificationId)
            .toResult("알림 상세 정보를 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun readAlarm(notificationId: Long): Result<Unit> = apiCall {
        alarmApi.readAlarm(notificationId)
            .toUnitResult("알림을 읽음 처리하지 못했습니다.")
    }

    override suspend fun readAllAlarms(): Result<Unit> = apiCall {
        alarmApi.readAllAlarms()
            .toResult("알림을 모두 읽음 처리하지 못했습니다.")
            .map { }
    }
}