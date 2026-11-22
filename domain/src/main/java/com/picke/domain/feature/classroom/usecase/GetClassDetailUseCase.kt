package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassDetailBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class GetClassDetailUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(classId: Long): Result<ClassDetailBoard> =
        classroomRepository.getClassDetail(classId)
}