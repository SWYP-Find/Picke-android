package com.picke.data.feature.share.repository

import com.picke.data.common.model.toResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.mypage.model.toDomainModel
import com.picke.data.feature.share.datasource.ShareApi
import com.picke.data.feature.share.model.toDomainModel
import com.picke.domain.feature.mypage.model.MyRecapBoard
import com.picke.domain.feature.share.model.ShareKey
import com.picke.domain.feature.share.model.ShareUrl
import com.picke.domain.feature.share.repository.ShareRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShareRepositoryImpl @Inject constructor(
    private val shareApi: ShareApi
) : ShareRepository {

    override suspend fun getReportShareLink(reportId: Int): Result<ShareUrl> = apiCall {
        shareApi.getReportShareLink(reportId)
            .toResult("철학자 유형 공유 링크를 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getBattleShareLink(battleId: Int): Result<ShareUrl> = apiCall {
        shareApi.getBattleShareLink(battleId)
            .toResult("배틀 공유 링크를 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getRecapShareKey(): Result<ShareKey> = apiCall {
        shareApi.getRecapShareKey()
            .toResult("공유 키를 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getRecapDetail(shareKey: String): Result<MyRecapBoard> = apiCall {
        shareApi.getRecapDetail(shareKey)
            .toResult("다른 사용자의 철학자 유형을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }
}