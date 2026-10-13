package com.picke.presentation.ui.classroom.classvote.model

import com.picke.presentation.ui.vote.model.BattleDetailUiModel
import java.time.LocalDateTime

internal sealed interface ClassVoteMutation {
    data class ClassVoteLoaded(
        val className: String,
        val deadline: LocalDateTime?,
        val battleId: Long,
        val isAiBattle: Boolean,
        val commentCount: Int,
        val battle: BattleDetailUiModel
    ) : ClassVoteMutation
}