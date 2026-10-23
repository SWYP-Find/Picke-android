package com.picke.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object PickeEmptyStateDimens {
    val logoWidth = 160.dp
    val logoHeight = 120.dp
}

@Composable
fun PickeEmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.logo_picke),
            contentDescription = stringResource(R.string.comment_empty_image),
            modifier = Modifier.size(
                width = PickeEmptyStateDimens.logoWidth,
                height = PickeEmptyStateDimens.logoHeight
            ),
            tint = PickeTheme.colors.borderBeigeDefault
        )
        Text(
            text = message,
            color = PickeTheme.colors.beige800,
            style = PickeTheme.typography.bodySmRegular
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PickeEmptyStatePreview() {
    PickeTheme {
        PickeEmptyState(
            message = "아직 준비된 배틀이 없어요!",
            modifier = Modifier.fillMaxSize()
        )
    }
}