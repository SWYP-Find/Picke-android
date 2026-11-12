package com.picke.presentation.ui.classroom.classdetail

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import com.picke.presentation.ui.classroom.classdetail.component.ClassDetailInfoRow
import com.picke.presentation.ui.classroom.classdetail.component.ClassDetailSkeleton
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailInfoUiModel
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailUiAction
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailUiEvent
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailUiState
import com.picke.presentation.ui.classroom.component.ClassBattleCard
import com.picke.presentation.ui.classroom.component.ClassManageSheet
import com.picke.presentation.ui.classroom.component.rememberClassDeadlineText
import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import com.picke.presentation.ui.component.PickeBadge
import com.picke.presentation.ui.component.PickeBadgeColor
import com.picke.presentation.ui.component.PickeButton
import com.picke.presentation.ui.component.PickeLoadFailedSection
import com.picke.presentation.ui.component.PickeTopAppBar
import com.picke.presentation.ui.component.PickeTopAppBarAction
import com.picke.presentation.ui.component.dialog.PickeConfirmDialog
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import java.time.LocalDateTime

@Composable
fun ClassDetailScreen(
    onBackClick: () -> Unit,
    onClassChanged: () -> Unit,
    onMemberClick: (classId: Long) -> Unit,
    onNavigateToPreVote: (classId: Long) -> Unit,
    onNavigateToScenario: (classId: Long, battleId: Long) -> Unit,
    onNavigateToDebate: (classId: Long) -> Unit,
    onNavigateToFinalVote: (classId: Long) -> Unit,
    onNavigateToPerspective: (classId: Long, battleId: Long) -> Unit,
    onShareCodeClick: (classId: Long) -> Unit,
    onResultClick: (classId: Long) -> Unit,
    onEditClick: (classId: Long) -> Unit,
    onClassRemoved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClassDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val deadlinePassedMessage = stringResource(R.string.class_detail_deadline_passed)
    val notParticipatingMessage = stringResource(R.string.class_detail_not_participating)
    val loadFailedMessage = stringResource(R.string.toast_load_failed)
    val actionFailedMessage = stringResource(R.string.toast_action_failed)

    BackHandler { viewModel.onAction(ClassDetailUiAction.BackClick) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ClassDetailUiEvent.NavigateBack -> onBackClick()

                ClassDetailUiEvent.NavigateBackAfterClassChanged -> onClassChanged()

                is ClassDetailUiEvent.NavigateToMember -> onMemberClick(event.classId)

                is ClassDetailUiEvent.NavigateToPreVote -> onNavigateToPreVote(event.classId)

                is ClassDetailUiEvent.NavigateToScenario -> onNavigateToScenario(
                    event.classId,
                    event.battleId
                )

                is ClassDetailUiEvent.NavigateToDebate -> onNavigateToDebate(event.classId)

                is ClassDetailUiEvent.NavigateToFinalVote -> onNavigateToFinalVote(event.classId)

                is ClassDetailUiEvent.NavigateToPerspective -> onNavigateToPerspective(
                    event.classId,
                    event.battleId
                )

                is ClassDetailUiEvent.NavigateToShare -> onShareCodeClick(event.classId)

                is ClassDetailUiEvent.NavigateToResult -> onResultClick(event.classId)

                is ClassDetailUiEvent.NavigateToEdit -> onEditClick(event.classId)

                ClassDetailUiEvent.NavigateBackAfterClassRemoved -> onClassRemoved()

                ClassDetailUiEvent.ShowDeadlinePassedToast ->
                    Toast.makeText(context, deadlinePassedMessage, Toast.LENGTH_SHORT).show()

                ClassDetailUiEvent.ShowNotParticipatingToast ->
                    Toast.makeText(context, notParticipatingMessage, Toast.LENGTH_SHORT).show()

                ClassDetailUiEvent.ShowLoadFailedToast ->
                    Toast.makeText(context, loadFailedMessage, Toast.LENGTH_SHORT).show()

                ClassDetailUiEvent.ShowActionFailedToast ->
                    Toast.makeText(context, actionFailedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ClassDetailContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun ClassDetailContent(
    uiState: ClassDetailUiState,
    onAction: (ClassDetailUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PickeTheme.colors.backgroundBeige)
    ) {
        PickeTopAppBar(
            title = stringResource(R.string.class_detail_title),
            onBackClick = { onAction(ClassDetailUiAction.BackClick) },
            backgroundColor = PickeTheme.colors.backgroundBeige,
            actions = {
                PickeTopAppBarAction(
                    iconRes = R.drawable.ic_more,
                    contentDescription = stringResource(R.string.more),
                    onClick = { onAction(ClassDetailUiAction.MoreClick) }
                )
            }
        )

        if (uiState.isLoading) {
            ClassDetailSkeleton(modifier = Modifier.weight(1f))
        } else {
            val classInfo = uiState.classInfo
            if (classInfo != null) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(SpacingTokens.s16),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.s20)
                ) {
                    ClassDetailHeaderSection(
                        isInProgress = classInfo.isInProgress,
                        title = classInfo.title
                    )

                    ClassDetailInfoSection(
                        memberCount = classInfo.memberCount,
                        deadline = classInfo.deadline,
                        isOperator = classInfo.isOperator,
                        onMemberClick = { onAction(ClassDetailUiAction.MemberClick) },
                        onDeadlineEditClick = { onAction(ClassDetailUiAction.DeadlineEditClick) }
                    )

                    uiState.battle?.let { battle ->
                        ClassDetailBattleSection(
                            battle = battle,
                            onBattleClick = { onAction(ClassDetailUiAction.BattleClick) }
                        )
                    }

                    ClassDetailActionSection(
                        onBattleClick = { onAction(ClassDetailUiAction.BattleClick) },
                        onResultClick = { onAction(ClassDetailUiAction.ResultClick) }
                    )
                }
            } else {
                PickeLoadFailedSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacingTokens.s16)
                )
            }
        }
    }

    val manageClassInfo = uiState.classInfo
    if (uiState.isManageSheetVisible && manageClassInfo != null) {
        ClassManageSheet(
            isOperator = manageClassInfo.isOperator,
            onEditClick = { onAction(ClassDetailUiAction.ManageEditClick) },
            onShareCodeClick = { onAction(ClassDetailUiAction.ManageShareCodeClick) },
            onDeleteClick = { onAction(ClassDetailUiAction.ManageDeleteClick) },
            onLeaveClick = { onAction(ClassDetailUiAction.ManageLeaveClick) },
            onDismiss = { onAction(ClassDetailUiAction.ManageSheetDismiss) }
        )
    }

    if (uiState.isDeleteDialogVisible) {
        PickeConfirmDialog(
            message = stringResource(R.string.class_delete_confirm),
            onConfirm = { onAction(ClassDetailUiAction.DeleteConfirm) },
            onDismiss = { onAction(ClassDetailUiAction.DeleteDialogDismiss) },
            confirmText = stringResource(R.string.class_delete)
        )
    }

    if (uiState.isLeaveDialogVisible) {
        PickeConfirmDialog(
            message = stringResource(R.string.class_leave_confirm),
            onConfirm = { onAction(ClassDetailUiAction.LeaveConfirm) },
            onDismiss = { onAction(ClassDetailUiAction.LeaveDialogDismiss) },
            confirmText = stringResource(R.string.class_leave)
        )
    }
}

@Composable
private fun ClassDetailHeaderSection(
    isInProgress: Boolean,
    title: String,
    modifier: Modifier = Modifier
) {
    val statusTextRes =
        if (isInProgress) R.string.my_class_status_in_progress else R.string.my_class_status_ended

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
    ) {
        PickeBadge(
            text = stringResource(statusTextRes),
            color = if (isInProgress) PickeBadgeColor.SECONDARY else PickeBadgeColor.TERTIARY
        )
        Text(
            text = title,
            color = PickeTheme.colors.textSubtler,
            style = PickeTheme.typography.headingLg
        )
    }
}

@Composable
private fun ClassDetailInfoSection(
    memberCount: Int,
    deadline: LocalDateTime?,
    isOperator: Boolean,
    onMemberClick: () -> Unit,
    onDeadlineEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)
    val deadlineText = rememberClassDeadlineText(deadline, R.string.class_deadline_full_format)
    val descriptionText =
        if (deadline != null) stringResource(R.string.class_deadline_until, deadlineText) else ""
    val actionText = if (isOperator) stringResource(R.string.class_detail_edit) else null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.surfaceBeigeDefault, shape)
            .border(
                ComponentNumberTokens.borderWidthRegular,
                PickeTheme.colors.borderBeigeDefault,
                shape
            )
            .padding(horizontal = SpacingTokens.s12)
    ) {
        ClassDetailInfoRow(
            title = stringResource(R.string.class_detail_member),
            description = stringResource(R.string.class_detail_member_count, memberCount),
            actionText = stringResource(R.string.my_class_view),
            onActionClick = onMemberClick
        )
        HorizontalDivider(
            thickness = ComponentNumberTokens.borderWidthRegular,
            color = PickeTheme.colors.borderBeigeDefault
        )
        ClassDetailInfoRow(
            title = stringResource(R.string.class_setting_deadline_label),
            description = descriptionText,
            actionText = actionText,
            onActionClick = onDeadlineEditClick
        )
    }
}

@Composable
private fun ClassDetailBattleSection(
    battle: ClassBattleUiModel,
    onBattleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
    ) {
        Text(
            text = stringResource(R.string.class_detail_this_battle),
            color = PickeTheme.colors.textMuted,
            style = PickeTheme.typography.bodySmSemiBold
        )
        ClassBattleCard(battle = battle, onClick = onBattleClick)
    }
}

@Composable
private fun ClassDetailActionSection(
    onBattleClick: () -> Unit,
    onResultClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
    ) {
        PickeButton(
            text = stringResource(R.string.class_detail_join_battle),
            onClick = onBattleClick,
            backgroundColor = PickeTheme.colors.buttonSecondaryBackgroundDefault,
            textColor = PickeTheme.colors.buttonSecondaryTextDefault,
            minHeight = ComponentNumberTokens.buttonPrimaryLargeHeight
        )
        PickeButton(
            text = stringResource(R.string.class_detail_view_result),
            onClick = onResultClick,
            minHeight = ComponentNumberTokens.buttonPrimaryLargeHeight
        )
    }
}

@Preview(name = "운영자", showBackground = true)
@Composable
private fun ClassDetailOperatorPreview() {
    PickeTheme {
        ClassDetailContentPreview(isOperator = true)
    }
}

@Preview(name = "참여자", showBackground = true)
@Composable
private fun ClassDetailMemberPreview() {
    PickeTheme {
        ClassDetailContentPreview(isOperator = false)
    }
}

@Composable
private fun ClassDetailContentPreview(isOperator: Boolean) {
    val classItem = DummyData.dummyMyClasses.first()

    ClassDetailContent(
        uiState = ClassDetailUiState(
            classId = classItem.id,
            isLoading = false,
            classInfo = ClassDetailInfoUiModel(
                isOperator = isOperator,
                isInProgress = classItem.isInProgress,
                title = classItem.title,
                memberCount = classItem.memberCount,
                deadline = classItem.deadline
            ),
            battle = DummyData.dummyClassBattles.first()
        ),
        onAction = {}
    )
}