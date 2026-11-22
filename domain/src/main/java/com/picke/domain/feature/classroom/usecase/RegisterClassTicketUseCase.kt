package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassTicketBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class RegisterClassTicketUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(code: String): Result<ClassTicketBoard> =
        classroomRepository.registerClassTicket(code)
}