package com.picke.presentation.ui.perspective.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.component.ProfileImage
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import com.picke.presentation.util.formatRate

private object PerspectiveStatsOptionDimens {
    val width = 70.dp
}

@Composable
fun PerspectiveStatsOption(
    imageUrl: String?,
    title: String,
    ratio: Float,
    modifier: Modifier = Modifier,
    isImageVisible: Boolean = true
) {
    Column(
        modifier = modifier.width(PerspectiveStatsOptionDimens.width),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isImageVisible) {
            ProfileImage(
                model = imageUrl,
                size = ComponentNumberTokens.avatarMd
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                color = PickeTheme.colors.textSubtler,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = PickeTheme.typography.captionMdMedium
            )
            Text(
                text = stringResource(R.string.comment_vote_rate, formatRate(ratio)),
                color = PickeTheme.colors.textSubtler,
                style = PickeTheme.typography.bodyXxsSemiBold
            )
        }
    }
}

@Preview(name = "이미지", showBackground = true)
@Composable
private fun PerspectiveStatsOptionPreview() {
    PickeTheme {
        val option = DummyData.dummyVoteOptions.first()
        PerspectiveStatsOption(
            imageUrl = option.imageUrl,
            title = option.title,
            ratio = option.ratio
        )
    }
}

@Preview(name = "이미지 없음", showBackground = true)
@Composable
private fun PerspectiveStatsOptionNoImagePreview() {
    PickeTheme {
        val option = DummyData.dummyVoteOptions.first()
        PerspectiveStatsOption(
            imageUrl = option.imageUrl,
            title = option.title,
            ratio = option.ratio,
            isImageVisible = false
        )
    }
}