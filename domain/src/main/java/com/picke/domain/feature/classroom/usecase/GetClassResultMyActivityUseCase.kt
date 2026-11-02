package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassResultMyActivityBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class GetClassResultMyActivityUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(
        classId: Long,
        cursor: String? = null
    ): Result<ClassResultMyActivityBoard> =
        classroomRepository.getClassResultMyActivity(classId, cursor)
}