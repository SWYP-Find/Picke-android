package com.picke.domain.feature.vote.usecase

import com.picke.domain.common.model.BattleTarget
import com.picke.domain.feature.vote.model.MyVoteBoard
import com.picke.domain.feature.vote.repository.VoteRepository

class GetMyVoteHistoryUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(target: BattleTarget): Result<MyVoteBoard> {
        return voteRepository.getMyVoteHistory(target)
    }
}