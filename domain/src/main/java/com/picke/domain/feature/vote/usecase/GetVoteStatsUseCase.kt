package com.picke.domain.feature.vote.usecase

import com.picke.domain.common.model.BattleTarget
import com.picke.domain.feature.vote.model.VoteStatsBoard
import com.picke.domain.feature.vote.repository.VoteRepository

class GetVoteStatsUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(target: BattleTarget): Result<VoteStatsBoard> {
        return voteRepository.getVoteStats(target)
    }
}