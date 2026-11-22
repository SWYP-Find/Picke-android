package com.picke.domain.feature.perspective.usecase

import com.picke.domain.common.model.BattleTarget
import com.picke.domain.feature.perspective.repository.PerspectiveRepository

class CreatePerspectiveUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(target: BattleTarget, content: String): Result<Unit> =
        perspectiveRepository.createPerspective(target, content).map { }
}