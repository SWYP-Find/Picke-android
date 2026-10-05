package com.picke.data.feature.explore.repository

import com.picke.data.common.error.toReportedFailure
import com.picke.data.feature.explore.datasource.ExploreApi
import com.picke.data.feature.explore.model.toDomainModel
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.feature.explore.model.ExplorePageBoard
import com.picke.domain.feature.explore.repository.ExploreRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ExploreRepositoryImpl @Inject constructor(
    private val exploreApi: ExploreApi
) : ExploreRepository {
    override suspend fun searchBattles(
        category: String?,
        sort: String,
        offset: Int?,
        size: Int
    ): Result<ExplorePageBoard> {
        return try {
            val response = exploreApi.searchBattles(category, sort, offset, size)

            // 데이터가 없으면 예외 처리, 있으면 성공 처리
            val data = response.data ?: throw ApiErrorException(response.error?.message ?: "탐색 목록을 불러오지 못했습니다.")
            Result.success(data.toDomainModel())

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            e.toReportedFailure()
        }
    }
}