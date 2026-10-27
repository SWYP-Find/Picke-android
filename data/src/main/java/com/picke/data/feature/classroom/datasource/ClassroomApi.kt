package com.picke.data.feature.classroom.datasource

import com.picke.data.common.model.BaseResponse
import com.picke.data.feature.classroom.model.ClassBattleDetailResponseDto
import com.picke.data.feature.classroom.model.ClassBattleRecommendationResponseDto
import com.picke.data.feature.classroom.model.ClassCreateRequestDto
import com.picke.data.feature.classroom.model.ClassDetailResponseDto
import com.picke.data.feature.classroom.model.ClassJoinPreviewResponseDto
import com.picke.data.feature.classroom.model.ClassJoinRequestDto
import com.picke.data.feature.classroom.model.ClassMemberNameUpdateRequestDto
import com.picke.data.feature.classroom.model.ClassMemberResponseDto
import com.picke.data.feature.classroom.model.ClassTicketPreviewResponseDto
import com.picke.data.feature.classroom.model.ClassTicketRegisterRequestDto
import com.picke.data.feature.classroom.model.ClassTicketResponseDto
import com.picke.data.feature.classroom.model.ClassTopicDraftRequestDto
import com.picke.data.feature.classroom.model.ClassTopicDraftResponseDto
import com.picke.data.feature.classroom.model.ClassTopicSelectionRequestDto
import com.picke.data.feature.classroom.model.ClassTopicVersionResponseDto
import com.picke.data.feature.classroom.model.ClassUpdateRequestDto
import com.picke.data.feature.classroom.model.MyClassResponseDto
import com.picke.data.feature.pollquiz.model.VoteRequestDto
import com.picke.data.feature.vote.model.VoteResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
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

    @GET("/api/v1/classes/{classId}")
    suspend fun getClassDetail(
        @Path("classId") classId: Long
    ): BaseResponse<ClassDetailResponseDto>

    @GET("/api/v1/classes/{classId}/battle")
    suspend fun getClassBattle(
        @Path("classId") classId: Long
    ): BaseResponse<ClassBattleDetailResponseDto>

    @POST("/api/v1/classes")
    suspend fun createClass(
        @Body request: ClassCreateRequestDto
    ): BaseResponse<ClassDetailResponseDto>

    @PATCH("/api/v1/classes/{classId}")
    suspend fun updateClass(
        @Path("classId") classId: Long,
        @Body request: ClassUpdateRequestDto
    ): BaseResponse<ClassDetailResponseDto>

    @DELETE("/api/v1/classes/{classId}")
    suspend fun deleteClass(
        @Path("classId") classId: Long
    ): BaseResponse<Unit>

    @GET("/api/v1/class-battle-recommendations")
    suspend fun getClassBattleRecommendations(
        @Query("difficulty") difficulty: String,
        @Query("categories") categories: List<String>,
        @Query("keyword") keyword: String?
    ): BaseResponse<ClassBattleRecommendationResponseDto>

    @POST("/api/v1/class-topic-drafts")
    suspend fun createClassTopicDraft(
        @Body request: ClassTopicDraftRequestDto
    ): BaseResponse<ClassTopicDraftResponseDto>

    @POST("/api/v1/class-topic-drafts/{draftId}/suggestions")
    suspend fun suggestClassTopics(
        @Path("draftId") draftId: Long
    ): BaseResponse<ClassTopicDraftResponseDto>

    @POST("/api/v1/class-topic-drafts/{draftId}/selection")
    suspend fun selectClassTopicCandidate(
        @Path("draftId") draftId: Long,
        @Body request: ClassTopicSelectionRequestDto
    ): BaseResponse<ClassTopicVersionResponseDto>

    @GET("/api/v1/classes/join/preview")
    suspend fun previewClassJoin(
        @Query("inviteCode") inviteCode: String
    ): BaseResponse<ClassJoinPreviewResponseDto>

    @POST("/api/v1/classes/join")
    suspend fun joinClass(
        @Body request: ClassJoinRequestDto
    ): BaseResponse<ClassDetailResponseDto>

    @GET("/api/v1/classes/{classId}/members")
    suspend fun getClassMembers(
        @Path("classId") classId: Long
    ): BaseResponse<List<ClassMemberResponseDto>>

    @PATCH("/api/v1/classes/{classId}/members/me")
    suspend fun updateMyDisplayName(
        @Path("classId") classId: Long,
        @Body request: ClassMemberNameUpdateRequestDto
    ): BaseResponse<ClassMemberResponseDto>

    @DELETE("/api/v1/classes/{classId}/members/me")
    suspend fun leaveClass(
        @Path("classId") classId: Long
    ): BaseResponse<Unit>

    @DELETE("/api/v1/classes/{classId}/members/{memberId}")
    suspend fun removeMember(
        @Path("classId") classId: Long,
        @Path("memberId") memberId: Long
    ): BaseResponse<Unit>

    @POST("/api/v1/classes/{classId}/battle/votes/pre")
    suspend fun submitClassPreVote(
        @Path("classId") classId: Long,
        @Body request: VoteRequestDto
    ): BaseResponse<VoteResponseDto>

    @POST("/api/v1/classes/{classId}/battle/votes/post")
    suspend fun submitClassPostVote(
        @Path("classId") classId: Long,
        @Body request: VoteRequestDto
    ): BaseResponse<VoteResponseDto>

    @POST("/api/v1/classes/{classId}/battle/votes/tts-complete")
    suspend fun completeClassTts(
        @Path("classId") classId: Long
    ): BaseResponse<Unit>
}