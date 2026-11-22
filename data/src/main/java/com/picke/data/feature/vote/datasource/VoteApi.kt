package com.picke.data.feature.vote.datasource

import com.picke.data.common.model.BaseResponse
import com.picke.data.feature.vote.model.MyVoteResponseDto
import com.picke.data.feature.vote.model.VoteRequestDto
import com.picke.data.feature.vote.model.VoteResponseDto
import com.picke.data.feature.vote.model.VoteStatsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface VoteApi {
    // 1. 투표 통계 조회
    @GET("/api/v1/battles/{battleId}/vote-stats")
    suspend fun getVoteStats(
        @Path("battleId") battleId: Long
    ): BaseResponse<VoteStatsDto>

    // 2. 내 투표 내역 조회
    @GET("/api/v1/battles/{battleId}/votes/me")
    suspend fun getMyVoteHistory(
        @Path("battleId") battleId: Long
    ): BaseResponse<MyVoteResponseDto>

    // 3. 사전 투표 제출
    @POST("/api/v1/battles/{battleId}/votes/pre")
    suspend fun submitPreVote(
        @Path("battleId") battleId: Long,
        @Body request: VoteRequestDto
    ): BaseResponse<VoteResponseDto>

    // 4. 사후 투표 제출
    @POST("/api/v1/battles/{battleId}/votes/post")
    suspend fun submitPostVote(
        @Path("battleId") battleId: Long,
        @Body request: VoteRequestDto
    ): BaseResponse<VoteResponseDto>

    @GET("/api/v1/classes/{classId}/battle/vote-stats")
    suspend fun getClassVoteStats(
        @Path("classId") classId: Long
    ): BaseResponse<VoteStatsDto>

    @GET("/api/v1/classes/{classId}/battle/votes/me")
    suspend fun getClassMyVoteHistory(
        @Path("classId") classId: Long
    ): BaseResponse<MyVoteResponseDto>
}