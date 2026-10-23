package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.ClassBattleDetailBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class GetClassBattleUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(classId: Long): Result<ClassBattleDetailBoard> =
        classroomRepository.getClassBattle(classId)
}