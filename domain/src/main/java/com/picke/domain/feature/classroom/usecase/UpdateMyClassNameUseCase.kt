package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassMemberBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class UpdateMyClassNameUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(classId: Long, displayName: String): Result<ClassMemberBoard> =
        classroomRepository.updateMyDisplayName(classId, displayName)
}