package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassJoinPreviewBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class PreviewClassJoinUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(inviteCode: String): Result<ClassJoinPreviewBoard> =
        classroomRepository.previewClassJoin(inviteCode)
}