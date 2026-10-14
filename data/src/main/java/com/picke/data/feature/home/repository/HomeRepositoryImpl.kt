package com.picke.data.feature.home.repository

import com.picke.data.common.model.toResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.home.datasource.HomeApi
import com.picke.data.feature.home.model.toDomainModel
import com.picke.domain.feature.home.model.HomeBoard
import com.picke.domain.feature.home.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi
) : HomeRepository {

    override suspend fun fetchHomeData(): Result<HomeBoard> = apiCall {
        homeApi.getHomeData()
            .toResult("홈 데이터를 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }
}