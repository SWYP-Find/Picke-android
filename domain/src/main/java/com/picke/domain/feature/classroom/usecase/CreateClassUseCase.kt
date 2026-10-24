package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassDetailBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class CreateClassUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(
        name: String,
        battleId: Long,
        deadline: String?,
        commentRequired: Boolean,
        ownerParticipates: Boolean
    ): Result<ClassDetailBoard> =
        classroomRepository.createClass(name, battleId, deadline, commentRequired, ownerParticipates)
}