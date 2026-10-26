package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassTopicDraftBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class SuggestClassTopicsUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(draftId: Long): Result<ClassTopicDraftBoard> =
        classroomRepository.suggestClassTopics(draftId)
}