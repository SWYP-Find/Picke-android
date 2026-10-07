package com.picke.data.feature.mypage.repository

import com.picke.data.common.model.toResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.mypage.datasource.MyPageApi
import com.picke.data.feature.mypage.model.ProfileUpdateRequestDto
import com.picke.data.feature.mypage.model.toDomainModel
import com.picke.data.feature.mypage.model.toDto
import com.picke.domain.feature.mypage.model.CreditHistoryPage
import com.picke.domain.feature.mypage.model.MyBattleRecordPage
import com.picke.domain.feature.mypage.model.MyContentActivityPage
import com.picke.domain.feature.mypage.model.MyPageInfoBoard
import com.picke.domain.feature.mypage.model.MyRecapBoard
import com.picke.domain.feature.mypage.model.NotificationSettingsBoard
import com.picke.domain.feature.mypage.model.ProfileUpdateBoard
import com.picke.domain.feature.mypage.repository.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageApi: MyPageApi
) : MyPageRepository {

    override suspend fun getMyBattleRecords(offset: Int?, size: Int, voteSide: String?): Result<MyBattleRecordPage> = apiCall {
        myPageApi.getMyBattleRecords(offset, size, voteSide)
            .toResult("배틀 기록을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getMyContentActivities(offset: Int?, size: Int, activityType: String?): Result<MyContentActivityPage> = apiCall {
        myPageApi.getMyContentActivities(offset, size, activityType)
            .toResult("활동 기록을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getMyPageInfo(): Result<MyPageInfoBoard> = apiCall {
        myPageApi.getMyPageInfo()
            .toResult("마이페이지 정보를 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getNotificationSettings(): Result<NotificationSettingsBoard> = apiCall {
        myPageApi.getNotificationSettings()
            .toResult("알림 설정을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun updateNotificationSettings(settings: NotificationSettingsBoard): Result<NotificationSettingsBoard> = apiCall {
        myPageApi.updateNotificationSettings(settings.toDto())
            .toResult("알림 설정을 변경하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun updateProfile(nickname: String, characterType: String): Result<ProfileUpdateBoard> = apiCall {
        myPageApi.updateProfile(ProfileUpdateRequestDto(nickname, characterType))
            .toResult("프로필을 수정하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getMyRecap(): Result<MyRecapBoard> = apiCall {
        myPageApi.getMyRecap()
            .toResult("철학자 유형 정보를 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getCreditHistory(offset: Int?, size: Int): Result<CreditHistoryPage> = apiCall {
        myPageApi.getCreditHistory(offset, size)
            .toResult("포인트 내역을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }
}