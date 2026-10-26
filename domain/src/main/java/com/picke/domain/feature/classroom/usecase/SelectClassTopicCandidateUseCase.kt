package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassTopicVersionBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class SelectClassTopicCandidateUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(
        draftId: Long,
        batchNo: Int,
        candidateId: Long
    ): Result<ClassTopicVersionBoard> =
        classroomRepository.selectClassTopicCandidate(draftId, batchNo, candidateId)
}