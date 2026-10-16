package com.picke.presentation.ui.scenario.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.formatSpeed
import com.picke.presentation.util.formatTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerBar(
    isPlaying: Boolean,
    currentPositionMs: Long,
    totalDurationMs: Long,
    playbackSpeed: Float,
    onPlayPauseClick: () -> Unit,
    onSeek: (Float) -> Unit,
    onRewindClick: () -> Unit,
    onForwardClick: () -> Unit,
    onSpeedClick: () -> Unit,
    onReplayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.surfaceBeigeDefault)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        val sliderHeight = 20.dp
        val trackHeight = 2.dp

        Slider(
            value = if (totalDurationMs > 0) currentPositionMs.toFloat() / totalDurationMs else 0f,
            onValueChange = onSeek,
            modifier = Modifier
                .fillMaxWidth()
                .height(sliderHeight),
            thumb = {
                Box(
                    modifier = Modifier.height(sliderHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = PickeTheme.colors.primary500,
                                shape = CircleShape
                            )
                    )
                }
            },
            track = { sliderState ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(sliderHeight),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(trackHeight)
                            .background(PickeTheme.colors.textMuted)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = sliderState.value.coerceIn(0f, 1f))
                            .height(trackHeight)
                            .background(PickeTheme.colors.primary500)
                    )
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(currentPositionMs),
                style = PickeTheme.typography.bodyXsRegular,
                color = PickeTheme.colors.textMuted
            )
            Text(
                text = formatTime(totalDurationMs),
                style = PickeTheme.typography.bodyXsRegular,
                color = PickeTheme.colors.textMuted
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            PlaybackSpeedButton(
                speed = playbackSpeed,
                onClick = onSpeedClick
            )

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ControlSkipButton(
                    iconResId = R.drawable.ic_play_back,
                    label = stringResource(R.string.scenario_audio_skip_15_seconds),
                    onClick = onRewindClick
                )

                Box(
                    modifier = Modifier
                        .width(55.dp)
                        .clip(RoundedCornerShape(50))
                        .clickable { onPlayPauseClick() }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            if (isPlaying) R.drawable.ic_play_stop else R.drawable.ic_play
                        ),
                        contentDescription = stringResource(
                            if (isPlaying) R.string.scenario_audio_pause else R.string.scenario_audio_play
                        ),
                        tint = PickeTheme.colors.gray600,
                        modifier = Modifier
                            .width(22.dp)
                            .height(31.dp)
                    )
                }

                ControlSkipButton(
                    iconResId = R.drawable.ic_play_forward,
                    label = stringResource(R.string.scenario_audio_skip_15_seconds),
                    onClick = onForwardClick
                )
            }

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onReplayClick() }
                    .widthIn(min = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_replay),
                    contentDescription = null,
                    tint = PickeTheme.colors.textDefault,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(R.string.scenario_audio_replay),
                    style = PickeTheme.typography.bodyXxsMedium,
                    color = PickeTheme.colors.textMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun PlaybackSpeedButton(
    speed: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .widthIn(min = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatSpeed(speed),
            style = PickeTheme.typography.bodyLgMedium,
            color = PickeTheme.colors.textDefault,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.scenario_audio_playback_speed),
            style = PickeTheme.typography.bodyXxsMedium,
            color = PickeTheme.colors.textMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ControlSkipButton(
    iconResId: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick() },
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = label,
            tint = PickeTheme.colors.gray600,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = PickeTheme.typography.bodyXxsMedium,
            color = PickeTheme.colors.textMuted,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AudioPlayerBarPreview() {
    PickeTheme {
        AudioPlayerBar(
            isPlaying = true,
            currentPositionMs = 12,
            totalDurationMs = 40,
            playbackSpeed = 1.0f,
            onPlayPauseClick = { },
            onSeek = { },
            onRewindClick = { },
            onForwardClick = { },
            onSpeedClick = { },
            onReplayClick = { }
        )
    }
}