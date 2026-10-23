package com.picke.presentation.ui.classroom.classmember.model

data class ClassMemberUiState(
    val classId: Long = 0L,
    val isOperatorViewer: Boolean = false,
    val query: String = "",
    val members: List<ClassMemberUiModel> = emptyList(),
    val isNameSheetVisible: Boolean = false,
    val editingName: String = "",
    val removingMember: ClassMemberUiModel? = null,
    val isRequesting: Boolean = false,
    val isMembersChanged: Boolean = false
) {
    val filteredMembers: List<ClassMemberUiModel>
        get() = members
            .sortedWith(compareByDescending<ClassMemberUiModel> { it.isMe }.thenByDescending { it.isOperator })
            .filter { query.isBlank() || it.name.contains(query.trim()) }

    val isNameConfirmEnabled: Boolean
        get() = !isRequesting && editingName.isNotBlank()

    internal val isRemoveConfirmEnabled: Boolean
        get() = !isRequesting && removingMember != null

    internal val myName: String
        get() = members.firstOrNull { it.isMe }?.name.orEmpty()
}