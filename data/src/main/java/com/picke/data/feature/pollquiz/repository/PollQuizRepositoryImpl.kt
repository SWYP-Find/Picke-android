package com.picke.data.feature.pollquiz.repository

import com.picke.data.common.model.toResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.pollquiz.datasource.PollQuizApi
import com.picke.data.feature.pollquiz.model.VoteRequestDto
import com.picke.data.feature.pollquiz.model.toDomainModel
import com.picke.domain.feature.pollquiz.model.PollQuizVoteBoard
import com.picke.domain.feature.pollquiz.repository.PollQuizRepository
import javax.inject.Inject

class PollQuizRepositoryImpl @Inject constructor(
    private val pollQuizApi: PollQuizApi
) : PollQuizRepository {

    override suspend fun submitPollVote(battleId: Long, optionId: Long): Result<PollQuizVoteBoard> = apiCall {
        pollQuizApi.submitPollVote(battleId, VoteRequestDto(optionId))
            .toResult("투표를 제출하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getMyPollVote(battleId: Long): Result<PollQuizVoteBoard> = apiCall {
        pollQuizApi.getMyPollVote(battleId)
            .toResult("내 투표 내역을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun submitQuizVote(battleId: Long, optionId: Long): Result<PollQuizVoteBoard> = apiCall {
        pollQuizApi.submitQuizVote(battleId, VoteRequestDto(optionId))
            .toResult("퀴즈를 제출하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getMyQuizVote(battleId: Long): Result<PollQuizVoteBoard> = apiCall {
        pollQuizApi.getMyQuizVote(battleId)
            .toResult("내 퀴즈 내역을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }
}