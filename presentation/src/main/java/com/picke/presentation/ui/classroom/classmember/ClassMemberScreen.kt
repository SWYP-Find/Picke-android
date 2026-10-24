package com.picke.presentation.ui.classroom.classmember

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classmember.component.ClassMemberItem
import com.picke.presentation.ui.classroom.classmember.component.ClassNameSheet
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberUiAction
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberUiEvent
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberUiState
import com.picke.presentation.ui.component.CustomTextField
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.dialog.CustomConfirmDialog
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun ClassMemberScreen(
    onBackClick: () -> Unit,
    onMembersChanged: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClassMemberViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val loadFailedMessage = stringResource(R.string.toast_load_failed)
    val nameDuplicatedMessage = stringResource(R.string.class_join_name_duplicated)
    val actionFailedMessage = stringResource(R.string.toast_action_failed)

    BackHandler { viewModel.onAction(ClassMemberUiAction.BackClick) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ClassMemberUiEvent.NavigateBack -> onBackClick()

                ClassMemberUiEvent.NavigateBackAfterMembersChanged -> onMembersChanged()

                ClassMemberUiEvent.ShowLoadFailedToast ->
                    Toast.makeText(context, loadFailedMessage, Toast.LENGTH_SHORT).show()

                ClassMemberUiEvent.ShowNameDuplicatedToast ->
                    Toast.makeText(context, nameDuplicatedMessage, Toast.LENGTH_SHORT).show()

                ClassMemberUiEvent.ShowActionFailedToast ->
                    Toast.makeText(context, actionFailedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ClassMemberContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun ClassMemberContent(
    uiState: ClassMemberUiState,
    onAction: (ClassMemberUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                CustomTopAppBar(
                    title = stringResource(R.string.class_detail_member),
                    onBackClick = { onAction(ClassMemberUiAction.BackClick) },
                    backgroundColor = PickeTheme.colors.backgroundBeige,
                    titleColor = PickeTheme.colors.textSubtler
                )

                ClassMemberSearchSection(
                    query = uiState.query,
                    onQueryChange = { query -> onAction(ClassMemberUiAction.QueryChange(query)) },
                    modifier = Modifier.padding(
                        start = SpacingTokens.s16,
                        top = SpacingTokens.s12,
                        end = SpacingTokens.s16
                    )
                )
            }
        },
        containerColor = PickeTheme.colors.backgroundBeige,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = stringResource(R.string.class_member_count, uiState.members.size),
                modifier = Modifier.padding(
                    start = SpacingTokens.s16,
                    top = SpacingTokens.s20,
                    end = SpacingTokens.s16,
                    bottom = SpacingTokens.s4
                ),
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.bodySmMedium
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = SpacingTokens.s16,
                    end = SpacingTokens.s16,
                    bottom = SpacingTokens.s20
                )
            ) {
                items(items = uiState.filteredMembers, key = { it.memberId }) { member ->
                    ClassMemberItem(
                        member = member,
                        canRemove = uiState.isOperatorViewer && !member.isMe,
                        onEditNameClick = { onAction(ClassMemberUiAction.EditNameClick) },
                        onRemoveClick = { onAction(ClassMemberUiAction.RemoveClick(member)) }
                    )
                    HorizontalDivider(
                        thickness = ComponentNumberTokens.borderWidthRegular,
                        color = PickeTheme.colors.borderBeigeDefault
                    )
                }
            }
        }
    }

    if (uiState.isNameSheetVisible) {
        ClassNameSheet(
            isConfirmEnabled = uiState.isNameConfirmEnabled,
            name = uiState.editingName,
            onNameChange = { name -> onAction(ClassMemberUiAction.EditingNameChange(name)) },
            onConfirm = { onAction(ClassMemberUiAction.NameConfirm) },
            onDismiss = { onAction(ClassMemberUiAction.NameSheetDismiss) }
        )
    }

    uiState.removingMember?.let { member ->
        CustomConfirmDialog(
            message = stringResource(R.string.class_member_remove_confirm, member.name),
            onConfirm = { onAction(ClassMemberUiAction.RemoveConfirm) },
            onDismiss = { onAction(ClassMemberUiAction.RemoveDialogDismiss) },
            confirmText = stringResource(R.string.class_member_remove)
        )
    }
}

@Composable
private fun ClassMemberSearchSection(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    CustomTextField(
        value = query,
        onValueChange = onQueryChange,
        minHeight = ComponentNumberTokens.inputTextfieldHeight,
        textStyle = PickeTheme.typography.bodyXsMedium,
        modifier = modifier,
        placeholder = stringResource(R.string.class_member_search_placeholder),
        leadingIconRes = R.drawable.ic_search
    )
}

@Preview(name = "운영자", showBackground = true)
@Composable
private fun ClassMemberOperatorPreview() {
    PickeTheme {
        ClassMemberContent(
            uiState = ClassMemberUiState(
                isOperatorViewer = true,
                members = DummyData.dummyClassMembers
            ),
            onAction = {}
        )
    }
}

@Preview(name = "참여자", showBackground = true)
@Composable
private fun ClassMemberParticipantPreview() {
    PickeTheme {
        ClassMemberContent(
            uiState = ClassMemberUiState(
                isOperatorViewer = false,
                members = DummyData.dummyParticipantClassMembers
            ),
            onAction = {}
        )
    }
}