package com.picke.presentation.ui.classroom.classdetail

import android.widget.Toast
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
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailUiAction
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailUiEvent
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailUiState
import com.picke.presentation.ui.classroom.component.ClassBadge
import com.picke.presentation.ui.classroom.component.ClassBattleCard
import com.picke.presentation.ui.classroom.component.ClassManageSheet
import com.picke.presentation.ui.classroom.component.rememberClassDeadlineText
import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.CustomTopAppBarAction
import com.picke.presentation.ui.component.dialog.CustomConfirmDialog
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import java.time.LocalDateTime

@Composable
fun ClassDetailScreen(
    onBackClick: () -> Unit,
    onMemberClick: (classId: Long) -> Unit,
    onBattleClick: (classId: Long) -> Unit,
    onShareCodeClick: (classId: Long) -> Unit,
    onResultClick: (classId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClassDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val deadlinePassedMessage = stringResource(R.string.class_detail_deadline_passed)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ClassDetailUiEvent.NavigateBack -> onBackClick()
                is ClassDetailUiEvent.NavigateToMember -> onMemberClick(event.classId)
                is ClassDetailUiEvent.NavigateToBattle -> onBattleClick(event.classId)
                is ClassDetailUiEvent.NavigateToShare -> onShareCodeClick(event.classId)
                is ClassDetailUiEvent.NavigateToResult -> onResultClick(event.classId)
                // TODO: 클래스 정보 수정 화면이 생기면 이동 연결
                is ClassDetailUiEvent.NavigateToEdit -> Unit
                ClassDetailUiEvent.ShowDeadlinePassedToast ->
                    Toast.makeText(context, deadlinePassedMessage, Toast.LENGTH_SHORT).show()
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
        CustomTopAppBar(
            title = stringResource(R.string.class_detail_title),
            onBackClick = { onAction(ClassDetailUiAction.BackClick) },
            backgroundColor = PickeTheme.colors.backgroundBeige,
            actions = {
                CustomTopAppBarAction(
                    iconRes = R.drawable.ic_more,
                    contentDescription = stringResource(R.string.more),
                    onClick = { onAction(ClassDetailUiAction.MoreClick) }
                )
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s20)
        ) {
            ClassDetailHeaderSection(
                isInProgress = uiState.isInProgress,
                title = uiState.title
            )

            ClassDetailInfoSection(
                memberCount = uiState.memberCount,
                deadline = uiState.deadline,
                isOperator = uiState.isOperator,
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
    }

    if (uiState.isManageSheetVisible) {
        ClassManageSheet(
            isOperator = uiState.isOperator,
            onEditClick = { onAction(ClassDetailUiAction.ManageEditClick) },
            onShareCodeClick = { onAction(ClassDetailUiAction.ManageShareCodeClick) },
            onDeleteClick = { onAction(ClassDetailUiAction.ManageDeleteClick) },
            onLeaveClick = { onAction(ClassDetailUiAction.ManageLeaveClick) },
            onDismiss = { onAction(ClassDetailUiAction.ManageSheetDismiss) }
        )
    }

    if (uiState.isDeleteDialogVisible) {
        CustomConfirmDialog(
            message = stringResource(R.string.class_delete_confirm),
            onConfirm = { onAction(ClassDetailUiAction.DeleteConfirm) },
            onDismiss = { onAction(ClassDetailUiAction.DeleteDialogDismiss) },
            confirmText = stringResource(R.string.class_delete)
        )
    }

    if (uiState.isLeaveDialogVisible) {
        CustomConfirmDialog(
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
    val statusTextRes = if (isInProgress) R.string.my_class_status_in_progress else R.string.my_class_status_ended

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
    ) {
        ClassBadge(
            text = stringResource(statusTextRes),
            isMuted = !isInProgress
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
    val descriptionText = if (deadline != null) stringResource(R.string.class_deadline_until, deadlineText) else ""
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
        CustomButton(
            text = stringResource(R.string.class_detail_join_battle),
            onClick = onBattleClick,
            backgroundColor = PickeTheme.colors.buttonSecondaryBackgroundDefault,
            textColor = PickeTheme.colors.buttonSecondaryTextDefault,
            minHeight = ComponentNumberTokens.buttonPrimaryLargeHeight
        )
        CustomButton(
            text = stringResource(R.string.class_detail_view_result),
            onClick = onResultClick,
            backgroundColor = PickeTheme.colors.buttonPrimaryBackgroundDefault,
            textColor = PickeTheme.colors.buttonPrimaryTextDefault,
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
            isOperator = isOperator,
            isInProgress = classItem.isInProgress,
            title = classItem.title,
            memberCount = classItem.memberCount,
            deadline = classItem.deadline,
            battle = DummyData.dummyClassBattles.first()
        ),
        onAction = {}
    )
}