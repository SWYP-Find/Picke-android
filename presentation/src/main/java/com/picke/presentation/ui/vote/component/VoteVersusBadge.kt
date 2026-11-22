package com.picke.presentation.ui.vote.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme

private object VoteVersusBadgeDimens {
    val size = 36.dp
}

@Composable
fun VoteVersusBadge(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(VoteVersusBadgeDimens.size),
        shape = CircleShape,
        color = PickeTheme.colors.secondary200
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.vote_versus),
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.bodySmMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VoteVersusBadgePreview() {
    PickeTheme {
        VoteVersusBadge()
    }
}