package com.picke.presentation.ui.classroom.myclass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.component.ClassManageSheet
import com.picke.presentation.ui.classroom.myclass.component.MyClassCard
import com.picke.presentation.ui.classroom.myclass.model.MyClassFilter
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiAction
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiEvent
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiState
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.SortFilterChip
import com.picke.presentation.ui.component.dialog.CustomConfirmDialog
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun MyClassScreen(
    onBackClick: () -> Unit,
    onClassClick: (classId: Long) -> Unit,
    onShareCodeClick: (classId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyClassViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                MyClassUiEvent.NavigateBack -> onBackClick()
                is MyClassUiEvent.NavigateToDetail -> onClassClick(event.classId)
                is MyClassUiEvent.NavigateToShare -> onShareCodeClick(event.classId)
            }
        }
    }

    MyClassContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun MyClassContent(
    uiState: MyClassUiState,
    onAction: (MyClassUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val managingClass = uiState.managingClass

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PickeTheme.colors.backgroundBeige)
    ) {
        CustomTopAppBar(
            title = stringResource(R.string.my_class_title),
            onBackClick = { onAction(MyClassUiAction.BackClick) },
            backgroundColor = PickeTheme.colors.backgroundBeige,
            titleColor = PickeTheme.colors.textSubtler
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                horizontal = SpacingTokens.s16,
                vertical = SpacingTokens.s20
            ),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
        ) {
            item(key = "filter") {
                MyClassFilterSection(
                    selectedFilter = uiState.selectedFilter,
                    onFilterSelect = { filter -> onAction(MyClassUiAction.FilterSelect(filter)) }
                )
            }

            items(items = uiState.filteredClasses, key = { it.id }) { item ->
                MyClassCard(
                    item = item,
                    onClick = { onAction(MyClassUiAction.ClassClick(item.id)) },
                    onMoreClick = { onAction(MyClassUiAction.MoreClick(item.id)) }
                )
            }
        }
    }

    if (managingClass != null && !uiState.isDeleteDialogVisible && !uiState.isLeaveDialogVisible) {
        ClassManageSheet(
            isOperator = managingClass.isOperator,
            onEditClick = { onAction(MyClassUiAction.ManageEditClick) },
            onShareCodeClick = { onAction(MyClassUiAction.ManageShareCodeClick) },
            onDeleteClick = { onAction(MyClassUiAction.ManageDeleteClick) },
            onLeaveClick = { onAction(MyClassUiAction.ManageLeaveClick) },
            onDismiss = { onAction(MyClassUiAction.ManageSheetDismiss) }
        )
    }

    if (uiState.isDeleteDialogVisible) {
        CustomConfirmDialog(
            message = stringResource(R.string.class_delete_confirm),
            onConfirm = { onAction(MyClassUiAction.DeleteConfirm) },
            onDismiss = { onAction(MyClassUiAction.DeleteDialogDismiss) },
            confirmText = stringResource(R.string.class_delete)
        )
    }

    if (uiState.isLeaveDialogVisible) {
        CustomConfirmDialog(
            message = stringResource(R.string.class_leave_confirm),
            onConfirm = { onAction(MyClassUiAction.LeaveConfirm) },
            onDismiss = { onAction(MyClassUiAction.LeaveDialogDismiss) },
            confirmText = stringResource(R.string.class_leave)
        )
    }
}

@Composable
private fun MyClassFilterSection(
    selectedFilter: MyClassFilter,
    onFilterSelect: (MyClassFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(bottom = SpacingTokens.s8),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
    ) {
        MyClassFilter.entries.forEach { filter ->
            SortFilterChip(
                text = stringResource(filter.labelRes),
                isSelected = selectedFilter == filter,
                onClick = { onFilterSelect(filter) }
            )
        }
    }
}

@Preview(name = "운영자", showBackground = true)
@Composable
private fun MyClassOperatorPreview() {
    PickeTheme {
        MyClassContent(
            uiState = MyClassUiState(
                classes = DummyData.dummyMyClasses.map { it.copy(isOperator = true) }
            ),
            onAction = {}
        )
    }
}

@Preview(name = "참여자", showBackground = true)
@Composable
private fun MyClassMemberPreview() {
    PickeTheme {
        MyClassContent(
            uiState = MyClassUiState(
                classes = DummyData.dummyMyClasses.map { it.copy(isOperator = false) }
            ),
            onAction = {}
        )
    }
}