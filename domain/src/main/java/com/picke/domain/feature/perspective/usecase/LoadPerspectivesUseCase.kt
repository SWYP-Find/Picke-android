package com.picke.domain.feature.perspective.usecase

import com.picke.domain.common.model.BattleTarget
import com.picke.domain.feature.perspective.model.PerspectivePage
import com.picke.domain.feature.perspective.repository.PerspectiveRepository

class LoadPerspectivesUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(
        target: BattleTarget,
        cursor: String?,
        size: Int = 10,
        optionId: Long?,
        sort: String
    ): Result<PerspectivePage> {
        return perspectiveRepository.getPerspectives(
            target = target,
            cursor = cursor,
            size = size,
            optionId = optionId,
            sort = sort
        )
    }
}