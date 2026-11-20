package com.picke.presentation.ui.vote.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.component.PickeEmptyState
import com.picke.presentation.ui.component.PickeTopAppBar
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun VoteBattleNotFound(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = PickeTheme.colors.backgroundBeige,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                PickeTopAppBar(
                    onBackClick = onBackClick,
                    backgroundColor = PickeTheme.colors.backgroundBeige
                )
            }
        }
    ) { innerPadding ->
        PickeEmptyState(
            message = stringResource(R.string.vote_battle_not_found),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VoteBattleNotFoundPreview() {
    PickeTheme {
        VoteBattleNotFound(onBackClick = {})
    }
}