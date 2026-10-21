package com.picke.presentation.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ChatBubbleDimens {
    val avatarSize = 40.dp
    val bubbleMaxWidth = 272.dp
    val playingBarWidth = 2.5.dp
    val playingBarHeights = listOf(6.dp, 10.dp, 15.dp, 10.dp, 6.dp)
}

private const val DIMMED_ALPHA = 0.8f
private const val PLAYING_BAR_MIN_SCALE = 0.6f
private const val PLAYING_BAR_DURATION_MILLIS = 400
private const val PLAYING_BAR_DELAY_MILLIS = 120
private val PLAYING_BAR_MAX_SCALES = listOf(1.1f, 1.3f, 1.6f, 1.3f, 1.1f)

@Composable
fun ChatBubble(
    text: String,
    speakerName: String,
    avatarUrl: String?,
    @DrawableRes defaultAvatarRes: Int,
    isLeft: Boolean,
    showAvatarAndName: Boolean,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    isDimmed: Boolean = false,
    nameBadge: String? = null,
    onClick: (() -> Unit)? = null
) {
    val bubbleShape = RoundedCornerShape(RadiusTokens.default)
    val bubbleColor =
        if (isLeft) PickeTheme.colors.surfaceBeigeDefault else PickeTheme.colors.surfaceBeigeStrong
    val bubblePadding = if (isLeft) {
        PaddingValues(horizontal = SpacingTokens.s8, vertical = SpacingTokens.s6)
    } else {
        PaddingValues(SpacingTokens.s8)
    }
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier

    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (isDimmed) DIMMED_ALPHA else 1f),
        horizontalArrangement = Arrangement.spacedBy(
            space = SpacingTokens.s6,
            alignment = if (isLeft) Alignment.Start else Alignment.End
        ),
        verticalAlignment = Alignment.Top
    ) {
        if (isLeft) {
            ChatBubbleAvatar(
                avatarUrl = avatarUrl,
                defaultAvatarRes = defaultAvatarRes,
                isVisible = showAvatarAndName
            )
        }

        Column(
            modifier = Modifier.weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6),
            horizontalAlignment = if (isLeft) Alignment.Start else Alignment.End
        ) {
            if (showAvatarAndName) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = speakerName,
                        color = PickeTheme.colors.textDefault,
                        style = PickeTheme.typography.bodySmSemiBold
                    )
                    if (nameBadge != null) {
                        ChatBubbleNameBadge(text = nameBadge)
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isLeft && isPlaying) {
                    ChatBubblePlayingIndicator()
                }
                Text(
                    text = text,
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .widthIn(max = ChatBubbleDimens.bubbleMaxWidth)
                        .fillMaxWidth()
                        .then(clickModifier)
                        .background(bubbleColor, bubbleShape)
                        .border(
                            width = ComponentNumberTokens.borderWidthRegular,
                            color = PickeTheme.colors.borderBeigeDefault,
                            shape = bubbleShape
                        )
                        .padding(bubblePadding),
                    color = PickeTheme.colors.textBody,
                    style = PickeTheme.typography.bodyXsRegular
                )
                if (isLeft && isPlaying) {
                    ChatBubblePlayingIndicator()
                }
            }
        }

        if (!isLeft) {
            ChatBubbleAvatar(
                avatarUrl = avatarUrl,
                defaultAvatarRes = defaultAvatarRes,
                isVisible = showAvatarAndName
            )
        }
    }
}

@Composable
private fun ChatBubbleAvatar(
    avatarUrl: String?,
    @DrawableRes defaultAvatarRes: Int,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        ProfileImage(
            model = avatarUrl ?: defaultAvatarRes,
            size = ChatBubbleDimens.avatarSize,
            modifier = modifier
        )
    } else {
        Spacer(modifier = modifier.size(ChatBubbleDimens.avatarSize))
    }
}

@Composable
private fun ChatBubbleNameBadge(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .background(PickeTheme.colors.primary50, RoundedCornerShape(RadiusTokens.default))
            .padding(horizontal = SpacingTokens.s4, vertical = SpacingTokens.s2),
        color = PickeTheme.colors.badgeFilledText,
        style = PickeTheme.typography.captionMdSemiBold
    )
}

@Composable
private fun ChatBubblePlayingIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_bars")
    val barScales = ChatBubbleDimens.playingBarHeights.indices.map { index ->
        infiniteTransition.animateFloat(
            initialValue = PLAYING_BAR_MIN_SCALE,
            targetValue = PLAYING_BAR_MAX_SCALES[index],
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = PLAYING_BAR_DURATION_MILLIS,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(offsetMillis = index * PLAYING_BAR_DELAY_MILLIS)
            ),
            label = "scale_${index + 1}"
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChatBubbleDimens.playingBarHeights.forEachIndexed { index, barHeight ->
            Box(
                modifier = Modifier
                    .size(width = ChatBubbleDimens.playingBarWidth, height = barHeight)
                    .scale(scaleX = 1f, scaleY = barScales[index].value)
                    .background(PickeTheme.colors.iconPrimaryDefault, RoundedCornerShape(RadiusTokens.max))
            )
        }
    }
}

@Preview(name = "왼쪽 화자 · 재생 중", showBackground = true)
@Composable
private fun ChatBubbleLeftPreview() {
    PickeTheme {
        Column(
            modifier = Modifier.padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
        ) {
            ChatBubble(
                text = "모든 사물은 그에 걸맞은 목적과 형상을 가지고 있습니다.",
                speakerName = "플라톤",
                avatarUrl = null,
                defaultAvatarRes = R.drawable.illust_mengzi,
                isLeft = true,
                showAvatarAndName = true,
                isPlaying = true
            )
            ChatBubble(
                text = "사물의 본질을 왜곡하는 것은 진리를 모독하는 행위입니다.",
                speakerName = "플라톤",
                avatarUrl = null,
                defaultAvatarRes = R.drawable.illust_mengzi,
                isLeft = true,
                showAvatarAndName = false,
                isDimmed = true
            )
        }
    }
}

@Preview(name = "이름 배지", showBackground = true)
@Composable
private fun ChatBubbleNameBadgePreview() {
    PickeTheme {
        ChatBubble(
            text = "모든 사물은 그에 걸맞은 목적과 형상을 가지고 있습니다.",
            speakerName = "플라톤",
            avatarUrl = null,
            defaultAvatarRes = R.drawable.illust_mengzi,
            isLeft = true,
            showAvatarAndName = true,
            modifier = Modifier.padding(SpacingTokens.s16),
            nameBadge = "AI"
        )
    }
}

@Preview(name = "오른쪽 화자", showBackground = true)
@Composable
private fun ChatBubbleRightPreview() {
    PickeTheme {
        ChatBubble(
            text = "하지만 사물은 그저 그곳에 존재할 뿐입니다.",
            speakerName = "사르트르",
            avatarUrl = null,
            defaultAvatarRes = R.drawable.illust_mengzi,
            isLeft = false,
            showAvatarAndName = true,
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}