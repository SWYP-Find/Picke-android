package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassTicketPreviewBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class PreviewClassTicketUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(code: String): Result<ClassTicketPreviewBoard> =
        classroomRepository.previewClassTicket(code)
}