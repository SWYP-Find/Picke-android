package com.picke.presentation.ui.perspective.model

import com.picke.domain.feature.classroom.model.ClassDetailBoard
import com.picke.presentation.ui.classroom.model.ClassBattleType

data class PerspectiveClassSettingUiModel(
    val isAiBattle: Boolean,
    val isCommentRequired: Boolean
)

fun ClassDetailBoard.toUiModel() = PerspectiveClassSettingUiModel(
    isAiBattle = ClassBattleType.from(battleType) == ClassBattleType.CUSTOM,
    isCommentRequired = commentRequired
)