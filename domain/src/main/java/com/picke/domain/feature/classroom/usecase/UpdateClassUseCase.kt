package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassDetailBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class UpdateClassUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(
        classId: Long,
        name: String?,
        deadline: String?
    ): Result<ClassDetailBoard> = classroomRepository.updateClass(classId, name, deadline)
}