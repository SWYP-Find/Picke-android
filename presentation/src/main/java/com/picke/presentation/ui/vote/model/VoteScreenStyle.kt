package com.picke.presentation.ui.vote.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme

internal data class VoteScreenStyle(
    @param:StringRes val submitTextRes: Int,
    val containerColor: Color,
    val titleColor: Color,
    val descriptionColor: Color,
    val topBarTitle: String?,
    val isShareVisible: Boolean,
    val isOptionImageVisible: Boolean,
    val commentCount: Int?,
    val deadlineText: String?
)

@Composable
internal fun voteScreenStyle(voteType: VoteType, classInfo: VoteClassInfoUiModel?): VoteScreenStyle {
    val isPreVote = voteType == VoteType.PRE
    val isGeneralBattle = classInfo == null
    val submitTextRes = when {
        !isGeneralBattle -> R.string.vote_submit
        isPreVote -> R.string.prevote
        else -> R.string.postvote
    }

    return VoteScreenStyle(
        submitTextRes = submitTextRes,
        containerColor = if (isPreVote) PickeTheme.colors.surfaceBeigeDefault else Color.Black,
        titleColor = if (isPreVote) PickeTheme.colors.textDefault else PickeTheme.colors.surfaceBeigeDefault,
        descriptionColor = if (isPreVote) PickeTheme.colors.textSubtle else PickeTheme.colors.gray400,
        topBarTitle = classInfo?.className,
        isShareVisible = isGeneralBattle,
        isOptionImageVisible = classInfo?.isAiBattle != true,
        commentCount = if (isPreVote) classInfo?.commentCount else null,
        deadlineText = classInfo?.deadlineText
    )
}