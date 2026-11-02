package com.picke.presentation.ui.classroom.classmyopinion

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.picke.presentation.ui.classroom.classmyopinion.component.ClassMyCommentCard
import com.picke.presentation.ui.classroom.classmyopinion.component.ClassMyOpinionSectionTitle
import com.picke.presentation.ui.classroom.classmyopinion.component.ClassMyReplyCard
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiAction
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiEvent
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiState
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun ClassMyOpinionScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClassMyOpinionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val loadFailedMessage = stringResource(R.string.toast_load_failed)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ClassMyOpinionUiEvent.NavigateBack -> onBackClick()

                ClassMyOpinionUiEvent.ShowLoadFailedToast ->
                    Toast.makeText(context, loadFailedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ClassMyOpinionContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun ClassMyOpinionContent(
    uiState: ClassMyOpinionUiState,
    onAction: (ClassMyOpinionUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PickeTheme.colors.backgroundBeige)
    ) {
        CustomTopAppBar(
            title = stringResource(R.string.class_my_opinion_title),
            onBackClick = { onAction(ClassMyOpinionUiAction.BackClick) },
            backgroundColor = PickeTheme.colors.backgroundBeige,
            titleColor = PickeTheme.colors.textSubtler
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = SpacingTokens.s16,
                top = SpacingTokens.s16,
                end = SpacingTokens.s16,
                bottom = SpacingTokens.s24
            ),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
        ) {
            if (uiState.comments.isNotEmpty()) {
                item {
                    ClassMyOpinionSectionTitle(text = stringResource(R.string.class_my_opinion_comments))
                }
                items(
                    items = uiState.comments,
                    key = { "comment_${it.perspectiveId}" }
                ) { comment ->
                    ClassMyCommentCard(comment = comment)
                }
            }
            if (uiState.replies.isNotEmpty()) {
                item {
                    ClassMyOpinionSectionTitle(
                        text = stringResource(R.string.class_my_opinion_replies),
                        modifier = if (uiState.comments.isNotEmpty()) Modifier.padding(top = SpacingTokens.s8) else Modifier
                    )
                }
                itemsIndexed(
                    items = uiState.replies,
                    key = { _, reply -> "reply_${reply.replyId}" }
                ) { index, reply ->
                    ClassMyReplyCard(reply = reply)

                    if (index == uiState.replies.lastIndex && uiState.canLoadMoreReplies) {
                        LaunchedEffect(reply.replyId) {
                            onAction(ClassMyOpinionUiAction.RepliesEndReach)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassMyOpinionContentPreview() {
    PickeTheme {
        ClassMyOpinionContent(
            uiState = ClassMyOpinionUiState(
                classId = DummyData.dummyMyClasses.first().id,
                comments = DummyData.dummyClassMyComments,
                replies = DummyData.dummyClassMyReplies
            ),
            onAction = {}
        )
    }
}