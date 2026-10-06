package com.picke.data.feature.perspective.repository

import com.picke.data.common.error.toReportedFailure
import com.picke.data.common.model.toResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.perspective.datasource.PerspectiveApi
import com.picke.data.feature.perspective.model.PerspectiveRequestDto
import com.picke.data.feature.perspective.model.toDomainModel
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.feature.perspective.model.PerspectiveDetailBoard
import com.picke.domain.feature.perspective.model.PerspectiveLikeCountBoard
import com.picke.domain.feature.perspective.model.PerspectiveLikeToggleBoard
import com.picke.domain.feature.perspective.model.PerspectivePage
import com.picke.domain.feature.perspective.model.PerspectiveStatusBoard
import com.picke.domain.feature.perspective.model.PerspectiveUpdateBoard
import com.picke.domain.feature.perspective.repository.PerspectiveRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class PerspectiveRepositoryImpl @Inject constructor(
    private val perspectiveApi: PerspectiveApi
) : PerspectiveRepository {

    override suspend fun getPerspectives(
        battleId: Long,
        cursor: String?,
        size: Int,
        optionId: Long?,
        sort: String
    ): Result<PerspectivePage> = apiCall {
        perspectiveApi.getPerspectives(battleId, cursor, size, optionId, sort)
            .toResult("관점 목록을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun createPerspective(battleId: Long, content: String): Result<PerspectiveStatusBoard> = apiCall {
        perspectiveApi.createPerspective(battleId, PerspectiveRequestDto(content))
            .toResult("관점을 작성하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getMyPerspective(battleId: Long): Result<PerspectiveDetailBoard> = apiCall {
        perspectiveApi.getMyPerspective(battleId)
            .toResult("내 관점을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getPerspective(perspectiveId: Long): Result<PerspectiveDetailBoard> = apiCall {
        perspectiveApi.getPerspective(perspectiveId)
            .toResult("관점 상세 정보를 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun deletePerspective(perspectiveId: Long): Result<String> = apiCall {
        perspectiveApi.deletePerspective(perspectiveId)
            .toResult("관점을 삭제하지 못했습니다.")
    }

    override suspend fun updatePerspective(perspectiveId: Long, content: String): Result<PerspectiveUpdateBoard> = apiCall {
        perspectiveApi.updatePerspective(perspectiveId, PerspectiveRequestDto(content))
            .toResult("관점을 수정하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun retryModeration(perspectiveId: Long): Result<String> = apiCall {
        perspectiveApi.retryModeration(perspectiveId)
            .toResult("검토 재요청을 하지 못했습니다.")
    }

    override suspend fun getPerspectiveLikeCount(perspectiveId: Long): Result<PerspectiveLikeCountBoard> = apiCall {
        perspectiveApi.getPerspectiveLikeCount(perspectiveId)
            .toResult("좋아요 수를 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun likePerspective(perspectiveId: Long): Result<PerspectiveLikeToggleBoard> = apiCall {
        perspectiveApi.likePerspective(perspectiveId)
            .toResult("좋아요를 등록하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun unlikePerspective(perspectiveId: Long): Result<PerspectiveLikeToggleBoard> = apiCall {
        perspectiveApi.unlikePerspective(perspectiveId)
            .toResult("좋아요를 취소하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun reportPerspective(perspectiveId: Long): Result<String> {
        return try {
            val response = perspectiveApi.reportPerspective(perspectiveId)
            when (response.statusCode) {
                200 -> Result.success(response.data ?: "Success")
                409 -> Result.failure(Exception("ALREADY_REPORTED"))
                else -> Result.failure(ApiErrorException("신고 실패(Code: ${response.statusCode}): ${response.error?.message ?: "알 수 없는 에러"}"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            if (e.message?.contains("409") == true) {
                Result.failure(Exception("ALREADY_REPORTED"))
            } else {
                e.toReportedFailure()
            }
        }
    }
}