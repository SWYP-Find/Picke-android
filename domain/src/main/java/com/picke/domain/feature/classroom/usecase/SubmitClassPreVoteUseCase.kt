package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.repository.ClassroomRepository

class SubmitClassPreVoteUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(classId: Long, optionId: Long): Result<Unit> =
        classroomRepository.submitClassPreVote(classId, optionId)
}