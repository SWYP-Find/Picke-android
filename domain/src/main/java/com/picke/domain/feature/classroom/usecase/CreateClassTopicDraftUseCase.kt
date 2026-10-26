package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassTopicDraftBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class CreateClassTopicDraftUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(
        topicSeed: String,
        difficulty: String,
        categories: List<String>
    ): Result<ClassTopicDraftBoard> =
        classroomRepository.createClassTopicDraft(topicSeed, difficulty, categories)
}