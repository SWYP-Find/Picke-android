package com.picke.domain.feature.classroom.usecase

import com.picke.domain.feature.classroom.model.MyClassBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository

class GetMyClassesUseCase(
    private val classroomRepository: ClassroomRepository
) {
    suspend operator fun invoke(): Result<List<MyClassBoard>> = classroomRepository.getMyClasses()
}