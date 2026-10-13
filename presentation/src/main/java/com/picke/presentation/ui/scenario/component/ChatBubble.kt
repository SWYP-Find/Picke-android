package com.picke.presentation.ui.scenario.component

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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.domain.feature.scenario.model.SpeakerType
import com.picke.presentation.R
import com.picke.presentation.ui.component.ProfileImage
import com.picke.presentation.ui.scenario.model.ScenarioScriptUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

private object ChatBubbleDimens {
    val avatarSize = 40.dp
    val bubbleMaxWidth = 272.dp
    val playingBarWidth = 2.5.dp
    val playingBarHeights = listOf(6.dp, 10.dp, 15.dp, 10.dp, 6.dp)
}

private const val PLAYING_BAR_MIN_SCALE = 0.6f
private const val PLAYING_BAR_DURATION_MILLIS = 400
private const val PLAYING_BAR_DELAY_MILLIS = 120
private val PLAYING_BAR_MAX_SCALES = listOf(1.1f, 1.3f, 1.6f, 1.3f, 1.1f)

@Composable
fun ChatBubble(
    script: ScenarioScriptUiModel,
    isActive: Boolean,
    showAvatarAndName: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (script.speakerType == SpeakerType.NARRATOR) {
        val formattedText = script.displayText.replace(Regex(",\\s*"), ",\n")

        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .alpha(if (isActive) 1f else 0.8f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = formattedText,
                modifier = Modifier.clickable(onClick = onClick),
                color = if (isActive) PickeTheme.colors.textSubtle else PickeTheme.colors.textMuted,
                textAlign = TextAlign.Center,
                style = PickeTheme.typography.captionLgMedium.copy(fontStyle = FontStyle.Italic)
            )
        }

        return
    }

    val isLeft = script.speakerType == SpeakerType.A
    val imageModel = script.profileImageUrl ?: R.drawable.illust_mengzi
    val bubbleShape = RoundedCornerShape(RadiusTokens.default)
    val bubbleColor =
        if (isLeft) PickeTheme.colors.surfaceBeigeDefault else PickeTheme.colors.surfaceBeigeStrong
    val bubblePadding = if (isLeft) {
        PaddingValues(horizontal = SpacingTokens.s8, vertical = SpacingTokens.s6)
    } else {
        PaddingValues(SpacingTokens.s8)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (isActive) 1f else 0.8f),
        horizontalArrangement = Arrangement.spacedBy(
            space = SpacingTokens.s6,
            alignment = if (isLeft) Alignment.Start else Alignment.End
        ),
        verticalAlignment = Alignment.Top
    ) {
        if (isLeft) {
            ChatBubbleAvatar(model = imageModel, isVisible = showAvatarAndName)
        }

        Column(
            modifier = Modifier.weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6),
            horizontalAlignment = if (isLeft) Alignment.Start else Alignment.End
        ) {
            if (showAvatarAndName) {
                Text(
                    text = script.speakerName,
                    color = PickeTheme.colors.textDefault,
                    style = PickeTheme.typography.bodySmSemiBold
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isLeft && isActive) {
                    ChattingLoadingAnimation()
                }
                Text(
                    text = script.displayText,
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .widthIn(max = ChatBubbleDimens.bubbleMaxWidth)
                        .fillMaxWidth()
                        .clickable(onClick = onClick)
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
                if (isLeft && isActive) {
                    ChattingLoadingAnimation()
                }
            }
        }

        if (!isLeft) {
            ChatBubbleAvatar(model = imageModel, isVisible = showAvatarAndName)
        }
    }
}

@Composable
private fun ChatBubbleAvatar(
    model: Any?,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        ProfileImage(
            model = model,
            modifier = modifier.size(ChatBubbleDimens.avatarSize)
        )
    } else {
        Spacer(modifier = modifier.size(ChatBubbleDimens.avatarSize))
    }
}

@Composable
private fun ChattingLoadingAnimation(modifier: Modifier = Modifier) {
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
    val scripts = DummyData.dummyScripts

    PickeTheme {
        Column(
            modifier = Modifier.padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
        ) {
            ChatBubble(
                script = scripts[1],
                isActive = true,
                showAvatarAndName = true,
                onClick = {}
            )
            ChatBubble(
                script = scripts[3],
                isActive = false,
                showAvatarAndName = false,
                onClick = {}
            )
        }
    }
}

@Preview(name = "오른쪽 화자", showBackground = true)
@Composable
private fun ChatBubbleRightPreview() {
    PickeTheme {
        ChatBubble(
            script = DummyData.dummyScripts[2],
            isActive = true,
            showAvatarAndName = true,
            onClick = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}

@Preview(name = "해설", showBackground = true)
@Composable
private fun ChatBubbleNarratorPreview() {
    PickeTheme {
        ChatBubble(
            script = DummyData.dummyScripts.first(),
            isActive = true,
            showAvatarAndName = true,
            onClick = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}