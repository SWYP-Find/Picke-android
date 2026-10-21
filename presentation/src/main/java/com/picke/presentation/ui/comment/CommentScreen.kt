package com.picke.presentation.ui.comment

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.comment.component.CommentItemCard
import com.picke.presentation.ui.comment.component.CommentSkeleton
import com.picke.presentation.ui.comment.model.CommentUiEvent
import com.picke.presentation.ui.comment.model.CommentUiModel
import com.picke.presentation.ui.comment.model.CommentUiState
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.PickeCommentInput
import com.picke.presentation.ui.component.dialog.CustomConfirmDialog
import com.picke.presentation.ui.component.dialog.PickeReportDialog
import com.picke.presentation.ui.component.dialog.ReportReason
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CommentScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollToCommentId: Long? = null,
    viewModel: CommentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val inputFieldState = rememberTextFieldState()

    var commentToDelete by remember { mutableStateOf<Long?>(null) }
    var commentToReport by remember { mutableStateOf<Long?>(null) }
    var isMainDeleteDialogVisible by remember { mutableStateOf(false) }
    var isMainReportDialogVisible by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            isRefreshing = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                CommentUiEvent.NavigateBack -> onBackClick()

                is CommentUiEvent.ShowToast -> {
                    Toast.makeText(context, context.getString(event.messageResId), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    CommentContent(
        uiState = uiState,
        scrollToCommentId = scrollToCommentId,
        inputFieldState = inputFieldState,
        isRefreshing = isRefreshing,
        commentToDelete = commentToDelete,
        commentToReport = commentToReport,
        isMainDeleteDialogVisible = isMainDeleteDialogVisible,
        isMainReportDialogVisible = isMainReportDialogVisible,
        onBackClick = onBackClick,
        onRefresh = {
            isRefreshing = true
            viewModel.refreshAllData()
        },
        onSubmit = {
            viewModel.submitComment(inputFieldState.text.toString()) {
                inputFieldState.clearText()
            }
        },
        onMainLikeClick = { mainContent ->
            if (mainContent.isMine) {
                Toast.makeText(context, context.getString(R.string.comment_like_own_perspective), Toast.LENGTH_SHORT).show()
            } else {
                viewModel.toggleMainPerspectiveLike()
            }
        },
        onEditMainPerspective = { content ->
            inputFieldState.setTextAndPlaceCursorAtEnd(content)
            viewModel.setMainPerspectiveEditMode()
        },
        onRequestDeleteMainPerspective = {
            isMainDeleteDialogVisible = true
        },
        onRequestReportMainPerspective = {
            isMainReportDialogVisible = true
        },
        onEditComment = { commentId, content ->
            inputFieldState.setTextAndPlaceCursorAtEnd(content)
            viewModel.setEditMode(commentId)
        },
        onRequestDelete = { commentId ->
            commentToDelete = commentId
        },
        onRequestReport = { commentId ->
            commentToReport = commentId
        },
        onLikeComment = { commentId, isLiked, isMine ->
            if (isMine) {
                Toast.makeText(context, context.getString(R.string.comment_like_own_reply), Toast.LENGTH_SHORT).show()
            } else {
                viewModel.toggleLike(commentId = commentId, isCurrentlyLiked = isLiked)
            }
        },
        onConfirmDelete = {
            commentToDelete?.let { viewModel.deleteComment(it) }
            commentToDelete = null
        },
        onDismissDelete = {
            commentToDelete = null
        },
        onConfirmReport = { _ ->
            // TODO: API 연동 - 신고 사유 전송 (신고 API에 사유 필드 없음)
            commentToReport?.let { viewModel.reportComment(it) }
            commentToReport = null
        },
        onDismissReport = {
            commentToReport = null
        },
        onConfirmDeleteMainPerspective = {
            viewModel.deleteMainPerspective()
            isMainDeleteDialogVisible = false
        },
        onDismissDeleteMainPerspective = {
            isMainDeleteDialogVisible = false
        },
        onConfirmReportMainPerspective = { _ ->
            // TODO: API 연동 - 신고 사유 전송 (신고 API에 사유 필드 없음)
            viewModel.reportMainPerspective()
            isMainReportDialogVisible = false
        },
        onDismissReportMainPerspective = {
            isMainReportDialogVisible = false
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommentContent(
    uiState: CommentUiState,
    scrollToCommentId: Long?,
    inputFieldState: TextFieldState,
    isRefreshing: Boolean,
    commentToDelete: Long?,
    commentToReport: Long?,
    isMainDeleteDialogVisible: Boolean,
    isMainReportDialogVisible: Boolean,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    onSubmit: () -> Unit,
    onMainLikeClick: (CommentUiModel) -> Unit,
    onEditMainPerspective: (String) -> Unit,
    onRequestDeleteMainPerspective: () -> Unit,
    onRequestReportMainPerspective: () -> Unit,
    onEditComment: (Long, String) -> Unit,
    onRequestDelete: (Long) -> Unit,
    onRequestReport: (Long) -> Unit,
    onLikeComment: (Long, Boolean, Boolean) -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDelete: () -> Unit,
    onConfirmReport: (ReportReason) -> Unit,
    onDismissReport: () -> Unit,
    onConfirmDeleteMainPerspective: () -> Unit,
    onDismissDeleteMainPerspective: () -> Unit,
    onConfirmReportMainPerspective: (ReportReason) -> Unit,
    onDismissReportMainPerspective: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(scrollToCommentId, uiState.comments) {
        if (scrollToCommentId != null && uiState.comments.isNotEmpty()) {
            val targetIndex = uiState.comments.indexOfFirst { it.commentId == scrollToCommentId }
            if (targetIndex >= 0) listState.animateScrollToItem(targetIndex)
        }
    }

    Scaffold(
        containerColor = PickeTheme.colors.backgroundBeige,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = stringResource(R.string.comment_reply_title),
                    centerTitle = true,
                    showLogo = false,
                    onBackClick = onBackClick,
                    backgroundColor = PickeTheme.colors.backgroundBeige
                )
            }
        },
        bottomBar = {
            val isEditing = uiState.editingCommentId != null || uiState.isEditingMainPerspective
            val inputHint = stringResource(
                if (isEditing) R.string.comment_input_hint_edit else R.string.comment_input_hint_reply
            )

            PickeCommentInput(
                textFieldState = inputFieldState,
                hintText = inputHint,
                onSubmit = onSubmit,
                editingId = uiState.editingCommentId ?: uiState.targetId.takeIf { uiState.isEditingMainPerspective }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading && uiState.comments.isEmpty() && uiState.mainPerspective == null) {
                CommentSkeleton(modifier = Modifier.fillMaxSize())
            } else {
                uiState.mainPerspective?.let { mainContent ->
                    CommentItemCard(
                        item = mainContent,
                        isMainContent = true,
                        onEditClick = onEditMainPerspective,
                        onDeleteClick = onRequestDeleteMainPerspective,
                        onLikeClick = { onMainLikeClick(mainContent) },
                        onReportClick = onRequestReportMainPerspective
                    )
                }

                CommentHeaderSection(count = uiState.comments.size)

                PullToRefreshBox(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                    modifier = Modifier.weight(1f),
                    indicator = {
                        PullToRefreshDefaults.Indicator(
                            state = pullToRefreshState,
                            isRefreshing = isRefreshing,
                            containerColor = PickeTheme.colors.surfaceBeigeDefault,
                            color = PickeTheme.colors.primary500,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.comments) { comment ->
                            CommentItemCard(
                                item = comment,
                                onEditClick = { content ->
                                    onEditComment(comment.commentId, content)
                                },
                                onDeleteClick = {
                                    onRequestDelete(comment.commentId)
                                },
                                onReportClick = {
                                    onRequestReport(comment.commentId)
                                },
                                onLikeClick = {
                                    onLikeComment(comment.commentId, comment.isLiked, comment.isMine)
                                }
                            )
                        }
                    }
                }
            }
        }

        if (commentToDelete != null) {
            CustomConfirmDialog(
                message = stringResource(R.string.comment_reply_delete_message),
                onConfirm = onConfirmDelete,
                onDismiss = onDismissDelete,
                confirmText = stringResource(R.string.comment_delete_confirm)
            )
        }

        if (isMainDeleteDialogVisible) {
            CustomConfirmDialog(
                message = stringResource(R.string.comment_perspective_delete_message),
                onConfirm = onConfirmDeleteMainPerspective,
                onDismiss = onDismissDeleteMainPerspective,
                confirmText = stringResource(R.string.comment_delete_confirm)
            )
        }

        if (isMainReportDialogVisible) {
            PickeReportDialog(
                onConfirm = onConfirmReportMainPerspective,
                onDismiss = onDismissReportMainPerspective
            )
        }

        if (commentToReport != null) {
            PickeReportDialog(
                onConfirm = onConfirmReport,
                onDismiss = onDismissReport
            )
        }
    }
}

@Composable
private fun CommentHeaderSection(
    count: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            // TODO: 토큰 동기화 - 답글 헤더 배경 (Figma 디자인 없음)
            .background(Color(0xFFF9F8F6))
            .padding(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s12)
    ) {
        Text(
            text = stringResource(R.string.comment_reply_count, count),
            style = PickeTheme.typography.bodyXsSemiBold,
            color = PickeTheme.colors.gray600
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CommentContentPreview() {
    PickeTheme {
        CommentContent(
            uiState = CommentUiState(
                mainPerspective = DummyData.dummyMainPerspective,
                comments = DummyData.dummyComments
            ),
            scrollToCommentId = null,
            inputFieldState = rememberTextFieldState(),
            isRefreshing = false,
            commentToDelete = null,
            commentToReport = null,
            isMainDeleteDialogVisible = false,
            isMainReportDialogVisible = false,
            onBackClick = {},
            onRefresh = {},
            onSubmit = {},
            onMainLikeClick = {},
            onEditMainPerspective = {},
            onRequestDeleteMainPerspective = {},
            onRequestReportMainPerspective = {},
            onEditComment = { _, _ -> },
            onRequestDelete = {},
            onRequestReport = {},
            onLikeComment = { _, _, _ -> },
            onConfirmDelete = {},
            onDismissDelete = {},
            onConfirmReport = {},
            onDismissReport = {},
            onConfirmDeleteMainPerspective = {},
            onDismissDeleteMainPerspective = {},
            onConfirmReportMainPerspective = {},
            onDismissReportMainPerspective = {}
        )
    }
}