package com.picke.domain.feature.perspective.usecase

import com.picke.domain.feature.perspective.repository.PerspectiveRepository

class UpdatePerspectiveUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(perspectiveId: Long, content: String): Result<Unit> =
        perspectiveRepository.updatePerspective(perspectiveId, content).map { }
}