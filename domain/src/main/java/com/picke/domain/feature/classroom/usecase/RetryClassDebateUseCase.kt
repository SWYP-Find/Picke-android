package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassDebateBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class RetryClassDebateUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(classId: Long): Result<ClassDebateBoard> =
        classroomRepository.retryClassDebate(classId)
}