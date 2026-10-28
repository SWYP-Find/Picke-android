package com.picke.presentation.ui.classroom.classdebate

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classdebate.component.ClassDebateRoundProgress
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateMessageUiModel
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateSpeaker
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateStatus
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateUiAction
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateUiEvent
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateUiState
import com.picke.presentation.ui.classroom.component.ClassRetryChip
import com.picke.presentation.ui.component.ChatBubble
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.component.CustomTextField
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.dialog.CustomConfirmDialog
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import com.picke.presentation.util.appendHighlighted

private object ClassDebateScreenDimens {
    val chatTopPadding = 28.dp
    val speakerGroupSpacing = 14.dp
    val composerBottomPadding = 28.dp
    val sendButtonSize = 36.dp
}

@Composable
fun ClassDebateScreen(
    onBackClick: () -> Unit,
    onNavigateToFinalVote: (classId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClassDebateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val loadFailedMessage = stringResource(R.string.toast_load_failed)
    val actionFailedMessage = stringResource(R.string.toast_action_failed)
    val replyTooShortText =
        stringResource(R.string.class_debate_reply_too_short, uiState.debate.replyMinLength)

    val replyTooShortMessage by rememberUpdatedState(replyTooShortText)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ClassDebateUiEvent.NavigateBack -> onBackClick()

                is ClassDebateUiEvent.NavigateToFinalVote -> onNavigateToFinalVote(event.classId)

                ClassDebateUiEvent.ShowLoadFailedToast ->
                    Toast.makeText(context, loadFailedMessage, Toast.LENGTH_SHORT).show()

                ClassDebateUiEvent.ShowReplyTooShortToast ->
                    Toast.makeText(context, replyTooShortMessage, Toast.LENGTH_SHORT).show()

                ClassDebateUiEvent.ShowActionFailedToast ->
                    Toast.makeText(context, actionFailedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ClassDebateContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun ClassDebateContent(
    uiState: ClassDebateUiState,
    onAction: (ClassDebateUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier.statusBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
            ) {
                CustomTopAppBar(
                    title = uiState.battleTitle,
                    centerTitle = false,
                    onBackClick = { onAction(ClassDebateUiAction.BackClick) },
                    backgroundColor = PickeTheme.colors.backgroundBeige,
                    titleColor = PickeTheme.colors.textSubtler
                )
                ClassDebateRoundProgress(
                    currentRound = uiState.displayRound,
                    totalRounds = ClassDebateUiState.TOTAL_ROUNDS,
                    modifier = Modifier.padding(horizontal = SpacingTokens.s16)
                )
            }
        },
        bottomBar = {
            if (uiState.isFinished) {
                ClassDebateFinalVoteSection(onFinalVoteClick = { onAction(ClassDebateUiAction.FinalVoteClick) })
            } else {
                ClassDebateComposerSection(
                    input = uiState.input,
                    isInputEnabled = uiState.isInputEnabled,
                    isSendEnabled = uiState.isSendEnabled,
                    onInputChange = { input -> onAction(ClassDebateUiAction.InputChange(input)) },
                    onSendClick = { onAction(ClassDebateUiAction.SendClick) }
                )
            }
        },
        containerColor = PickeTheme.colors.backgroundBeige,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        ClassDebateChatSection(
            myChoice = uiState.debate.myChoice,
            opponentName = uiState.debate.opponentName,
            opponentImageUrl = uiState.debate.opponentImageUrl,
            messages = uiState.debate.messages,
            isOpponentGenerating = uiState.isOpponentGenerating,
            isOpponentFailed = uiState.isOpponentFailed,
            isRetryVisible = uiState.isRetryVisible,
            onRetryClick = { onAction(ClassDebateUiAction.RetryClick) },
            modifier = Modifier.padding(innerPadding)
        )
    }

    if (uiState.isFinalVoteDialogVisible) {
        CustomConfirmDialog(
            message = stringResource(R.string.class_debate_final_vote_dialog_message),
            onConfirm = { onAction(ClassDebateUiAction.FinalVoteDialogReplayClick) },
            onDismiss = { onAction(ClassDebateUiAction.FinalVoteDialogVoteClick) },
            confirmText = stringResource(R.string.class_debate_final_vote_dialog_replay),
            dismissText = stringResource(R.string.class_debate_final_vote)
        )
    }
}

@Composable
private fun ClassDebateChatSection(
    myChoice: String,
    opponentName: String,
    opponentImageUrl: String?,
    messages: List<ClassDebateMessageUiModel>,
    isOpponentGenerating: Boolean,
    isOpponentFailed: Boolean,
    isRetryVisible: Boolean,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val myLabel = stringResource(R.string.class_member_me)
    val aiBadge = stringResource(R.string.class_debate_ai_badge)
    val pendingText = stringResource(
        if (isOpponentGenerating) R.string.class_debate_generating else R.string.class_debate_failed
    )

    val listState = rememberLazyListState()

    val isPendingVisible = isOpponentGenerating || isOpponentFailed
    val isPendingNewSpeaker = messages.lastOrNull()?.speaker != ClassDebateSpeaker.AI
    val lastItemIndex =
        messages.size + (if (isPendingVisible) 1 else 0) + (if (isRetryVisible) 1 else 0)

    LaunchedEffect(lastItemIndex) {
        if (lastItemIndex > 0) listState.animateScrollToItem(lastItemIndex)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(
            start = SpacingTokens.s16,
            top = ClassDebateScreenDimens.chatTopPadding,
            end = SpacingTokens.s16,
            bottom = SpacingTokens.s8
        ),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
    ) {
        item {
            ClassDebateIntroSection(myChoice = myChoice, opponentName = opponentName)
        }

        itemsIndexed(
            items = messages,
            key = { _, message -> message.messageId }
        ) { index, message ->
            val isOpponent = message.speaker == ClassDebateSpeaker.AI
            val isNewSpeaker = index == 0 || messages[index - 1].speaker != message.speaker

            ChatBubble(
                text = message.text,
                speakerName = if (isOpponent) opponentName else myLabel,
                avatarUrl = if (isOpponent) opponentImageUrl else null,
                defaultAvatarRes = if (isOpponent) R.drawable.illust_mengzi else R.drawable.illust_owl,
                isLeft = isOpponent,
                showAvatarAndName = isNewSpeaker,
                modifier = if (isNewSpeaker) {
                    Modifier.padding(top = ClassDebateScreenDimens.speakerGroupSpacing)
                } else {
                    Modifier
                },
                nameBadge = if (isOpponent) aiBadge else null
            )
        }

        if (isPendingVisible) {
            item {
                ChatBubble(
                    text = pendingText,
                    speakerName = opponentName,
                    avatarUrl = opponentImageUrl,
                    defaultAvatarRes = R.drawable.illust_mengzi,
                    isLeft = true,
                    showAvatarAndName = isPendingNewSpeaker,
                    modifier = if (isPendingNewSpeaker) {
                        Modifier.padding(top = ClassDebateScreenDimens.speakerGroupSpacing)
                    } else {
                        Modifier
                    },
                    isDimmed = isOpponentGenerating,
                    nameBadge = aiBadge
                )
            }
        }

        if (isRetryVisible) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ClassRetryChip(onClick = onRetryClick)
                }
            }
        }
    }
}

@Composable
private fun ClassDebateIntroSection(
    myChoice: String,
    opponentName: String,
    modifier: Modifier = Modifier
) {
    val choiceText = stringResource(R.string.class_debate_intro_choice, myChoice)
    val opponentText = stringResource(R.string.class_debate_intro_opponent, opponentName)
    val choiceLine = stringResource(R.string.class_debate_intro_choice_format, choiceText)
    val startLine =
        stringResource(
            R.string.class_debate_intro_start_format,
            opponentText,
            ClassDebateUiState.TOTAL_ROUNDS
        )

    val highlightStyle =
        SpanStyle(color = PickeTheme.colors.textDefault, fontWeight = FontWeight.Bold)
    val introText = remember(choiceLine, startLine, highlightStyle) {
        buildAnnotatedString {
            appendHighlighted(choiceLine, choiceText, highlightStyle)
            append("\n")
            appendHighlighted(startLine, opponentText, highlightStyle)
        }
    }

    Text(
        text = introText,
        modifier = modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.beige500, RoundedCornerShape(RadiusTokens.default))
            .padding(vertical = SpacingTokens.s12),
        color = PickeTheme.colors.textBody,
        textAlign = TextAlign.Center,
        style = PickeTheme.typography.captionMdMedium
    )
}

@Composable
private fun ClassDebateComposerSection(
    input: String,
    isInputEnabled: Boolean,
    isSendEnabled: Boolean,
    onInputChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sendButtonColor = if (isSendEnabled) {
        PickeTheme.colors.buttonIconBackgroundDefault
    } else {
        PickeTheme.colors.buttonIconBackgroundDisabled
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.beige300)
            .navigationBarsPadding()
            .imePadding()
    ) {
        HorizontalDivider(
            thickness = ComponentNumberTokens.borderWidthRegular,
            color = PickeTheme.colors.borderBeigeDefault
        )
        Row(
            modifier = Modifier.padding(
                start = SpacingTokens.s16,
                top = SpacingTokens.s16,
                end = SpacingTokens.s16,
                bottom = ClassDebateScreenDimens.composerBottomPadding
            ),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextField(
                value = input,
                onValueChange = onInputChange,
                minHeight = ComponentNumberTokens.inputTextfieldHeight,
                textStyle = PickeTheme.typography.bodyXsMedium,
                modifier = Modifier.weight(1f),
                placeholder = stringResource(R.string.class_debate_input_placeholder),
                enabled = isInputEnabled
            )
            IconButton(
                onClick = onSendClick,
                modifier = Modifier
                    .size(ClassDebateScreenDimens.sendButtonSize)
                    .background(sendButtonColor, CircleShape),
                enabled = isSendEnabled
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = stringResource(R.string.class_debate_send),
                    modifier = Modifier.size(ComponentNumberTokens.iconLg),
                    tint = PickeTheme.colors.textInverse
                )
            }
        }
    }
}

@Composable
private fun ClassDebateFinalVoteSection(
    onFinalVoteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(SpacingTokens.s16)
    ) {
        CustomButton(
            text = stringResource(R.string.class_debate_final_vote),
            onClick = onFinalVoteClick,
            backgroundColor = PickeTheme.colors.buttonPrimaryBackgroundDefault,
            textColor = PickeTheme.colors.buttonPrimaryTextDefault,
            minHeight = ComponentNumberTokens.buttonPrimaryLargeHeight
        )
    }
}

@Preview(name = "진행 중", showBackground = true)
@Composable
private fun ClassDebateContentPreview() {
    val battle = DummyData.dummyClassAiQuestions.first()
    val debate = DummyData.dummyClassDebate

    PickeTheme {
        ClassDebateContent(
            uiState = ClassDebateUiState(
                battleTitle = battle.title,
                debate = debate.copy(
                    round = 2,
                    messages = debate.messages +
                            DummyData.dummyClassDebateMyMessage +
                            DummyData.dummyClassDebateReplies.first()
                )
            ),
            onAction = {}
        )
    }
}

@Preview(name = "반박 불가", showBackground = true)
@Composable
private fun ClassDebateReplyDisabledPreview() {
    val battle = DummyData.dummyClassAiQuestions.first()

    PickeTheme {
        ClassDebateContent(
            uiState = ClassDebateUiState(
                battleTitle = battle.title,
                debate = DummyData.dummyClassDebate.copy(canReply = false)
            ),
            onAction = {}
        )
    }
}

@Preview(name = "AI 주장 생성 중", showBackground = true)
@Composable
private fun ClassDebateGeneratingPreview() {
    val battle = DummyData.dummyClassAiQuestions.first()
    val debate = DummyData.dummyClassDebate

    PickeTheme {
        ClassDebateContent(
            uiState = ClassDebateUiState(
                battleTitle = battle.title,
                debate = debate.copy(
                    round = 2,
                    status = ClassDebateStatus.GENERATING,
                    canReply = false,
                    messages = debate.messages + DummyData.dummyClassDebateMyMessage
                )
            ),
            onAction = {}
        )
    }
}

@Preview(name = "AI 주장 생성 실패", showBackground = true)
@Composable
private fun ClassDebateFailedPreview() {
    val battle = DummyData.dummyClassAiQuestions.first()
    val debate = DummyData.dummyClassDebate

    PickeTheme {
        ClassDebateContent(
            uiState = ClassDebateUiState(
                battleTitle = battle.title,
                debate = debate.copy(
                    round = 2,
                    status = ClassDebateStatus.FAILED,
                    canReply = false,
                    canRetry = true,
                    messages = debate.messages + DummyData.dummyClassDebateMyMessage
                )
            ),
            onAction = {}
        )
    }
}

@Preview(name = "토론 종료", showBackground = true)
@Composable
private fun ClassDebateFinishedPreview() {
    val battle = DummyData.dummyClassAiQuestions.first()
    val debate = DummyData.dummyClassDebate

    PickeTheme {
        ClassDebateContent(
            uiState = ClassDebateUiState(
                battleTitle = battle.title,
                debate = debate.copy(
                    round = ClassDebateUiState.TOTAL_ROUNDS,
                    status = ClassDebateStatus.COMPLETED,
                    canReply = false,
                    messages = debate.messages + DummyData.dummyClassDebateReplies.last()
                )
            ),
            onAction = {}
        )
    }
}

@Preview(name = "최종 투표 다이얼로그", showBackground = true)
@Composable
private fun ClassDebateFinalVoteDialogPreview() {
    val battle = DummyData.dummyClassAiQuestions.first()
    val debate = DummyData.dummyClassDebate

    PickeTheme {
        ClassDebateContent(
            uiState = ClassDebateUiState(
                battleTitle = battle.title,
                debate = debate.copy(
                    round = ClassDebateUiState.TOTAL_ROUNDS,
                    status = ClassDebateStatus.COMPLETED,
                    canReply = false,
                    messages = debate.messages + DummyData.dummyClassDebateReplies.last()
                ),
                isFinalVoteDialogVisible = true
            ),
            onAction = {}
        )
    }
}