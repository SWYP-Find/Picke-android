package com.picke.presentation.ui.perspective.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.component.PickeBadge
import com.picke.presentation.ui.component.PickeIconCount
import com.picke.presentation.ui.component.PickeMoreMenu
import com.picke.presentation.ui.component.PickeMoreMenuItem
import com.picke.presentation.ui.component.ProfileImage
import com.picke.presentation.ui.perspective.model.PerspectiveStatus
import com.picke.presentation.ui.perspective.model.PerspectiveUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

private const val CONTENT_MAX_LINES = 3

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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(enabled = clickable && !isDetail) { onMoreClick() }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s12),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TODO: 토큰 동기화 - color/surface/beige-darker
                ProfileImage(
                    model = item.profileImageUrl,
                    size = ComponentNumberTokens.avatarSm,
                    backgroundColor = PickeTheme.colors.avatarBackround
                )

                Column(modifier = Modifier.weight(1f)) {
                    // TODO: 토큰 동기화 - color/text/basic
                    Text(
                        text = if (item.isMine) stringResource(R.string.comment_me) else item.nickname,
                        color = PickeTheme.colors.textDefault,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = PickeTheme.typography.bodyXxsSemiBold
                    )
                    Text(
                        text = item.timeAgo,
                        color = PickeTheme.colors.textMuted,
                        maxLines = 1,
                        style = PickeTheme.typography.captionSmSemiBold
                    )
                }

                if (isReviewing) {
                    PerspectiveStatusBadge(status = status)
                } else {
                    PickeBadge(
                        text = item.optionTitle,
                        horizontalPadding = SpacingTokens.s4
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4)) {
                Text(
                    text = item.content,
                    modifier = Modifier.padding(horizontal = SpacingTokens.s2),
                    color = PickeTheme.colors.textMuted,
                    maxLines = if (isDetail) Int.MAX_VALUE else CONTENT_MAX_LINES,
                    overflow = TextOverflow.Ellipsis,
                    style = PickeTheme.typography.bodyXsRegular
                )

                if (!isDetail && !isReviewing) {
                    // TODO: 토큰 동기화 - color/text/faint
                    Text(
                        text = stringResource(R.string.comment_read_more),
                        modifier = Modifier
                            .padding(horizontal = SpacingTokens.s2)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                enabled = clickable
                            ) { onMoreClick() },
                        color = PickeTheme.colors.gray200,
                        style = PickeTheme.typography.captionLgMedium
                    )
                }
            }

            if (status != PerspectiveStatus.PENDING) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isReviewing) {
                        PickeIconCount(
                            iconRes = R.drawable.ic_heart_plus,
                            contentDescription = stringResource(R.string.comment_like),
                            count = item.likeCount,
                            contentColor = if (item.isLiked) PickeTheme.colors.primary500 else PickeTheme.colors.textMuted,
                            onClick = onLikeClick
                        )
                    }

                    if (!isReviewing && !isDetail) {
                        PickeIconCount(
                            iconRes = R.drawable.ic_message,
                            contentDescription = stringResource(R.string.comment_reply),
                            count = item.replyCount,
                            onClick = onMoreClick
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    PickeMoreMenu(
                        items = perspectiveMenuItems(
                            isMine = item.isMine,
                            isRejected = status == PerspectiveStatus.REJECTED,
                            onEditClick = { onEditClick(item.content) },
                            onDeleteClick = onDeleteClick,
                            onReportClick = onReportClick
                        )
                    )
                }
            }
        }

        // TODO: 토큰 동기화 - color/divider/beige
        HorizontalDivider(
            thickness = ComponentNumberTokens.borderWidthRegular,
            color = PickeTheme.colors.borderBeigeDefault
        )
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