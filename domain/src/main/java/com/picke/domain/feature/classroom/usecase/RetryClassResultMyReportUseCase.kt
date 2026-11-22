package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassResultMyReportBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class RetryClassResultMyReportUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(classId: Long): Result<ClassResultMyReportBoard> =
        classroomRepository.retryClassResultMyReport(classId)
}