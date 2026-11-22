package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassBattleRecommendationBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class GetClassBattleRecommendationsUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(
        difficulty: String,
        categories: List<String>,
        keyword: String?
    ): Result<List<ClassBattleRecommendationBoard>> =
        classroomRepository.getClassBattleRecommendations(difficulty, categories, keyword)
}