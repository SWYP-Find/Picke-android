package com.picke.data.feature.perspective.repository

import com.picke.data.common.model.toResult
import com.picke.data.common.model.toUnitResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.perspective.datasource.PerspectiveApi
import com.picke.data.feature.perspective.model.PerspectiveRequestDto
import com.picke.data.feature.perspective.model.toDomainModel
import com.picke.domain.common.model.BattleTarget
import com.picke.domain.feature.perspective.model.MyPerspectiveBoard
import com.picke.domain.feature.perspective.model.PerspectiveDetailBoard
import com.picke.domain.feature.perspective.model.PerspectiveLikeCountBoard
import com.picke.domain.feature.perspective.model.PerspectiveLikeToggleBoard
import com.picke.domain.feature.perspective.model.PerspectivePage
import com.picke.domain.feature.perspective.model.PerspectiveStatusBoard
import com.picke.domain.feature.perspective.model.PerspectiveUpdateBoard
import com.picke.domain.feature.perspective.model.WeeklyBestPerspectiveBoard
import com.picke.domain.feature.perspective.repository.PerspectiveRepository
import javax.inject.Inject

class PerspectiveRepositoryImpl @Inject constructor(
    private val perspectiveApi: PerspectiveApi
) : PerspectiveRepository {

    override suspend fun getPerspectives(
        target: BattleTarget,
        cursor: String?,
        size: Int,
        optionId: Long?,
        sort: String
    ): Result<PerspectivePage> = apiCall {
        when (target) {
            is BattleTarget.General ->
                perspectiveApi.getPerspectives(target.battleId, cursor, size, optionId, sort)

            is BattleTarget.Class ->
                perspectiveApi.getClassPerspectives(target.classId, cursor, size, optionId, sort)
        }
            .toResult("관점 목록을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getMyPerspectives(target: BattleTarget): Result<List<MyPerspectiveBoard>> =
        apiCall {
            when (target) {
                is BattleTarget.General -> perspectiveApi.getMyPerspectives(target.battleId)
                is BattleTarget.Class -> perspectiveApi.getClassMyPerspectives(target.classId)
            }
                .toResult("내 관점을 불러오지 못했습니다.")
                .map { perspectives -> perspectives.map { it.toDomainModel() } }
        }

    override suspend fun getPerspective(perspectiveId: Long): Result<PerspectiveDetailBoard> =
        apiCall {
            perspectiveApi.getPerspective(perspectiveId)
                .toResult("관점 상세 정보를 불러오지 못했습니다.")
                .map { it.toDomainModel() }
        }

    override suspend fun getPerspectiveLikeCount(perspectiveId: Long): Result<PerspectiveLikeCountBoard> =
        apiCall {
            perspectiveApi.getPerspectiveLikeCount(perspectiveId)
                .toResult("좋아요 수를 불러오지 못했습니다.")
                .map { it.toDomainModel() }
        }

    override suspend fun getWeeklyBestPerspectives(): Result<WeeklyBestPerspectiveBoard> = apiCall {
        perspectiveApi.getWeeklyBestPerspectives()
            .toResult("주간 베스트 관점을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun createPerspective(
        target: BattleTarget,
        content: String
    ): Result<PerspectiveStatusBoard> = apiCall {
        val request = PerspectiveRequestDto(content)
        when (target) {
            is BattleTarget.General -> perspectiveApi.createPerspective(target.battleId, request)
            is BattleTarget.Class -> perspectiveApi.createClassPerspective(target.classId, request)
        }
            .toResult("관점을 작성하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun updatePerspective(
        perspectiveId: Long,
        content: String
    ): Result<PerspectiveUpdateBoard> = apiCall {
        perspectiveApi.updatePerspective(perspectiveId, PerspectiveRequestDto(content))
            .toResult("관점을 수정하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun deletePerspective(perspectiveId: Long): Result<Unit> = apiCall {
        perspectiveApi.deletePerspective(perspectiveId)
            .toUnitResult("관점을 삭제하지 못했습니다.")
    }

    override suspend fun retryModeration(perspectiveId: Long): Result<Unit> = apiCall {
        perspectiveApi.retryModeration(perspectiveId)
            .toUnitResult("검토 재요청을 하지 못했습니다.")
    }

    override suspend fun likePerspective(perspectiveId: Long): Result<PerspectiveLikeToggleBoard> =
        apiCall {
            perspectiveApi.likePerspective(perspectiveId)
                .toResult("좋아요를 등록하지 못했습니다.")
                .map { it.toDomainModel() }
        }

    override suspend fun unlikePerspective(perspectiveId: Long): Result<PerspectiveLikeToggleBoard> =
        apiCall {
            perspectiveApi.unlikePerspective(perspectiveId)
                .toResult("좋아요를 취소하지 못했습니다.")
                .map { it.toDomainModel() }
        }

    override suspend fun reportPerspective(perspectiveId: Long): Result<Unit> = apiCall {
        perspectiveApi.reportPerspective(perspectiveId)
            .toUnitResult("관점을 신고하지 못했습니다.")
    }
}