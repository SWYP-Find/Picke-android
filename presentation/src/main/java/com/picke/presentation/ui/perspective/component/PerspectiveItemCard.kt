package com.picke.presentation.ui.perspective.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.component.PickeBadge
import com.picke.presentation.ui.component.PickeCommentCard
import com.picke.presentation.ui.component.PickeMoreMenuItem
import com.picke.presentation.ui.perspective.model.PerspectiveStatus
import com.picke.presentation.ui.perspective.model.PerspectiveUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun PerspectiveItemCard(
    item: PerspectiveUiModel,
    modifier: Modifier = Modifier,
    isDetail: Boolean = false,
    clickable: Boolean = true,
    onMoreClick: () -> Unit = {},
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onReportClick: () -> Unit = {}
) {
    val status = item.status
    val isReviewing = status != PerspectiveStatus.PUBLISHED
    // TODO: 토큰 동기화 - color/surface/white, 거절 상태 배경 (Figma 디자인 없음)
    val backgroundColor = when (status) {
        PerspectiveStatus.PUBLISHED -> PickeTheme.colors.surfaceBeigeDefault
        PerspectiveStatus.PENDING -> PickeTheme.colors.secondary50
        PerspectiveStatus.REJECTED -> Color(0xFFFFF9F9)
    }
    val canOpenDetail = clickable && !isDetail

    PickeCommentCard(
        profileImageUrl = item.profileImageUrl,
        nickname = if (item.isMine) stringResource(R.string.comment_me) else item.nickname,
        timeAgo = item.timeAgo,
        content = item.content,
        modifier = modifier,
        backgroundColor = backgroundColor,
        isContentExpanded = isDetail,
        likeCount = item.likeCount.takeIf { !isReviewing },
        isLiked = item.isLiked,
        replyCount = item.replyCount.takeIf { !isReviewing && !isDetail },
        menuItems = if (status == PerspectiveStatus.PENDING) {
            emptyList()
        } else {
            perspectiveMenuItems(
                isMine = item.isMine,
                isRejected = status == PerspectiveStatus.REJECTED,
                onEditClick = { onEditClick(item.content) },
                onDeleteClick = onDeleteClick,
                onReportClick = onReportClick
            )
        },
        onClick = onMoreClick.takeIf { canOpenDetail },
        onReadMoreClick = onMoreClick.takeIf { canOpenDetail && !isReviewing },
        onLikeClick = onLikeClick,
        onReplyClick = onMoreClick
    ) {
        if (isReviewing) {
            PerspectiveStatusBadge(status = status)
        } else {
            PickeBadge(
                text = item.optionTitle,
                horizontalPadding = SpacingTokens.s4
            )
        }
    }
}

@Composable
private fun PerspectiveStatusBadge(
    status: PerspectiveStatus,
    modifier: Modifier = Modifier
) {
    // TODO: 토큰 동기화 - 거절 상태 배지 (Figma 디자인 없음)
    val badgeColor = if (status == PerspectiveStatus.REJECTED) Color(0xFFA64D47) else PickeTheme.colors.secondary500

    Text(
        text = stringResource(status.reviewLabelRes()),
        modifier = modifier
            .background(badgeColor, RoundedCornerShape(RadiusTokens.default))
            .padding(
                horizontal = ComponentNumberTokens.paddingBadgeHorizontal,
                vertical = ComponentNumberTokens.paddingBadgeVertical
            ),
        color = PickeTheme.colors.textInverse,
        style = PickeTheme.typography.bodyXxsMedium
    )
}

private fun perspectiveMenuItems(
    isMine: Boolean,
    isRejected: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit
): List<PickeMoreMenuItem> {
    val editItem = PickeMoreMenuItem(
        iconRes = R.drawable.ic_edit,
        textRes = R.string.comment_menu_edit,
        onClick = onEditClick
    )
    val deleteItem = PickeMoreMenuItem(
        iconRes = R.drawable.ic_trash,
        textRes = R.string.comment_menu_delete,
        onClick = onDeleteClick
    )
    val reportItem = PickeMoreMenuItem(
        iconRes = R.drawable.ic_bell,
        textRes = R.string.comment_menu_report,
        onClick = onReportClick
    )
    return when {
        !isMine -> listOf(reportItem)
        isRejected -> listOf(editItem)
        else -> listOf(deleteItem, editItem)
    }
}

@StringRes
private fun PerspectiveStatus.reviewLabelRes(): Int =
    if (this == PerspectiveStatus.REJECTED) R.string.comment_status_rejected else R.string.comment_status_pending

@Preview(showBackground = true)
@Composable
private fun PerspectiveItemCardPreview() {
    PickeTheme {
        Column {
            PerspectiveItemCard(item = DummyData.dummyPerspectives.first())
            PerspectiveItemCard(item = DummyData.dummyPerspectives.last().copy(status = PerspectiveStatus.PENDING))
            PerspectiveItemCard(item = DummyData.dummyPerspectives.last().copy(status = PerspectiveStatus.REJECTED))
        }
    }
}