package com.picke.domain.feature.perspective.usecase

import com.picke.domain.common.model.BattleTarget
import com.picke.domain.feature.perspective.model.PerspectiveDetailBoard
import com.picke.domain.feature.perspective.repository.PerspectiveRepository

class GetMyPerspectivesUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(target: BattleTarget): Result<List<PerspectiveDetailBoard>> {
        return perspectiveRepository.getMyPerspectives(target)
    }
}