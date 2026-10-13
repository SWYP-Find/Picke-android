package com.picke.presentation.ui.scenario.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.domain.feature.scenario.model.SpeakerType
import com.picke.presentation.R
import com.picke.presentation.ui.component.ChatBubble
import com.picke.presentation.ui.scenario.model.ScenarioScriptUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun ScenarioChatBubble(
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

    ChatBubble(
        text = script.displayText,
        speakerName = script.speakerName,
        avatarUrl = script.profileImageUrl,
        defaultAvatarRes = R.drawable.illust_mengzi,
        isLeft = script.speakerType == SpeakerType.A,
        showAvatarAndName = showAvatarAndName,
        modifier = modifier,
        isPlaying = isActive,
        isDimmed = !isActive,
        onClick = onClick
    )
}

@Preview(name = "대화", showBackground = true)
@Composable
private fun ScenarioChatBubblePreview() {
    val scripts = DummyData.dummyScripts

    PickeTheme {
        Column(
            modifier = Modifier.padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
        ) {
            ScenarioChatBubble(
                script = scripts[1],
                isActive = true,
                showAvatarAndName = true,
                onClick = {}
            )
            ScenarioChatBubble(
                script = scripts[2],
                isActive = false,
                showAvatarAndName = true,
                onClick = {}
            )
        }
    }
}

@Preview(name = "해설", showBackground = true)
@Composable
private fun ScenarioChatBubbleNarratorPreview() {
    PickeTheme {
        ScenarioChatBubble(
            script = DummyData.dummyScripts.first(),
            isActive = true,
            showAvatarAndName = true,
            onClick = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}