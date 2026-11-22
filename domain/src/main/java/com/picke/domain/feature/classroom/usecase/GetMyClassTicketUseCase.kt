package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassTicketBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class GetMyClassTicketUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(): Result<ClassTicketBoard> = classroomRepository.getMyClassTicket()
}