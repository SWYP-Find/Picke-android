package com.picke.presentation.ui.classroom.classdebate.model

data class ClassDebateUiState(
    val classId: Long = 0L,
    val battleTitle: String = "",
    val debate: ClassDebateUiModel = ClassDebateUiModel(),
    val input: String = "",
    val isReplying: Boolean = false,
    val isFinalVoteDialogVisible: Boolean = false
) {
    val isFinished: Boolean
        get() = debate.status == ClassDebateStatus.COMPLETED

    val displayRound: Int
        get() = debate.round.coerceIn(1, TOTAL_ROUNDS)

    val isInputEnabled: Boolean
        get() = debate.canReply && !isReplying

    val isSendEnabled: Boolean
        get() = input.isNotBlank() && isInputEnabled

    internal val nextMessageId: Long
        get() = (debate.messages.maxOfOrNull { it.messageId } ?: 0L) + 1

    companion object {
        const val TOTAL_ROUNDS = 3
    }
}