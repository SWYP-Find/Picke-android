package com.picke.presentation.ui.todaybattle

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.picke.presentation.R
import com.picke.presentation.analytics.ShareChannel
import com.picke.presentation.analytics.ShareTarget
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.CustomTopAppBarAction
import com.picke.presentation.ui.component.dialog.ShareDialog
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.ui.todaybattle.component.OpinionCard
import com.picke.presentation.ui.todaybattle.component.TodayBattleSkeleton
import com.picke.presentation.ui.todaybattle.component.TopIndicatorBar
import com.picke.presentation.ui.todaybattle.model.TodayBattleUiModel
import com.picke.presentation.ui.todaybattle.model.TodayBattleUiState
import com.picke.presentation.util.DummyData
import com.picke.presentation.util.captureBitmap
import com.picke.presentation.util.launchBitmapShare
import com.picke.presentation.util.loadBitmapFromUrl
import com.picke.presentation.util.shareBattleToInstagramStoryDarkMode
import com.picke.presentation.util.shareBattleToKakao

@Composable
fun TodayBattleScreen(
    viewModel: TodayBattleViewModel = hiltViewModel(),
    initialBattleId: String? = null,
    onBackClick: () -> Unit,
    onNavigateToScenario: (String) -> Unit,
    onNavigateToPerspective: (String) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    TodayBattleScreen(
        uiState = uiState,
        initialBattleId = initialBattleId,
        onBackClick = onBackClick,
        onEnterBattle = { currentBattleId, selectedOptionId ->
            viewModel.enterBattle(
                battleId = currentBattleId.toLong(),
                optionId = selectedOptionId!!.toLong(),
                onNavigateToScenario = onNavigateToScenario,
                onNavigateToPerspective = onNavigateToPerspective
            )
        },
        onGetShareLink = { currentBattleId ->
            viewModel.getShareLink(
                battleId = currentBattleId,
                onSuccess = { url ->
                    clipboardManager.setText(AnnotatedString(url))
                    Toast.makeText(context, "링크가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
                    viewModel.trackShareAction(ShareTarget.BATTLE, ShareChannel.LINK)
                },
                onError = { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
        },
        onTrackShareAction = viewModel::trackShareAction
    )
}

@Composable
fun TodayBattleScreen(
    uiState: TodayBattleUiState,
    initialBattleId: String?,
    onBackClick: () -> Unit,
    onEnterBattle: (String, String?) -> Unit,
    onGetShareLink: (Int) -> Unit,
    onTrackShareAction: (String, String) -> Unit
) {
    val context = LocalContext.current

    val battleList = uiState.battleList

    val pagerState = rememberPagerState(pageCount = { battleList.size })
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    var selectedOptionId by remember(pagerState.currentPage) { mutableStateOf<String?>(null) }
    var showShareDialog by remember { mutableStateOf(false) }
    var isSharing by remember { mutableStateOf(false) }

    val isButtonEnabled = selectedOptionId != null && !uiState.isEntering
    val currentBattle = if (battleList.isNotEmpty()) battleList[pagerState.currentPage] else null

    val onKakaoShareClick = {
        currentBattle?.let { battle ->
            isSharing = true
            coroutineScope.launchBitmapShare(
                loadBitmap = { loadBitmapFromUrl(context, battle.imageUrl) },
                onBitmapReady = { bitmap ->
                    shareBattleToKakao(
                        context = context,
                        bitmap = bitmap,
                        battleId = battle.battleId,
                        battleTitle = battle.title,
                        battleDescription = battle.description,
                        onComplete = { isSharing = false },
                        onSuccess = { onTrackShareAction(ShareTarget.BATTLE, ShareChannel.KAKAO) }
                    )
                },
                onBitmapMissing = {
                    isSharing = false
                    Toast.makeText(context, "이미지 로드 실패", Toast.LENGTH_SHORT).show()
                },
                onError = {
                    isSharing = false
                    Toast.makeText(context, "공유 실패", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    val onInstaShareClick = {
        isSharing = true
        coroutineScope.launchBitmapShare(
            loadBitmap = { graphicsLayer.captureBitmap() },
            onBitmapReady = { bitmap ->
                shareBattleToInstagramStoryDarkMode(
                    context = context,
                    bitmap = bitmap,
                    onComplete = { isSharing = false },
                    onSuccess = { onTrackShareAction(ShareTarget.BATTLE, ShareChannel.INSTAGRAM) }
                )
            },
            onError = {
                isSharing = false
                Toast.makeText(context, "캡처 실패", Toast.LENGTH_SHORT).show()
            }
        )
    }

    LaunchedEffect(battleList, initialBattleId) {
        if (initialBattleId != null && battleList.isNotEmpty()) {
            val targetPage = battleList.indexOfFirst { it.battleId == initialBattleId }
            if (targetPage >= 0) pagerState.animateScrollToPage(targetPage)
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            TodayBattleSkeleton(modifier = Modifier.fillMaxSize())

            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    onBackClick = onBackClick,
                    backgroundColor = Color.Transparent,
                    backIconColor = PickeTheme.colors.textInverse
                )
            }
        }

        return
    }

    if (battleList.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    onBackClick = onBackClick,
                    backgroundColor = Color.Transparent,
                    backIconColor = PickeTheme.colors.textInverse
                )
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo_picke),
                    contentDescription = "빈 화면 로고",
                    modifier = Modifier.size(width = 160.dp, height = 120.dp),
                    tint = PickeTheme.colors.borderBeigeDefault
                )
                Text(
                    text = "아직 빠른 배틀이 선정되지 않았어요\n조금만 기다려주세요!",
                    style = PickeTheme.typography.bodySmRegular,
                    color = PickeTheme.colors.surfaceBeigeStrong,
                    textAlign = TextAlign.Center
                )
            }
        }

        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Black,
            bottomBar = {
                CustomButton(
                    text = stringResource(R.string.battle_start),
                    onClick = {
                        if (isButtonEnabled) {
                            val currentBattleId = battleList[pagerState.currentPage].battleId
                            onEnterBattle(currentBattleId, selectedOptionId)
                        }
                    },
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(20.dp),
                    backgroundColor = if (isButtonEnabled) PickeTheme.colors.primary500 else PickeTheme.colors.primary300,
                    textColor = PickeTheme.colors.surfaceBeigeDefault
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .background(Color.Black)
                    .drawWithCache {
                        onDrawWithContent {
                            graphicsLayer.record {
                                this@onDrawWithContent.drawContent()
                            }
                            drawLayer(graphicsLayer)
                        }
                    }
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    BattleContent(
                        item = battleList[page],
                        selectedOptionId = selectedOptionId,
                        onOptionSelect = { optionId -> selectedOptionId = optionId }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                ) {
                    if (battleList.size > 1) {
                        Box(
                            modifier = Modifier.padding(
                                start = SpacingTokens.s16,
                                top = SpacingTokens.s16,
                                end = SpacingTokens.s16
                            )
                        ) {
                            TopIndicatorBar(
                                currentPage = pagerState.currentPage,
                                totalPages = battleList.size
                            )
                        }
                    }

                    CustomTopAppBar(
                        onBackClick = onBackClick,
                        backgroundColor = Color.Transparent,
                        backIconColor = PickeTheme.colors.textInverse,
                        actions = {
                            CustomTopAppBarAction(
                                iconRes = R.drawable.ic_share,
                                contentDescription = stringResource(R.string.top_app_bar_share),
                                onClick = { showShareDialog = true },
                                tint = PickeTheme.colors.textInverse
                            )
                        }
                    )
                }
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
                    val currentBattleId = battleList[pagerState.currentPage].battleId.toInt()
                    onGetShareLink(currentBattleId)
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
    }
}

@Composable
fun BattleContent(
    item: TodayBattleUiModel,
    selectedOptionId: String?,
    onOptionSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            SubcomposeAsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.6f),
                                        Color.Black
                                    ),
                                    startY = 0f,
                                    endY = size.height
                                )
                            )
                        }
                    },
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmer(PickeTheme.colors.gray600, PickeTheme.colors.gray400)
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item.tags.forEach { tag ->
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(2.dp)
                        ) {
                            Text(
                                text = "#$tag",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = PickeTheme.typography.captionLgMedium,
                                color = PickeTheme.colors.primary500
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = item.title,
                    style = PickeTheme.typography.displayMd,
                    color = PickeTheme.colors.surfaceBeigeDefault,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = item.description,
                    style = PickeTheme.typography.bodySmRegular,
                    color = PickeTheme.colors.gray400,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(2.dp),
                    border = BorderStroke(1.dp, PickeTheme.colors.textSubtle)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_clock),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            item.timeLeft,
                            style = PickeTheme.typography.captionSmSemiBold,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                if (item.options.isNotEmpty()) {
                    val optionA = item.options[0]
                    OpinionCard(
                        name = optionA.name,
                        opinion = optionA.opinion,
                        quote = optionA.quote,
                        isSelected = selectedOptionId == optionA.optionId,
                        onClick = { onOptionSelect(optionA.optionId) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                if (item.options.size > 1) {
                    val optionB = item.options[1]
                    OpinionCard(
                        name = optionB.name,
                        opinion = optionB.opinion,
                        quote = optionB.quote,
                        isSelected = selectedOptionId == optionB.optionId,
                        onClick = { onOptionSelect(optionB.optionId) }
                    )
                }
            }

            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = PickeTheme.colors.secondary200
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("VS", style = PickeTheme.typography.bodySmSemiBold, color = Color.Black)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TodayBattleScreenPreview() {
    PickeTheme {
        TodayBattleScreen(
            uiState = TodayBattleUiState(
                battleList = DummyData.dummyTodayBattles
            ),
            initialBattleId = "",
            onBackClick = { },
            onEnterBattle = { _, _ -> },
            onGetShareLink = { },
            onTrackShareAction = { _, _ -> }
        )
    }
}