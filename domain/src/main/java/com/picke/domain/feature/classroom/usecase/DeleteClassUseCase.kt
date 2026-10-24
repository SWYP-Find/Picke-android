package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.repository.ClassroomRepository

class DeleteClassUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(classId: Long): Result<Unit> =
        classroomRepository.deleteClass(classId)
}