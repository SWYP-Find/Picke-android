package com.picke.presentation.ui.classroom.classvote.model

import com.picke.presentation.ui.vote.model.BattleDetailUiModel
import com.picke.presentation.ui.vote.model.VoteType
import java.time.LocalDateTime

data class ClassVoteUiState(
    val classId: Long = 0L,
    val voteType: VoteType = VoteType.PRE,
    val className: String = "",
    val deadline: LocalDateTime? = null,
    val battleId: Long = 0L,
    val isAiBattle: Boolean = false,
    val commentCount: Int = 0,
    val battle: BattleDetailUiModel? = null
)