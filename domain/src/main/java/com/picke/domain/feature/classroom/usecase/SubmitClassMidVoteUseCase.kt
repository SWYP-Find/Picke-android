package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.repository.ClassroomRepository

class SubmitClassMidVoteUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(classId: Long, checkpointId: Long, optionId: Long): Result<Unit> =
        classroomRepository.submitClassMidVote(
            classId = classId,
            checkpointId = checkpointId,
            optionId = optionId
        )
}