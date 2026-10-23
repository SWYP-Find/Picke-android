package com.picke.data.feature.classroom.datasource

import com.picke.data.common.model.BaseResponse
import com.picke.data.feature.classroom.model.ClassBattleDetailResponseDto
import com.picke.data.feature.classroom.model.ClassCreateRequestDto
import com.picke.data.feature.classroom.model.ClassDetailResponseDto
import com.picke.data.feature.classroom.model.ClassTicketPreviewResponseDto
import com.picke.data.feature.classroom.model.ClassTicketRegisterRequestDto
import com.picke.data.feature.classroom.model.ClassTicketResponseDto
import com.picke.data.feature.classroom.model.MyClassResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ClassroomApi {

    @GET("/api/v1/class-tickets/me")
    suspend fun getMyClassTicket(): BaseResponse<ClassTicketResponseDto>

    @GET("/api/v1/class-tickets/preview")
    suspend fun previewClassTicket(
        @Query("code") code: String
    ): BaseResponse<ClassTicketPreviewResponseDto>

    @POST("/api/v1/class-tickets/register")
    suspend fun registerClassTicket(
        @Body request: ClassTicketRegisterRequestDto
    ): BaseResponse<ClassTicketResponseDto>

    @GET("/api/v1/classes")
    suspend fun getMyClasses(): BaseResponse<List<MyClassResponseDto>>

    @POST("/api/v1/classes")
    suspend fun createClass(
        @Body request: ClassCreateRequestDto
    ): BaseResponse<ClassDetailResponseDto>

    @GET("/api/v1/classes/{classId}")
    suspend fun getClassDetail(
        @Path("classId") classId: Long
    ): BaseResponse<ClassDetailResponseDto>

    @GET("/api/v1/classes/{classId}/battle")
    suspend fun getClassBattle(
        @Path("classId") classId: Long
    ): BaseResponse<ClassBattleDetailResponseDto>
}