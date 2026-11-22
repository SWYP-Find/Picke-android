package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassMemberBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class GetClassMembersUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(classId: Long): Result<List<ClassMemberBoard>> =
        classroomRepository.getClassMembers(classId)
}