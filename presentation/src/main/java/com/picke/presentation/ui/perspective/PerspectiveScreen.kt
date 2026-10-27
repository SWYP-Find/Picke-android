package com.picke.presentation.ui.perspective

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.component.CustomTabBar
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.CustomTopAppBarAction
import com.picke.presentation.ui.component.CustomTopAppBarTextAction
import com.picke.presentation.ui.component.PickeCommentInput
import com.picke.presentation.ui.component.PickeEmptyState
import com.picke.presentation.ui.component.SortFilterChip
import com.picke.presentation.ui.component.dialog.CustomConfirmDialog
import com.picke.presentation.ui.component.dialog.PickeReportDialog
import com.picke.presentation.ui.perspective.component.PerspectiveHeaderSkeleton
import com.picke.presentation.ui.perspective.component.PerspectiveItemCard
import com.picke.presentation.ui.perspective.component.PerspectiveListSkeleton
import com.picke.presentation.ui.perspective.component.PerspectiveOpinionBadge
import com.picke.presentation.ui.perspective.component.PerspectiveRatioBar
import com.picke.presentation.ui.perspective.component.PerspectiveStatsOption
import com.picke.presentation.ui.perspective.component.PerspectiveTabBarSkeleton
import com.picke.presentation.ui.perspective.model.PerspectiveSort
import com.picke.presentation.ui.perspective.model.PerspectiveStatus
import com.picke.presentation.ui.perspective.model.PerspectiveUiEvent
import com.picke.presentation.ui.perspective.model.PerspectiveUiState
import com.picke.presentation.ui.perspective.model.PerspectiveVoteOptionUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private object PerspectiveScreenDimens {
    val statsBadgeTopPadding = 10.dp
}

private const val DEFAULT_VOTE_RATIO = 50f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerspectiveScreen(
    onBackClick: () -> Unit,
    onNextClick: (Long) -> Unit,
    onMoreClick: (Long, Long) -> Unit,
    modifier: Modifier = Modifier,
    scrollToPerspectiveId: Long? = null,
    viewModel: PerspectiveViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is PerspectiveUiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        context.getString(event.messageResId),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is PerspectiveUiEvent.NavigateNext -> onNextClick(event.battleId)
            }
        }
    }

    PerspectiveContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onNextClick = { viewModel.moveToNext() },
        onMoreClick = onMoreClick,
        onSubmitPerspective = { content, onSuccess ->
            viewModel.submitPerspective(content, onSuccess)
        },
        onSelectOption = { optionId ->
            viewModel.selectOption(optionId)
        },
        onUpdateSort = { sort ->
            viewModel.updateSort(sort)
        },
        onRefreshAllData = {
            viewModel.refreshAllData()
        },
        onSetEditMode = { id ->
            viewModel.setEditMode(id)
        },
        onDeletePerspective = { id ->
            viewModel.deletePerspective(id)
        },
        onReportPerspective = { id ->
            viewModel.reportPerspective(id)
        },
        onToggleLike = { id, isLiked ->
            viewModel.toggleLike(id, isLiked)
        },
        onLoadPerspectives = {
            viewModel.fetchPerspectives()
        },
        modifier = modifier,
        scrollToPerspectiveId = scrollToPerspectiveId
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerspectiveContent(
    uiState: PerspectiveUiState,
    onBackClick: () -> Unit,
    onNextClick: (Long) -> Unit,
    onMoreClick: (Long, Long) -> Unit,
    onSubmitPerspective: (String, () -> Unit) -> Unit,
    onSelectOption: (Long?) -> Unit,
    onUpdateSort: (PerspectiveSort) -> Unit,
    onRefreshAllData: () -> Unit,
    onSetEditMode: (Long) -> Unit,
    onDeletePerspective: (Long) -> Unit,
    onReportPerspective: (Long) -> Unit,
    onToggleLike: (Long, Boolean) -> Unit,
    onLoadPerspectives: () -> Unit,
    modifier: Modifier = Modifier,
    scrollToPerspectiveId: Long? = null
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val voteOptions = uiState.voteOptions
    val allTabTitle = stringResource(R.string.comment_tab_all)
    val tabList = remember(voteOptions, allTabTitle) {
        listOf(allTabTitle) + voteOptions.map { it.title }
    }

    val inputFieldState = rememberTextFieldState()
    val pagerState = rememberPagerState(pageCount = { tabList.size })
    val coroutineScope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()

    var isRefreshing by remember { mutableStateOf(false) }
    var isSorting by remember { mutableStateOf(false) }
    var perspectiveToDelete by remember { mutableStateOf<Long?>(null) }
    var perspectiveToReport by remember { mutableStateOf<Long?>(null) }
    var scrollToTopTrigger by remember { mutableIntStateOf(0) }
    var hasScrolledToComment by remember { mutableStateOf(false) }

    val isShowingMyPendingOrRejected =
        uiState.myPerspective?.let { it.status != PerspectiveStatus.PUBLISHED } ?: false

    BackHandler {
        onBackClick()
    }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            if (isRefreshing || isSorting) {
                scrollToTopTrigger++
            }
            isRefreshing = false
            isSorting = false
        }
    }

    Scaffold(
        containerColor = PickeTheme.colors.backgroundBeige,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = uiState.battleTitle.ifBlank { stringResource(R.string.comment_perspective_title) },
                    centerTitle = true,
                    showLogo = false,
                    backgroundColor = PickeTheme.colors.backgroundBeige,
                    actions = {
                        if (uiState.isClassBattle) {
                            CustomTopAppBarTextAction(
                                iconRes = R.drawable.ic_report,
                                text = stringResource(R.string.top_app_bar_report),
                                onClick = { onNextClick(uiState.battleId) }
                            )
                        } else {
                            CustomTopAppBarAction(
                                iconRes = R.drawable.ic_arrow_right,
                                contentDescription = stringResource(R.string.top_app_bar_next),
                                onClick = { onNextClick(uiState.battleId) },
                                iconSize = 16.dp
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            val isEditing = uiState.editingPerspectiveId != null
            val inputHint = stringResource(
                if (isEditing) R.string.comment_input_hint_edit else R.string.comment_input_hint_perspective
            )

            PickeCommentInput(
                textFieldState = inputFieldState,
                hintText = inputHint,
                onSubmit = {
                    onSubmitPerspective(inputFieldState.text.toString()) {
                        inputFieldState.clearText()
                        focusManager.clearFocus()
                        scrollToTopTrigger++
                    }
                },
                editingId = uiState.editingPerspectiveId
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (voteOptions.isEmpty()) {
                PerspectiveHeaderSkeleton()
                PerspectiveTabBarSkeleton()
            } else {
                PerspectiveStatsSection(
                    voteOptions = uiState.voteOptions,
                    opinionChanged = uiState.opinionChanged,
                    isOptionImageVisible = !uiState.isAiBattle
                )

                CustomTabBar(
                    tabs = tabList,
                    selectedTab = tabList[pagerState.currentPage],
                    onTabSelected = { selected ->
                        val targetIndex = tabList.indexOf(selected)
                        coroutineScope.launch { pagerState.animateScrollToPage(targetIndex) }
                        val optionId =
                            if (targetIndex == 0) null else voteOptions.getOrNull(targetIndex - 1)?.optionId
                        onSelectOption(optionId)
                    },
                    isScrollable = tabList.size > 3
                )
            }

            LaunchedEffect(pagerState.currentPage) {
                val optionId =
                    if (pagerState.currentPage == 0) null else voteOptions.getOrNull(pagerState.currentPage - 1)?.optionId
                onSelectOption(optionId)
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
            ) { pageIndex ->
                val listState =
                    remember(uiState.sort, uiState.selectedOptionId, pageIndex) { LazyListState() }
                val filteredList = uiState.perspectives

                LaunchedEffect(scrollToTopTrigger) {
                    if (scrollToTopTrigger > 0) {
                        listState.scrollToItem(0)
                    }
                }

                if (pageIndex == 0 && scrollToPerspectiveId != null) {
                    LaunchedEffect(uiState.perspectives) {
                        if (!hasScrolledToComment && uiState.perspectives.isNotEmpty() && !uiState.isLoading) {
                            val targetIndex =
                                uiState.perspectives.indexOfFirst { it.perspectiveId == scrollToPerspectiveId }
                            if (targetIndex >= 0) {
                                val offset = if (isShowingMyPendingOrRejected) 1 else 0
                                listState.animateScrollToItem(targetIndex + offset)
                                hasScrolledToComment = true
                            }
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = SpacingTokens.s16,
                                top = SpacingTokens.s16,
                                end = SpacingTokens.s16,
                                bottom = SpacingTokens.s6
                            ),
                        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
                    ) {
                        PerspectiveSort.entries.forEach { sort ->
                            SortFilterChip(
                                text = stringResource(sort.labelRes()),
                                isSelected = uiState.sort == sort,
                                onClick = {
                                    if (uiState.sort != sort) {
                                        isSorting = true
                                        onUpdateSort(sort)
                                    }
                                }
                            )
                        }
                    }

                    if (uiState.isLoading && uiState.perspectives.isEmpty()) {
                        PerspectiveListSkeleton(modifier = Modifier.weight(1f))
                    } else {
                        PullToRefreshBox(
                            state = pullToRefreshState,
                            isRefreshing = isRefreshing,
                            onRefresh = {
                                isRefreshing = true
                                onRefreshAllData()
                            },
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
                                uiState.myPerspective?.let { myView ->
                                    if (myView.status != PerspectiveStatus.PUBLISHED) {
                                        item {
                                            PerspectiveItemCard(
                                                item = myView,
                                                clickable = false,
                                                onEditClick = {
                                                    inputFieldState.setTextAndPlaceCursorAtEnd(it)
                                                    onSetEditMode(myView.perspectiveId)
                                                },
                                                onDeleteClick = {
                                                    perspectiveToDelete = myView.perspectiveId
                                                },
                                                onLikeClick = {
                                                    Toast.makeText(
                                                        context,
                                                        context.getString(R.string.comment_like_own_perspective),
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            )
                                        }
                                    }
                                }

                                if (filteredList.isEmpty() && !uiState.isLoading && !isShowingMyPendingOrRejected) {
                                    item {
                                        val emptyMsg = if (pageIndex == 0) {
                                            stringResource(R.string.comment_empty_all)
                                        } else {
                                            stringResource(R.string.comment_empty_option, tabList.getOrElse(pageIndex) { "" })
                                        }

                                        PickeEmptyState(
                                            message = emptyMsg,
                                            modifier = Modifier.fillParentMaxSize()
                                        )
                                    }
                                } else {
                                    itemsIndexed(
                                        items = filteredList,
                                        key = { _, item -> item.perspectiveId }
                                    ) { index, item ->
                                        PerspectiveItemCard(
                                            item = item,
                                            onMoreClick = {
                                                onMoreClick(
                                                    item.perspectiveId,
                                                    uiState.voteOptions.firstOrNull()
                                                        ?.optionId ?: 0L
                                                )
                                            },
                                            onEditClick = { content ->
                                                inputFieldState.setTextAndPlaceCursorAtEnd(content)
                                                onSetEditMode(item.perspectiveId)
                                            },
                                            onDeleteClick = {
                                                perspectiveToDelete = item.perspectiveId
                                            },
                                            onReportClick = {
                                                perspectiveToReport = item.perspectiveId
                                            },
                                            onLikeClick = {
                                                if (item.isMine) {
                                                    Toast.makeText(
                                                        context,
                                                        context.getString(R.string.comment_like_own_perspective),
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    onToggleLike(
                                                        item.perspectiveId,
                                                        item.isLiked
                                                    )
                                                }
                                            }
                                        )
                                        val isAtEnd = index == filteredList.lastIndex
                                        val isNotLoading = !uiState.isLoading
                                        val hasMorePages = uiState.hasNext

                                        if (isAtEnd && isNotLoading && hasMorePages) {
                                            LaunchedEffect(item.perspectiveId) {
                                                onLoadPerspectives()
                                            }
                                        }
                                    }
                                }

                                if (uiState.isLoading && !isRefreshing) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(color = PickeTheme.colors.primary900)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (perspectiveToDelete != null) {
            CustomConfirmDialog(
                message = stringResource(R.string.comment_perspective_delete_message),
                onConfirm = {
                    onDeletePerspective(perspectiveToDelete!!)
                    perspectiveToDelete = null
                },
                onDismiss = {
                    perspectiveToDelete = null
                },
                confirmText = stringResource(R.string.comment_delete_confirm)
            )
        }

        if (perspectiveToReport != null) {
            PickeReportDialog(
                onConfirm = { _ ->
                    // TODO: API 연동 - 신고 사유 전송 (신고 API에 사유 필드 없음)
                    onReportPerspective(perspectiveToReport!!)
                    perspectiveToReport = null
                },
                onDismiss = {
                    perspectiveToReport = null
                }
            )
        }
    }
}

@Composable
private fun PerspectiveStatsSection(
    voteOptions: List<PerspectiveVoteOptionUiModel>,
    opinionChanged: Boolean,
    isOptionImageVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val leftOption = voteOptions.getOrNull(0)
    val rightOption = voteOptions.getOrNull(1)
    val leftRatio = leftOption?.ratio ?: DEFAULT_VOTE_RATIO

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s4),
        verticalAlignment = if (isOptionImageVisible) Alignment.Bottom else Alignment.CenterVertically
    ) {
        PerspectiveStatsOption(
            imageUrl = leftOption?.imageUrl,
            title = leftOption?.title.orEmpty(),
            ratio = leftRatio,
            isImageVisible = isOptionImageVisible
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = PerspectiveScreenDimens.statsBadgeTopPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PerspectiveOpinionBadge(opinionChanged = opinionChanged)
            PerspectiveRatioBar(ratio = leftRatio)
        }

        PerspectiveStatsOption(
            imageUrl = rightOption?.imageUrl,
            title = rightOption?.title.orEmpty(),
            ratio = rightOption?.ratio ?: DEFAULT_VOTE_RATIO,
            isImageVisible = isOptionImageVisible
        )
    }
}

@StringRes
private fun PerspectiveSort.labelRes(): Int = when (this) {
    PerspectiveSort.POPULAR -> R.string.comment_sort_popular
    PerspectiveSort.LATEST -> R.string.comment_sort_latest
}

@Preview(showBackground = true)
@Composable
private fun PerspectiveContentPreview() {
    PickeTheme {
        PerspectiveContent(
            uiState = PerspectiveUiState(
                voteOptions = DummyData.dummyVoteOptions,
                perspectives = DummyData.dummyPerspectives
            ),
            onBackClick = {},
            onNextClick = {},
            onMoreClick = { _, _ -> },
            onSubmitPerspective = { _, _ -> },
            onSelectOption = {},
            onUpdateSort = {},
            onRefreshAllData = {},
            onSetEditMode = {},
            onDeletePerspective = {},
            onReportPerspective = {},
            onToggleLike = { _, _ -> },
            onLoadPerspectives = {}
        )
    }
}