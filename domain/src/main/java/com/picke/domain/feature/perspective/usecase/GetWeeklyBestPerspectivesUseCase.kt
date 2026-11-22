package com.picke.domain.feature.perspective.usecase

import com.picke.domain.feature.perspective.model.WeeklyBestPerspectiveBoard
import com.picke.domain.feature.perspective.repository.PerspectiveRepository

class GetWeeklyBestPerspectivesUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(): Result<WeeklyBestPerspectiveBoard> =
        perspectiveRepository.getWeeklyBestPerspectives()
}