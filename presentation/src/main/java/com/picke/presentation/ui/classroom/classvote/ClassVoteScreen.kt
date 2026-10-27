package com.picke.presentation.ui.classroom.classvote

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classvote.model.ClassVoteUiAction
import com.picke.presentation.ui.classroom.classvote.model.ClassVoteUiEvent
import com.picke.presentation.ui.classroom.classvote.model.ClassVoteUiState
import com.picke.presentation.ui.classroom.component.rememberClassDeadlineText
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.vote.VoteScreen
import com.picke.presentation.ui.vote.model.VoteClassInfoUiModel
import com.picke.presentation.ui.vote.model.VoteType
import com.picke.presentation.ui.vote.model.VoteUiState
import com.picke.presentation.util.DummyData

@Composable
fun ClassVoteScreen(
    onBackClick: () -> Unit,
    onNavigateToDebate: (classId: Long) -> Unit,
    onNavigateToScenario: (classId: Long, battleId: Long) -> Unit,
    onNavigateToPerspective: (classId: Long, battleId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClassVoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val loadFailedMessage = stringResource(R.string.toast_load_failed)
    val actionFailedMessage = stringResource(R.string.toast_action_failed)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ClassVoteUiEvent.NavigateBack -> onBackClick()

                is ClassVoteUiEvent.NavigateToDebate -> onNavigateToDebate(event.classId)

                is ClassVoteUiEvent.NavigateToScenario -> onNavigateToScenario(
                    event.classId,
                    event.battleId
                )

                is ClassVoteUiEvent.NavigateToPerspective -> onNavigateToPerspective(
                    event.classId,
                    event.battleId
                )

                ClassVoteUiEvent.ShowLoadFailedToast ->
                    Toast.makeText(context, loadFailedMessage, Toast.LENGTH_SHORT).show()

                ClassVoteUiEvent.ShowActionFailedToast ->
                    Toast.makeText(context, actionFailedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ClassVoteContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun ClassVoteContent(
    uiState: ClassVoteUiState,
    onAction: (ClassVoteUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val deadlineText =
        rememberClassDeadlineText(uiState.deadline, R.string.class_deadline_full_format)
    val deadlineUntilText = if (uiState.deadline != null) {
        stringResource(R.string.class_deadline_until, deadlineText)
    } else {
        null
    }

    val battle = uiState.battle ?: return
    val classInfo = VoteClassInfoUiModel(
        className = uiState.className,
        // TODO: API 연동 - 클래스 배틀 상세에 의견 수 필드가 생기면 연결
        commentCount = null,
        deadlineText = deadlineUntilText,
        isAiBattle = uiState.isAiBattle
    )

    Box(modifier = modifier) {
        VoteScreen(
            voteType = uiState.voteType,
            battleDetail = battle,
            uiState = VoteUiState(),
            onBackClick = { onAction(ClassVoteUiAction.BackClick) },
            onVoteSubmit = {},
            onNavigateToExplore = {},
            onSubmitVote = { optionId, _ -> onAction(ClassVoteUiAction.VoteClick(optionId)) },
            onTrackShare = {},
            onGetShareLink = { _, _, _ -> },
            onDismissPointDialog = {},
            classInfo = classInfo
        )
    }
}

@Preview(name = "사전 투표", showBackground = true)
@Composable
private fun ClassVotePreVotePreview() {
    val classItem = DummyData.dummyMyClasses[1]
    val classBattle = DummyData.dummyClassAiQuestions.first()

    PickeTheme {
        ClassVoteContent(
            uiState = ClassVoteUiState(
                classId = classItem.id,
                voteType = VoteType.PRE,
                className = classItem.title,
                deadline = classItem.deadline,
                battleId = classBattle.battleId,
                isAiBattle = classBattle.isAiQuestion,
                battle = DummyData.dummyClassAiBattleDetail
            ),
            onAction = {}
        )
    }
}

@Preview(name = "최종 투표", showBackground = true)
@Composable
private fun ClassVoteFinalVotePreview() {
    val classItem = DummyData.dummyMyClasses[1]
    val classBattle = DummyData.dummyClassAiQuestions.first()

    PickeTheme {
        ClassVoteContent(
            uiState = ClassVoteUiState(
                classId = classItem.id,
                voteType = VoteType.POST,
                className = classItem.title,
                deadline = classItem.deadline,
                battleId = classBattle.battleId,
                isAiBattle = classBattle.isAiQuestion,
                battle = DummyData.dummyClassAiBattleDetail
            ),
            onAction = {}
        )
    }
}