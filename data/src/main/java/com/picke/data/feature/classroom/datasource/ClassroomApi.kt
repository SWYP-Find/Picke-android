package com.picke.data.feature.classroom.datasource

import com.picke.data.common.model.BaseResponse
import com.picke.data.feature.classroom.model.ClassTicketResponseDto
import retrofit2.http.GET

interface ClassroomApi {

    @GET("/api/v1/class-tickets/me")
    suspend fun getMyClassTicket(): BaseResponse<ClassTicketResponseDto>
}