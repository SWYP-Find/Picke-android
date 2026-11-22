package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassDebateBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class GetClassDebateUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(classId: Long): Result<ClassDebateBoard> =
        classroomRepository.getClassDebate(classId)
}