package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassResultBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class GetClassResultUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(classId: Long): Result<ClassResultBoard> =
        classroomRepository.getClassResult(classId)
}