package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassResultInsightBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class RetryClassResultInsightUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(classId: Long): Result<ClassResultInsightBoard> =
        classroomRepository.retryClassResultInsight(classId)
}