package com.picke.presentation.ui.classroom.classdebate.model

data class ClassDebateUiState(
    val classId: Long = 0L,
    val battleTitle: String = "",
    val debate: ClassDebateUiModel = ClassDebateUiModel(),
    val input: String = "",
    val replyRequestKey: String? = null,
    val isRequesting: Boolean = false,
    val isFinalVoteDialogVisible: Boolean = false
) {
    val isFinished: Boolean
        get() = debate.status == ClassDebateStatus.COMPLETED

    val displayRound: Int
        get() = debate.round.coerceIn(1, TOTAL_ROUNDS)

    val isInputEnabled: Boolean
        get() = debate.canReply && !isRequesting

    val isSendEnabled: Boolean
        get() = input.isNotBlank() && isInputEnabled

    internal val isReplyLengthEnough: Boolean
        get() = input.trim().length >= debate.replyMinLength

    internal fun isReplyLengthWithinMax(text: String): Boolean =
        debate.replyMaxLength <= 0 || text.length <= debate.replyMaxLength

    companion object {
        const val TOTAL_ROUNDS = 3
    }
}