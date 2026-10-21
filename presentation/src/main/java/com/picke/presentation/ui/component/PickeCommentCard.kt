package com.picke.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private const val CONTENT_MAX_LINES = 3

@Composable
fun PickeCommentCard(
    profileImageUrl: String,
    nickname: String,
    timeAgo: String,
    content: String,
    modifier: Modifier = Modifier,
    // TODO: 토큰 동기화 - color/surface/white
    backgroundColor: Color = PickeTheme.colors.surfaceBeigeDefault,
    contentColor: Color = PickeTheme.colors.textMuted,
    isContentExpanded: Boolean = false,
    likeCount: Int? = null,
    isLiked: Boolean = false,
    replyCount: Int? = null,
    menuItems: List<PickeMoreMenuItem> = emptyList(),
    onClick: (() -> Unit)? = null,
    onReadMoreClick: (() -> Unit)? = null,
    onLikeClick: () -> Unit = {},
    onReplyClick: () -> Unit = {},
    badge: @Composable () -> Unit = {}
) {
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    val hasActions = likeCount != null || replyCount != null || menuItems.isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .then(clickModifier)
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = SpacingTokens.s16,
                vertical = SpacingTokens.s12
            ),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TODO: 토큰 동기화 - color/surface/beige-darker
                ProfileImage(
                    model = profileImageUrl,
                    size = ComponentNumberTokens.avatarSm,
                    backgroundColor = PickeTheme.colors.avatarBackround
                )

                Column(modifier = Modifier.weight(1f)) {
                    // TODO: 토큰 동기화 - color/text/basic
                    Text(
                        text = nickname,
                        color = PickeTheme.colors.textDefault,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = PickeTheme.typography.bodyXxsSemiBold
                    )
                    Text(
                        text = timeAgo,
                        color = PickeTheme.colors.textMuted,
                        maxLines = 1,
                        style = PickeTheme.typography.captionSmSemiBold
                    )
                }

                badge()
            }

            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4)) {
                Text(
                    text = content,
                    modifier = Modifier.padding(horizontal = SpacingTokens.s2),
                    color = contentColor,
                    maxLines = if (isContentExpanded) Int.MAX_VALUE else CONTENT_MAX_LINES,
                    overflow = TextOverflow.Ellipsis,
                    style = PickeTheme.typography.bodyXsRegular
                )

                if (onReadMoreClick != null) {
                    // TODO: 토큰 동기화 - color/text/faint
                    Text(
                        text = stringResource(R.string.comment_read_more),
                        modifier = Modifier
                            .padding(horizontal = SpacingTokens.s2)
                            .clickable(
                                interactionSource = null,
                                indication = null,
                                onClick = onReadMoreClick
                            ),
                        color = PickeTheme.colors.gray200,
                        style = PickeTheme.typography.captionLgMedium
                    )
                }
            }

            if (hasActions) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (likeCount != null) {
                        PickeIconCount(
                            iconRes = R.drawable.ic_heart_plus,
                            contentDescription = stringResource(R.string.comment_like),
                            count = likeCount,
                            contentColor = if (isLiked) PickeTheme.colors.primary500 else PickeTheme.colors.textMuted,
                            onClick = onLikeClick
                        )
                    }

                    if (replyCount != null) {
                        PickeIconCount(
                            iconRes = R.drawable.ic_message,
                            contentDescription = stringResource(R.string.comment_reply),
                            count = replyCount,
                            onClick = onReplyClick
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (menuItems.isNotEmpty()) {
                        PickeMoreMenu(items = menuItems)
                    }
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

@Preview(showBackground = true)
@Composable
private fun PickeCommentCardPreview() {
    PickeTheme {
        Column {
            PickeCommentCard(
                profileImageUrl = "",
                nickname = "사유하는 라쿤",
                timeAgo = "2분 전",
                content = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠. 제도화가 진행될수록 개인의 판단보다 사회의 기준이 앞서게 됩니다.",
                likeCount = 1340,
                replyCount = 23,
                menuItems = listOf(
                    PickeMoreMenuItem(
                        iconRes = R.drawable.ic_bell,
                        textRes = R.string.comment_menu_report,
                        onClick = {})
                ),
                onClick = {},
                onReadMoreClick = {},
                badge = { PickeBadge(text = "변기는 변기다", horizontalPadding = SpacingTokens.s4) }
            )
            PickeCommentCard(
                profileImageUrl = "",
                nickname = "사색하는 사슴",
                timeAgo = "5분 전",
                content = "네덜란드 사례를 일반화하기엔 무리가 있지 않나요?",
                likeCount = 12,
                isLiked = true,
                badge = { PickeBadge(text = "예술이다", horizontalPadding = SpacingTokens.s4) }
            )
        }
    }
}