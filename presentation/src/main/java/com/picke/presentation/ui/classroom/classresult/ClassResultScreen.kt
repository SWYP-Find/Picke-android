package com.picke.presentation.ui.classroom.classresult

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
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
import com.picke.presentation.ui.classroom.classresult.component.ClassResultAiInsightCard
import com.picke.presentation.ui.classroom.classresult.component.ClassResultFeedbackCard
import com.picke.presentation.ui.classroom.classresult.component.ClassResultMyVoteCard
import com.picke.presentation.ui.classroom.classresult.component.ClassResultOpinionCard
import com.picke.presentation.ui.classroom.classresult.component.ClassResultParticipationCard
import com.picke.presentation.ui.classroom.classresult.component.ClassResultStanceCard
import com.picke.presentation.ui.classroom.classresult.model.ClassResultReportUiModel
import com.picke.presentation.ui.classroom.classresult.model.ClassResultSummaryUiModel
import com.picke.presentation.ui.classroom.classresult.model.ClassResultTab
import com.picke.presentation.ui.classroom.classresult.model.ClassResultUiAction
import com.picke.presentation.ui.classroom.classresult.model.ClassResultUiEvent
import com.picke.presentation.ui.classroom.classresult.model.ClassResultUiState
import com.picke.presentation.ui.classroom.component.ClassTitledBlock
import com.picke.presentation.ui.classroom.component.rememberClassDeadlineText
import com.picke.presentation.ui.component.CustomTabBar
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.PickeBadge
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import java.time.LocalDateTime

private const val TAB_ITEM_INDEX = 1

@Composable
fun ClassResultScreen(
    onBackClick: () -> Unit,
    onNavigateToMyOpinion: (classId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClassResultViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val loadFailedMessage = stringResource(R.string.toast_load_failed)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ClassResultUiEvent.NavigateBack -> onBackClick()

                is ClassResultUiEvent.NavigateToMyOpinion -> onNavigateToMyOpinion(event.classId)

                ClassResultUiEvent.ShowLoadFailedToast ->
                    Toast.makeText(context, loadFailedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ClassResultContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ClassResultContent(
    uiState: ClassResultUiState,
    onAction: (ClassResultUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabLabels = ClassResultTab.entries.map { stringResource(it.labelRes) }
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.visibleTab) {
        if (listState.firstVisibleItemIndex >= TAB_ITEM_INDEX) listState.scrollToItem(TAB_ITEM_INDEX)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PickeTheme.colors.backgroundBeige)
    ) {
        CustomTopAppBar(
            title = uiState.className,
            onBackClick = { onAction(ClassResultUiAction.BackClick) },
            backgroundColor = PickeTheme.colors.backgroundBeige,
            titleColor = PickeTheme.colors.textSubtler
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = listState
        ) {
            item {
                ClassResultHeaderSection(
                    isInProgress = uiState.isInProgress,
                    tag = uiState.tag,
                    battleTitle = uiState.battleTitle,
                    deadline = uiState.deadline
                )
            }

            if (uiState.isTabVisible) {
                stickyHeader {
                    CustomTabBar(
                        tabs = tabLabels,
                        selectedTab = tabLabels[uiState.visibleTab.ordinal],
                        onTabSelected = { label ->
                            val tab = ClassResultTab.entries[tabLabels.indexOf(label)]
                            onAction(ClassResultUiAction.TabSelect(tab))
                        },
                        modifier = Modifier.background(PickeTheme.colors.backgroundBeige)
                    )
                }
            }

            item {
                when (uiState.visibleTab) {
                    ClassResultTab.SUMMARY -> uiState.summary?.let { summary ->
                        ClassResultSummarySection(summary = summary)
                    }

                    ClassResultTab.REPORT -> uiState.report?.let { report ->
                        ClassResultReportSection(
                            report = report,
                            onMyOpinionClick = { onAction(ClassResultUiAction.MyOpinionClick) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ClassResultHeaderSection(
    isInProgress: Boolean,
    tag: String,
    battleTitle: String,
    deadline: LocalDateTime?,
    modifier: Modifier = Modifier
) {
    val statusTextRes =
        if (isInProgress) R.string.my_class_status_in_progress else R.string.my_class_status_ended
    val deadlineText = rememberClassDeadlineText(deadline, R.string.class_deadline_full_format)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s12),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6)) {
            PickeBadge(
                text = stringResource(statusTextRes),
                isMuted = !isInProgress
            )
            if (tag.isNotEmpty()) PickeBadge(text = tag)
        }

        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
            Text(
                text = battleTitle,
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.headingXl
            )
            if (deadline != null) {
                Text(
                    text = stringResource(R.string.class_deadline_until, deadlineText),
                    color = PickeTheme.colors.textMuted,
                    style = PickeTheme.typography.bodyXsRegular
                )
            }
        }
    }
}

@Composable
private fun ClassResultSummarySection(
    summary: ClassResultSummaryUiModel,
    modifier: Modifier = Modifier
) {
    val titleStyle = PickeTheme.typography.bodySmSemiBold

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = SpacingTokens.s16,
                top = SpacingTokens.s16,
                end = SpacingTokens.s16,
                bottom = SpacingTokens.s24
            ),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s24)
    ) {
        ClassResultAiInsightCard(insight = summary.aiInsight)

        ClassTitledBlock(
            title = stringResource(R.string.class_result_participation),
            titleStyle = titleStyle
        ) {
            ClassResultParticipationCard(
                memberCount = summary.memberCount,
                participatedCount = summary.participatedCount,
                commentedCount = summary.commentedCount
            )
        }

        ClassTitledBlock(
            title = stringResource(R.string.class_result_stance),
            titleStyle = titleStyle
        ) {
            ClassResultStanceCard(stances = summary.stances)
        }

        ClassTitledBlock(
            title = stringResource(R.string.class_result_opinions),
            titleStyle = titleStyle
        ) {
            ClassResultOpinionCard(
                bestComment = summary.bestComment,
                mostDebatedComment = summary.mostDebatedComment
            )
        }
    }
}

@Composable
private fun ClassResultReportSection(
    report: ClassResultReportUiModel,
    onMyOpinionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val titleStyle = PickeTheme.typography.bodyXsSemiBold

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = SpacingTokens.s16,
                top = SpacingTokens.s16,
                end = SpacingTokens.s16,
                bottom = SpacingTokens.s24
            ),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s24)
    ) {
        ClassTitledBlock(
            title = stringResource(R.string.class_result_my_vote),
            titleStyle = titleStyle
        ) {
            ClassResultMyVoteCard(report = report)
        }

        ClassTitledBlock(
            title = stringResource(R.string.class_result_ai_feedback),
            titleStyle = titleStyle
        ) {
            ClassResultFeedbackCard(
                report = report,
                onMyOpinionClick = onMyOpinionClick
            )
        }
    }
}

@Preview(name = "운영자 참여 - 클래스 요약", showBackground = true)
@Composable
private fun ClassResultOperatorParticipatedPreview() {
    ClassResultContentPreview(
        summary = DummyData.dummyClassResultSummary,
        report = DummyData.dummyClassResultReport
    )
}

@Preview(name = "운영자 참여 - 내 리포트", showBackground = true)
@Composable
private fun ClassResultOperatorReportPreview() {
    ClassResultContentPreview(
        summary = DummyData.dummyClassResultSummary,
        report = DummyData.dummyClassResultReport,
        selectedTab = ClassResultTab.REPORT
    )
}

@Preview(name = "운영자 미참여", showBackground = true)
@Composable
private fun ClassResultOperatorNotParticipatedPreview() {
    ClassResultContentPreview(
        summary = DummyData.dummyClassResultSummary,
        report = null
    )
}

@Preview(name = "멤버", showBackground = true)
@Composable
private fun ClassResultMemberPreview() {
    ClassResultContentPreview(
        summary = null,
        report = DummyData.dummyClassResultReport
    )
}

@Composable
private fun ClassResultContentPreview(
    summary: ClassResultSummaryUiModel?,
    report: ClassResultReportUiModel?,
    selectedTab: ClassResultTab = ClassResultTab.SUMMARY
) {
    val classItem = DummyData.dummyMyClasses.first()
    val battle = DummyData.dummyClassBattleByClassId.getValue(classItem.id)

    PickeTheme {
        ClassResultContent(
            uiState = ClassResultUiState(
                classId = classItem.id,
                className = classItem.title,
                isInProgress = classItem.isInProgress,
                tag = battle.tag,
                battleTitle = battle.title,
                deadline = classItem.deadline,
                hasSummaryTab = summary != null,
                hasReportTab = report != null,
                summary = summary,
                report = report,
                selectedTab = selectedTab
            ),
            onAction = {}
        )
    }
}