package com.picke.data.feature.vote.repository

import com.picke.data.feature.vote.model.VoteRequestDto
import com.picke.data.feature.vote.model.toDomainModel
import com.picke.data.common.model.toResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.vote.datasource.VoteApi
import com.picke.domain.common.model.BattleTarget
import com.picke.domain.feature.vote.model.MyVoteBoard
import com.picke.domain.feature.vote.model.VoteStatsBoard
import com.picke.domain.feature.vote.repository.VoteRepository
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val voteApi: VoteApi
) : VoteRepository {

    override suspend fun getVoteStats(target: BattleTarget): Result<VoteStatsBoard> = apiCall {
        when (target) {
            is BattleTarget.General -> voteApi.getVoteStats(target.battleId)
            is BattleTarget.Class -> voteApi.getClassVoteStats(target.classId)
        }
            .toResult("투표 통계를 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getMyVoteHistory(target: BattleTarget): Result<MyVoteBoard> = apiCall {
        when (target) {
            is BattleTarget.General -> voteApi.getMyVoteHistory(target.battleId)
            is BattleTarget.Class -> voteApi.getClassMyVoteHistory(target.classId)
        }
            .toResult("내 투표 내역을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun submitPreVote(battleId: Long, optionId: Long): Result<Boolean> = apiCall {
        voteApi.submitPreVote(battleId, VoteRequestDto(optionId))
            .toResult("사전 투표를 하지 못했습니다.")
            .map { true }
    }

    override suspend fun submitPostVote(battleId: Long, optionId: Long): Result<Boolean> = apiCall {
        voteApi.submitPostVote(battleId, VoteRequestDto(optionId))
            .toResult("최종 투표를 하지 못했습니다.")
            .map { true }
    }
}