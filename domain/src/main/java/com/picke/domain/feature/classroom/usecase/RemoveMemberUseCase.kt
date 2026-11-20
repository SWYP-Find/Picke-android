package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.repository.ClassroomRepository

class RemoveMemberUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(classId: Long, memberId: Long): Result<Unit> =
        classroomRepository.removeMember(classId, memberId)
}