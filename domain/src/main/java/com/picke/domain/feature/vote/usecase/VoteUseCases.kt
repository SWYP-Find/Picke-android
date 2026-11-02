package com.picke.domain.feature.vote.usecase

data class VoteUseCases(
    val getVoteStatsUseCase: GetVoteStatsUseCase,
    val getMyVoteHistoryUseCase: GetMyVoteHistoryUseCase,
    val submitVoteUseCase: SubmitVoteUseCase
)