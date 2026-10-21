package com.picke.presentation.ui.comment.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.comment.model.CommentUiModel
import com.picke.presentation.ui.component.PickeBadge
import com.picke.presentation.ui.component.PickeCommentCard
import com.picke.presentation.ui.component.PickeMoreMenuItem
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun CommentItemCard(
    item: CommentUiModel,
    modifier: Modifier = Modifier,
    isMainContent: Boolean = false,
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onReportClick: () -> Unit = {}
) {
    PickeCommentCard(
        profileImageUrl = item.profileImageUrl,
        nickname = if (item.isMine) stringResource(R.string.comment_me) else item.nickname,
        timeAgo = item.timeAgo,
        content = item.content,
        modifier = modifier,
        contentColor = PickeTheme.colors.textBody,
        isContentExpanded = true,
        likeCount = item.likeCount,
        isLiked = item.isLiked,
        replyCount = item.replyCount.takeIf { isMainContent },
        menuItems = commentMenuItems(
            isMine = item.isMine,
            onEditClick = { onEditClick(item.content) },
            onDeleteClick = onDeleteClick,
            onReportClick = onReportClick
        ),
        onLikeClick = onLikeClick
    ) {
        PickeBadge(
            text = item.stance,
            horizontalPadding = SpacingTokens.s4
        )
    }
}

private fun commentMenuItems(
    isMine: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit
): List<PickeMoreMenuItem> = if (isMine) {
    listOf(
        PickeMoreMenuItem(
            iconRes = R.drawable.ic_trash,
            textRes = R.string.comment_menu_delete,
            onClick = onDeleteClick
        ),
        PickeMoreMenuItem(
            iconRes = R.drawable.ic_edit,
            textRes = R.string.comment_menu_edit,
            onClick = onEditClick
        )
    )
} else {
    listOf(
        PickeMoreMenuItem(
            iconRes = R.drawable.ic_bell,
            textRes = R.string.comment_menu_report,
            onClick = onReportClick
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun CommentItemCardPreview() {
    PickeTheme {
        Column {
            CommentItemCard(
                item = DummyData.dummyMainPerspective,
                isMainContent = true
            )
            CommentItemCard(item = DummyData.dummyComments.first())
            CommentItemCard(item = DummyData.dummyComments.last())
        }
    }
}