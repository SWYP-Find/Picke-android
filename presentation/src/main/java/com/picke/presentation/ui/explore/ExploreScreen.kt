package com.picke.presentation.ui.explore

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.picke.presentation.BuildConfig
import com.picke.presentation.R
import com.picke.presentation.ads.AdFitBannerAd
import com.picke.presentation.ui.component.CustomTabBar
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.PickeEmptyState
import com.picke.presentation.ui.component.SortFilterChip
import com.picke.presentation.ui.explore.component.ExploreCard
import com.picke.presentation.ui.explore.component.ExploreSkeleton
import com.picke.presentation.ui.explore.model.ExploreUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel(),
    scrollToTopTrigger: Int = 0,
    onNavigateToAlarm: () -> Unit,
    onNavigateToVote: (String) -> Unit,
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedSort by viewModel.selectedSort.collectAsState()
    val pagingItems = viewModel.explorePagingData.collectAsLazyPagingItems()
    val context = LocalContext.current

    val loadFailedMessage = stringResource(R.string.toast_load_failed)

    var isRefreshing by remember { mutableStateOf(false) }

    val refreshState = pagingItems.loadState.refresh
    val appendState = pagingItems.loadState.append

    LaunchedEffect(refreshState) {
        if (refreshState !is LoadState.Loading) isRefreshing = false
    }

    LaunchedEffect(refreshState, appendState) {
        if (refreshState is LoadState.Error || appendState is LoadState.Error) {
            Toast.makeText(context, loadFailedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    ExploreScreen(
        selectedCategory = selectedCategory,
        selectedSort = selectedSort,
        pagingItems = pagingItems,
        isRefreshing = isRefreshing,
        scrollToTopTrigger = scrollToTopTrigger,
        onCategoryChange = { viewModel.updateCategory(it) },
        onSortChange = { viewModel.updateSort(it) },
        onRefresh = {
            isRefreshing = true
            pagingItems.refresh()
        },
        onNavigateToAlarm = onNavigateToAlarm,
        onNavigateToVote = onNavigateToVote
    )
}

@Composable
fun ExploreScreen(
    selectedCategory: String,
    selectedSort: String,
    pagingItems: LazyPagingItems<ExploreUiModel>,
    isRefreshing: Boolean,
    scrollToTopTrigger: Int,
    onCategoryChange: (String) -> Unit,
    onSortChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onNavigateToAlarm: () -> Unit,
    onNavigateToVote: (String) -> Unit
) {
    val exploreCategories = listOf("전체", "철학", "문학", "예술", "과학", "사회", "역사")
    val pagerState = rememberPagerState(pageCount = { exploreCategories.size })
    val coroutineScope = rememberCoroutineScope()
    var hasUnreadNotification by remember { mutableStateOf(false) }

    val isLoading = pagingItems.loadState.refresh is LoadState.Loading && !isRefreshing

    LaunchedEffect(pagerState.currentPage) {
        val currentCategory = exploreCategories[pagerState.currentPage]
        if (selectedCategory != currentCategory) {
            onCategoryChange(currentCategory)
        }
    }

    Scaffold(
        containerColor = PickeTheme.colors.backgroundBeige,
        topBar = {
            CustomTopAppBar(
                showLogo = true,
                centerTitle = false,
                backgroundColor = PickeTheme.colors.backgroundBeige,
                /*actions = {
                    IconButton(
                        onClick = {
                            onNavigateToAlarm()
                            hasUnreadNotification = false
                        }
                    ) {
                        BadgedBox(
                            badge = {
                                if (hasUnreadNotification) {
                                    Badge(containerColor = PickeTheme.colors.primary500)
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_alarm),
                                contentDescription = "알림",
                                tint = Color.Unspecified
                            )
                        }
                    }
                }*/
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTabBar(
                tabs = exploreCategories,
                selectedTab = selectedCategory,
                onTabSelected = { clickedCategory ->
                    val targetPage = exploreCategories.indexOf(clickedCategory)
                    coroutineScope.launch { pagerState.animateScrollToPage(targetPage) }
                },
                isScrollable = false
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) { _ ->
                ExploreList(
                    pagingItems = pagingItems,
                    isLoading = isLoading,
                    isRefreshing = isRefreshing,
                    selectedSort = selectedSort,
                    scrollToTopTrigger = scrollToTopTrigger,
                    onSortChanged = onSortChange,
                    onRefresh = onRefresh,
                    onNavigateToVote = onNavigateToVote
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreList(
    pagingItems: LazyPagingItems<ExploreUiModel>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    selectedSort: String,
    scrollToTopTrigger: Int = 0,
    onSortChanged: (String) -> Unit,
    onRefresh: () -> Unit,
    onNavigateToVote: (String) -> Unit
) {
    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(scrollToTopTrigger) {
        if (scrollToTopTrigger > 0) {
            listState.animateScrollToItem(0)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SortFilterChip(
                text = stringResource(R.string.explore_hot_rank),
                isSelected = selectedSort == "POPULAR",
                onClick = { onSortChanged("POPULAR") }
            )
            SortFilterChip(
                text = stringResource(R.string.explore_recent_rank),
                isSelected = selectedSort == "LATEST",
                onClick = { onSortChanged("LATEST") }
            )
        }

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize(),
            state = pullToRefreshState,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    modifier = Modifier.align(Alignment.TopCenter),
                    containerColor = PickeTheme.colors.surfaceBeigeDefault,
                    color = PickeTheme.colors.primary500
                )
            }
        ) {
            if (isLoading) {
                ExploreSkeleton(modifier = Modifier.fillMaxSize())
            } else if (pagingItems.itemCount == 0) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        PickeEmptyState(
                            message = stringResource(R.string.explore_empty),
                            modifier = Modifier.fillParentMaxSize()
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val adCount = pagingItems.itemCount / 3
                    val totalCount = pagingItems.itemCount + adCount

                    items(count = totalCount) { displayIndex ->
                        val cycleIndex = displayIndex % 4
                        val cycleNumber = displayIndex / 4

                        if (cycleIndex < 3) {
                            val battleIndex = cycleNumber * 3 + cycleIndex
                            if (battleIndex < pagingItems.itemCount) {
                                pagingItems[battleIndex]?.let { item ->
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = PickeTheme.colors.borderBeigeDefault,
                                    )
                                    ExploreCard(
                                        item = item,
                                        onClick = { id -> onNavigateToVote(id) }
                                    )
                                    if (battleIndex == pagingItems.itemCount - 1) {
                                        HorizontalDivider(
                                            thickness = 1.dp,
                                            color = PickeTheme.colors.borderBeigeDefault,
                                        )
                                    }
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                AdFitBannerAd(adUnitId = BuildConfig.ADFIT_BANNER_EXPLORE)
                            }
                        }
                    }

                    if (pagingItems.loadState.append is LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = PickeTheme.colors.primary900,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    PickeTheme {
        val dummyPagingItems =
            flowOf(PagingData.from(DummyData.dummyExploreList)).collectAsLazyPagingItems()

        ExploreScreen(
            selectedCategory = "전체",
            selectedSort = "POPULAR",
            pagingItems = dummyPagingItems,
            isRefreshing = false,
            scrollToTopTrigger = 0,
            onCategoryChange = {},
            onSortChange = {},
            onRefresh = {},
            onNavigateToAlarm = {},
            onNavigateToVote = {}
        )
    }
}