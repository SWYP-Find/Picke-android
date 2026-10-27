package com.picke.domain.feature.vote.repository

import com.picke.domain.common.model.BattleTarget
import com.picke.domain.feature.vote.model.MyVoteBoard
import com.picke.domain.feature.vote.model.VoteStatsBoard

interface VoteRepository {
    suspend fun submitPreVote(battleId: Long, optionId: Long): Result<Boolean>
    suspend fun submitPostVote(battleId: Long, optionId: Long): Result<Boolean>
    suspend fun getVoteStats(target: BattleTarget): Result<VoteStatsBoard>
    suspend fun getMyVoteHistory(target: BattleTarget): Result<MyVoteBoard>
}