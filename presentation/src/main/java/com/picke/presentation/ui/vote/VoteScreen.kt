package com.picke.presentation.ui.vote

import android.content.ClipData
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.picke.presentation.R
import com.picke.presentation.analytics.ShareChannel
import com.picke.presentation.share.captureBitmap
import com.picke.presentation.share.launchBitmapShare
import com.picke.presentation.share.loadBitmapFromUrl
import com.picke.presentation.share.shareBattleToInstagramStoryBrightMode
import com.picke.presentation.share.shareBattleToInstagramStoryDarkMode
import com.picke.presentation.share.shareBattleToKakao
import com.picke.presentation.ui.component.PickeButton
import com.picke.presentation.ui.component.PickeTopAppBar
import com.picke.presentation.ui.component.PickeTopAppBarAction
import com.picke.presentation.ui.component.dialog.PickeSingleActionDialog
import com.picke.presentation.ui.component.dialog.ShareDialog
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.ui.vote.component.VoteBattleNotFound
import com.picke.presentation.ui.vote.component.VoteCommentCountBadge
import com.picke.presentation.ui.vote.component.VoteDeadlineRow
import com.picke.presentation.ui.vote.component.VoteOptionCard
import com.picke.presentation.ui.vote.component.VoteSkeleton
import com.picke.presentation.ui.vote.component.VoteTagChip
import com.picke.presentation.ui.vote.component.VoteVersusBadge
import com.picke.presentation.ui.vote.model.BattleDetailUiModel
import com.picke.presentation.ui.vote.model.BattleInfoUiModel
import com.picke.presentation.ui.vote.model.BattleOptionUiModel
import com.picke.presentation.ui.vote.model.VoteClassInfoUiModel
import com.picke.presentation.ui.vote.model.VoteScreenStyle
import com.picke.presentation.ui.vote.model.VoteType
import com.picke.presentation.ui.vote.model.VoteUiEvent
import com.picke.presentation.ui.vote.model.VoteUiState
import com.picke.presentation.ui.vote.model.voteScreenStyle
import com.picke.presentation.util.DummyData
import kotlinx.coroutines.launch

private const val CLIP_LABEL = "battle_link"

@Composable
fun VoteScreen(
    voteType: VoteType,
    onBackClick: () -> Unit,
    onVoteSubmit: (String) -> Unit,
    onNavigateToExplore: () -> Unit,
    viewModel: VoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val loadFailedMessage = stringResource(R.string.toast_load_failed)
    val actionFailedMessage = stringResource(R.string.toast_action_failed)

    BackHandler { onBackClick() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                VoteUiEvent.ShowLoadFailedToast ->
                    Toast.makeText(context, loadFailedMessage, Toast.LENGTH_SHORT).show()

                VoteUiEvent.ShowActionFailedToast ->
                    Toast.makeText(context, actionFailedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (uiState.isLoading) {
        VoteSkeleton(voteType = voteType, modifier = Modifier.fillMaxSize())
    } else {
        val detail = uiState.battleDetail
        if (detail != null) {
            VoteContent(
                voteType = voteType,
                battleDetail = detail,
                uiState = uiState,
                onBackClick = onBackClick,
                onVoteSubmit = onVoteSubmit,
                onNavigateToExplore = onNavigateToExplore,
                onSubmitVote = { selectedOptionId, onSuccess ->
                    viewModel.submitVote(
                        voteType = voteType,
                        selectedOptionId = selectedOptionId,
                        onSuccess = onSuccess
                    )
                },
                onTrackShare = { channel ->
                    viewModel.trackShare(voteType, channel)
                },
                onGetShareLink = { battleId, onSuccess, onError ->
                    viewModel.getShareLink(
                        battleId = battleId,
                        onSuccess = onSuccess,
                        onError = onError
                    )
                },
                onDismissPointDialog = {
                    viewModel.dismissPointDialog()
                }
            )
        } else {
            VoteBattleNotFound(onBackClick = onBackClick)
        }
    }
}

@Composable
fun VoteContent(
    voteType: VoteType,
    battleDetail: BattleDetailUiModel,
    uiState: VoteUiState,
    onBackClick: () -> Unit,
    onVoteSubmit: (String) -> Unit,
    onNavigateToExplore: () -> Unit,
    onSubmitVote: (String, () -> Unit) -> Unit,
    onTrackShare: (String) -> Unit,
    onGetShareLink: (Int, (String) -> Unit, (String) -> Unit) -> Unit,
    onDismissPointDialog: () -> Unit,
    classInfo: VoteClassInfoUiModel? = null,
    isRequesting: Boolean = false
) {
    val context = LocalContext.current
    val clipboard = LocalClipboard.current

    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    var showShareDialog by remember { mutableStateOf(false) }
    var isSharing by remember { mutableStateOf(false) }
    var selectedOptionId by remember { mutableStateOf<String?>(null) }

    val isPreVote = voteType == VoteType.PRE
    val style = voteScreenStyle(voteType = voteType, classInfo = classInfo)
    val battleInfo = battleDetail.battleInfo

    val imageLoadFailedMessage = stringResource(R.string.share_image_load_failed)
    val shareFailedMessage = stringResource(R.string.share_failed)
    val captureFailedMessage = stringResource(R.string.share_capture_failed)
    val linkCopiedMessage = stringResource(R.string.share_link_copied)

    val onKakaoShareClick = {
        isSharing = true
        coroutineScope.launchBitmapShare(
            loadBitmap = { loadBitmapFromUrl(context, battleInfo.thumbnailUrl) },
            onBitmapReady = { bitmap ->
                shareBattleToKakao(
                    context = context,
                    bitmap = bitmap,
                    battleId = battleInfo.battleId,
                    battleTitle = battleInfo.title,
                    battleDescription = if (isPreVote) battleInfo.summary else battleDetail.description,
                    onComplete = { isSharing = false },
                    onSuccess = { onTrackShare(ShareChannel.KAKAO) }
                )
            },
            onBitmapMissing = {
                isSharing = false
                Toast.makeText(context, imageLoadFailedMessage, Toast.LENGTH_SHORT).show()
            },
            onError = {
                isSharing = false
                Toast.makeText(context, shareFailedMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }

    val onInstaShareClick = {
        isSharing = true
        coroutineScope.launchBitmapShare(
            loadBitmap = { graphicsLayer.captureBitmap() },
            onBitmapReady = { bitmap ->
                if (isPreVote) {
                    shareBattleToInstagramStoryBrightMode(
                        context = context,
                        bitmap = bitmap,
                        onComplete = { isSharing = false },
                        onSuccess = { onTrackShare(ShareChannel.INSTAGRAM) }
                    )
                } else {
                    shareBattleToInstagramStoryDarkMode(
                        context = context,
                        bitmap = bitmap,
                        onComplete = { isSharing = false },
                        onSuccess = { onTrackShare(ShareChannel.INSTAGRAM) }
                    )
                }
            },
            onError = {
                isSharing = false
                Toast.makeText(context, captureFailedMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = style.containerColor,
            contentWindowInsets = WindowInsets(0.dp),
            topBar = {
                Box(modifier = Modifier.statusBarsPadding()) {
                    PickeTopAppBar(
                        title = style.topBarTitle,
                        centerTitle = style.topBarTitle != null,
                        onBackClick = onBackClick,
                        backgroundColor = Color.Transparent,
                        backIconColor = PickeTheme.colors.textInverse,
                        titleColor = PickeTheme.colors.textInverse,
                        actions = {
                            if (style.isShareVisible) {
                                PickeTopAppBarAction(
                                    iconRes = R.drawable.ic_share,
                                    contentDescription = stringResource(R.string.top_app_bar_share),
                                    onClick = { showShareDialog = true },
                                    iconSize = 20.dp,
                                    tint = PickeTheme.colors.textInverse
                                )
                            }
                        }
                    )
                }
            },
            bottomBar = {
                VoteBottomSection(
                    submitText = stringResource(style.submitTextRes),
                    isSubmitEnabled = selectedOptionId != null && !isRequesting,
                    deadlineText = style.deadlineText,
                    onSubmitClick = {
                        selectedOptionId?.let { optionId ->
                            onSubmitVote(optionId) {
                                onVoteSubmit(battleInfo.battleId)
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .background(style.containerColor)
                    .drawWithCache {
                        onDrawWithContent {
                            graphicsLayer.record {
                                this@onDrawWithContent.drawContent()
                            }
                            drawLayer(graphicsLayer)
                        }
                    }
            ) {
                VoteBattleInfoSection(
                    battleInfo = battleInfo,
                    description = battleDetail.description,
                    style = style,
                    isPreVote = isPreVote,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                Spacer(modifier = Modifier.height(24.dp))
                VoteOptionSection(
                    options = battleInfo.options,
                    selectedOptionId = selectedOptionId,
                    isOptionImageVisible = style.isOptionImageVisible,
                    onOptionSelect = { optionId -> selectedOptionId = optionId }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        if (showShareDialog) {
            ShareDialog(
                onKakaoClick = {
                    showShareDialog = false
                    onKakaoShareClick()
                },
                onInstaClick = {
                    showShareDialog = false
                    onInstaShareClick()
                },
                onCopyLinkClick = {
                    showShareDialog = false
                    onGetShareLink(
                        battleInfo.battleId.toInt(),
                        { url ->
                            coroutineScope.launch {
                                clipboard.setClipEntry(
                                    ClipEntry(ClipData.newPlainText(CLIP_LABEL, url))
                                )
                                Toast.makeText(context, linkCopiedMessage, Toast.LENGTH_SHORT)
                                    .show()
                                onTrackShare(ShareChannel.LINK)
                            }
                        },
                        { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onDismiss = { showShareDialog = false }
            )
        }

        if (isSharing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .pointerInput(Unit) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PickeTheme.colors.primary900)
            }
        }

        if (uiState.isInsufficientPoints) {
            PickeSingleActionDialog(
                message = stringResource(R.string.vote_point_shortage_message),
                subMessage = stringResource(R.string.vote_point_shortage_sub_message),
                buttonText = stringResource(R.string.vote_point_shortage_button),
                onDismiss = { onDismissPointDialog() },
                onConfirm = {
                    onDismissPointDialog()
                    onNavigateToExplore()
                }
            )
        }
    }
}

@Composable
private fun VoteBottomSection(
    submitText: String,
    isSubmitEnabled: Boolean,
    deadlineText: String?,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .navigationBarsPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PickeButton(
            text = submitText,
            onClick = onSubmitClick,
            enabled = isSubmitEnabled,
            disabledBackgroundColor = PickeTheme.colors.primary300,
            textColor = PickeTheme.colors.surfaceBeigeDefault
        )
        if (deadlineText != null) {
            VoteDeadlineRow(deadlineText = deadlineText)
        }
    }
}

@Composable
private fun VoteBattleInfoSection(
    battleInfo: BattleInfoUiModel,
    description: String,
    style: VoteScreenStyle,
    isPreVote: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        SubcomposeAsyncImage(
            model = battleInfo.thumbnailUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Transparent,
                                    style.containerColor
                                ),
                                startY = 0f,
                                endY = size.height
                            )
                        )
                    }
                },
            contentScale = ContentScale.Crop,
            loading = {
                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmer(
                            baseColor = if (isPreVote) null else PickeTheme.colors.gray600,
                            highlightColor = if (isPreVote) null else PickeTheme.colors.gray400
                        )
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    battleInfo.tags.forEach { tag ->
                        VoteTagChip(name = tag.name)
                    }
                }
                if (style.commentCount != null) {
                    VoteCommentCountBadge(count = style.commentCount)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = battleInfo.title.replace(", ", ",\n"),
                color = style.titleColor,
                style = PickeTheme.typography.displayMd
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                color = style.descriptionColor,
                style = PickeTheme.typography.bodySmRegular
            )
        }
    }
}

@Composable
private fun VoteOptionSection(
    options: List<BattleOptionUiModel>,
    selectedOptionId: String?,
    isOptionImageVisible: Boolean,
    onOptionSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (options.size >= 2) {
                VoteOptionCard(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight(),
                    option = options[0],
                    isSelected = selectedOptionId == options[0].optionId,
                    onClick = { onOptionSelect(options[0].optionId) },
                    isImageVisible = isOptionImageVisible
                )
                VoteOptionCard(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight(),
                    option = options[1],
                    isSelected = selectedOptionId == options[1].optionId,
                    onClick = { onOptionSelect(options[1].optionId) },
                    isImageVisible = isOptionImageVisible
                )
            }
        }

        VoteVersusBadge()
    }
}

@Preview(showBackground = true)
@Composable
private fun VoteContentPreview() {
    PickeTheme {
        VoteContent(
            voteType = VoteType.PRE,
            battleDetail = DummyData.dummyBattleDetailList.first(),
            uiState = VoteUiState(),
            onBackClick = {},
            onVoteSubmit = {},
            onNavigateToExplore = {},
            onSubmitVote = { _, _ -> },
            onTrackShare = { },
            onGetShareLink = { _, _, _ -> },
            onDismissPointDialog = {}
        )
    }
}

@Preview(name = "AI 배틀 사전 투표", showBackground = true)
@Composable
private fun VoteContentClassPreview() {
    val classItem = DummyData.dummyMyClasses[1]
    val battle = DummyData.dummyClassAiQuestions.first()

    PickeTheme {
        VoteContent(
            voteType = VoteType.PRE,
            battleDetail = DummyData.dummyClassAiBattleDetail,
            uiState = VoteUiState(),
            onBackClick = {},
            onVoteSubmit = {},
            onNavigateToExplore = {},
            onSubmitVote = { _, _ -> },
            onTrackShare = { },
            onGetShareLink = { _, _, _ -> },
            onDismissPointDialog = {},
            classInfo = VoteClassInfoUiModel(
                className = classItem.title,
                commentCount = battle.commentCount,
                deadlineText = "2026. 10. 11. 일요일 23:59까지",
                isAiBattle = battle.isAiQuestion
            )
        )
    }
}