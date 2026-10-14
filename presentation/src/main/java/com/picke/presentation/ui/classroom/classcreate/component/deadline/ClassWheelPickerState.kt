package com.picke.presentation.ui.classroom.classcreate.component.deadline

import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlin.math.abs

class ClassWheelPickerState(
    val listState: LazyListState,
    private val scrollableState: ScrollableState,
    private val flingBehavior: FlingBehavior
) {
    private var isDragging by mutableStateOf(false)

    val centerIndex: Int by derivedStateOf {
        val layoutInfo = listState.layoutInfo
        val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
        layoutInfo.visibleItemsInfo
            .minByOrNull { item -> abs(item.offset + item.size / 2 - viewportCenter) }
            ?.index ?: listState.firstVisibleItemIndex
    }

    val isFlinging: Boolean
        get() = scrollableState.isScrollInProgress

    val isMoving: Boolean
        get() = isDragging || scrollableState.isScrollInProgress || listState.isScrollInProgress

    val draggableState: DraggableState =
        DraggableState { delta -> scrollableState.dispatchRawDelta(-delta) }

    fun distanceFromCenter(index: Int): Float {
        val layoutInfo = listState.layoutInfo
        val itemInfo = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
        val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2f
        val halfHeight = (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2f
        if (itemInfo == null || halfHeight == 0f) return 0f
        return ((itemInfo.offset + itemInfo.size / 2f - viewportCenter) / halfHeight)
            .coerceIn(-1f, 1f)
    }

    suspend fun startDrag() {
        isDragging = true
        scrollableState.stopScroll()
    }

    suspend fun fling(velocity: Float) {
        scrollableState.scroll {
            isDragging = false
            with(flingBehavior) { performFling(-velocity) }
        }
    }

    suspend fun scrollIntoRange(minIndex: Int) {
        if (centerIndex < minIndex) {
            listState.animateScrollToItem(minIndex)
        }
    }
}

@Composable
fun rememberClassWheelPickerState(
    initialIndex: Int,
    minIndex: Int,
    itemHeight: Dp
): ClassWheelPickerState {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }
    val scrollableState = rememberScrollableState { delta ->
        val currentOffset =
            listState.firstVisibleItemIndex * itemHeightPx + listState.firstVisibleItemScrollOffset
        val minOffset = minIndex * itemHeightPx
        listState.dispatchRawDelta(maxOf(delta, minOffset - currentOffset))
    }
    val flingBehavior = rememberSnapFlingBehavior(
        lazyListState = listState,
        snapPosition = SnapPosition.Center
    )

    return remember(listState, scrollableState, flingBehavior) {
        ClassWheelPickerState(
            listState = listState,
            scrollableState = scrollableState,
            flingBehavior = flingBehavior
        )
    }
}