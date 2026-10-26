package com.picke.presentation.ui.classroom.classbattlepreview

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classbattlepreview.component.ClassBattlePreviewBadge
import com.picke.presentation.ui.classroom.classbattlepreview.model.ClassBattlePreviewUiAction
import com.picke.presentation.ui.classroom.classbattlepreview.model.ClassBattlePreviewUiEvent
import com.picke.presentation.ui.classroom.classbattlepreview.model.ClassBattlePreviewUiState
import com.picke.presentation.ui.component.AudioPlayerBar
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.scenario.component.ScenarioChatBubble
import com.picke.presentation.ui.scenario.model.ScenarioScriptUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

private object ClassBattlePreviewScreenDimens {
    val speakerGroupSpacing = 14.dp
}

@Composable
fun ClassBattlePreviewScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClassBattlePreviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val loadFailedMessage = stringResource(R.string.toast_load_failed)

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        viewModel.onAction(ClassBattlePreviewUiAction.ScreenStop)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ClassBattlePreviewUiEvent.NavigateBack -> onBackClick()

                ClassBattlePreviewUiEvent.ShowLoadFailedToast ->
                    Toast.makeText(context, loadFailedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ClassBattlePreviewContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun ClassBattlePreviewContent(
    uiState: ClassBattlePreviewUiState,
    onAction: (ClassBattlePreviewUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = uiState.title,
                    onBackClick = { onAction(ClassBattlePreviewUiAction.BackClick) },
                    backgroundColor = PickeTheme.colors.backgroundBeige
                )
            }
        },
        bottomBar = {
            AudioPlayerBar(
                isPlaying = uiState.isPlaying,
                currentPositionMs = uiState.currentPositionMs,
                totalDurationMs = uiState.totalDurationMs,
                playbackSpeed = uiState.playbackSpeed,
                onPlayPauseClick = { onAction(ClassBattlePreviewUiAction.PlayPauseClick) },
                onSeek = { ratio -> onAction(ClassBattlePreviewUiAction.Seek(ratio)) },
                onRewindClick = { onAction(ClassBattlePreviewUiAction.RewindClick) },
                onForwardClick = { onAction(ClassBattlePreviewUiAction.ForwardClick) },
                onSpeedClick = { onAction(ClassBattlePreviewUiAction.SpeedClick) },
                onReplayClick = { onAction(ClassBattlePreviewUiAction.ReplayClick) }
            )
        },
        containerColor = PickeTheme.colors.backgroundBeige,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClassBattlePreviewBadge(modifier = Modifier.padding(top = SpacingTokens.s8))

            ClassBattlePreviewScriptSection(
                scripts = uiState.visibleScripts,
                activeIndex = uiState.activeIndex,
                isPlaying = uiState.isPlaying,
                onScriptClick = { startTimeMs ->
                    onAction(ClassBattlePreviewUiAction.ScriptClick(startTimeMs))
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ClassBattlePreviewScriptSection(
    scripts: List<ScenarioScriptUiModel>,
    activeIndex: Int,
    isPlaying: Boolean,
    onScriptClick: (startTimeMs: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(activeIndex) {
        if (activeIndex in scripts.indices) listState.animateScrollToItem(activeIndex)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(
            start = SpacingTokens.s16,
            top = SpacingTokens.s20,
            end = SpacingTokens.s16,
            bottom = SpacingTokens.s40
        ),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
    ) {
        itemsIndexed(scripts) { index, script ->
            val showAvatarAndName =
                index == 0 || scripts[index - 1].speakerType != script.speakerType

            if (showAvatarAndName && index > 0) {
                Spacer(modifier = Modifier.height(ClassBattlePreviewScreenDimens.speakerGroupSpacing))
            }

            ScenarioChatBubble(
                script = script,
                isActive = isPlaying && index == activeIndex,
                showAvatarAndName = showAvatarAndName,
                onClick = { onScriptClick(script.startTimeMs) }
            )
        }
    }
}

@Preview(name = "재생 중", showBackground = true)
@Composable
private fun ClassBattlePreviewContentPlayingPreview() {
    val battle = DummyData.dummyClassBattles[1]
    val scenario = DummyData.dummyClassBattlePreviewScenarios.getValue(battle.battleId)
    val scripts =
        scenario.nodes.getValue(scenario.startNodeId).scripts.filter { it.startTimeMs < 60_000L }

    PickeTheme {
        ClassBattlePreviewContent(
            uiState = ClassBattlePreviewUiState(
                battleId = battle.battleId,
                title = battle.title,
                scripts = scripts,
                isPlaying = true,
                maxRevealedIndex = 2,
                currentPositionMs = scripts[2].startTimeMs,
                totalDurationMs = 60_000L
            ),
            onAction = {}
        )
    }
}

@Preview(name = "1분 종료", showBackground = true)
@Composable
private fun ClassBattlePreviewContentEndedPreview() {
    val battle = DummyData.dummyClassBattles[1]
    val scenario = DummyData.dummyClassBattlePreviewScenarios.getValue(battle.battleId)
    val scripts =
        scenario.nodes.getValue(scenario.startNodeId).scripts.filter { it.startTimeMs < 60_000L }

    PickeTheme {
        ClassBattlePreviewContent(
            uiState = ClassBattlePreviewUiState(
                battleId = battle.battleId,
                title = battle.title,
                scripts = scripts,
                maxRevealedIndex = scripts.lastIndex,
                currentPositionMs = 60_000L,
                totalDurationMs = 60_000L
            ),
            onAction = {}
        )
    }
}