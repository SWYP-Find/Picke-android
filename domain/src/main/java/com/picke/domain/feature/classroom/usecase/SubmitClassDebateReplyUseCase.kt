package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassDebateBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class SubmitClassDebateReplyUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(
        classId: Long,
        roundIndex: Int,
        content: String,
        requestKey: String
    ): Result<ClassDebateBoard> = classroomRepository.submitClassDebateReply(
        classId = classId,
        roundIndex = roundIndex,
        content = content,
        requestKey = requestKey
    )
}