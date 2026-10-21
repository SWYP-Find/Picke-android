package com.picke.domain.feature.perspective.usecase

import com.picke.domain.feature.perspective.repository.PerspectiveRepository

class CreatePerspectiveUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(battleId: Long, content: String): Result<Unit> =
        perspectiveRepository.createPerspective(battleId, content).map { }
}