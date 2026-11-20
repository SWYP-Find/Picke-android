package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassResultMyReportBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class GetClassResultMyReportUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(classId: Long): Result<ClassResultMyReportBoard> =
        classroomRepository.getClassResultMyReport(classId)
}