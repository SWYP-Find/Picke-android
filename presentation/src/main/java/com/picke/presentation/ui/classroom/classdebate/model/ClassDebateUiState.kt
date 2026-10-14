package com.picke.presentation.ui.classroom.classdebate.model

data class ClassDebateUiState(
    val classId: Long = 0L,
    val battleId: Long = 0L,
    val battleTitle: String = "",
    val myChoice: String = "",
    val opponentName: String = "",
    val opponentImageUrl: String? = null,
    val currentRound: Int = 1,
    val messages: List<ClassDebateMessageUiModel> = emptyList(),
    val quickReplies: List<String> = emptyList(),
    val input: String = "",
    val isReplying: Boolean = false
) {
    val isFinished: Boolean
        get() = currentRound > TOTAL_ROUNDS

    val displayRound: Int
        get() = currentRound.coerceAtMost(TOTAL_ROUNDS)

    val isSendEnabled: Boolean
        get() = input.isNotBlank() && !isReplying && !isFinished

    internal val nextMessageId: Long
        get() = (messages.maxOfOrNull { it.messageId } ?: 0L) + 1

    companion object {
        const val TOTAL_ROUNDS = 3
    }
}
