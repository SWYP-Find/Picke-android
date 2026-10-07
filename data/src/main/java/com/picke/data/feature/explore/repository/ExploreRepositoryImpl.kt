package com.picke.data.feature.explore.repository

import com.picke.data.common.model.toResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.explore.datasource.ExploreApi
import com.picke.data.feature.explore.model.toDomainModel
import com.picke.domain.feature.explore.model.ExplorePageBoard
import com.picke.domain.feature.explore.repository.ExploreRepository
import javax.inject.Inject

class ExploreRepositoryImpl @Inject constructor(
    private val exploreApi: ExploreApi
) : ExploreRepository {

    override suspend fun searchBattles(
        category: String?,
        sort: String,
        offset: Int?,
        size: Int
    ): Result<ExplorePageBoard> = apiCall {
        exploreApi.searchBattles(category, sort, offset, size)
            .toResult("탐색 목록을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }
}