package com.picke.data.feature.classroom.repository

import com.picke.data.common.model.toResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.classroom.datasource.ClassroomApi
import com.picke.data.feature.classroom.model.toDomainModel
import com.picke.domain.feature.classroom.model.ClassTicketBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository
import javax.inject.Inject

class ClassroomRepositoryImpl @Inject constructor(
    private val classroomApi: ClassroomApi
) : ClassroomRepository {

    override suspend fun getMyClassTicket(): Result<ClassTicketBoard> = apiCall {
        classroomApi.getMyClassTicket()
            .toResult("내 이용권을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }
}