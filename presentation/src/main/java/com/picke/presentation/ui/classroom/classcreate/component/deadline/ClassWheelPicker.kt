package com.picke.presentation.ui.classroom.classcreate.component.deadline

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import kotlinx.coroutines.flow.filter
import kotlin.math.abs

private object ClassWheelPickerDimens {
    val height = 174.dp
    val itemHeight = 32.dp
    val selectedRadius = 8.dp
    val verticalPadding = (height - itemHeight) / 2
}

private const val UNSELECTED_ITEM_ALPHA = 0.35f
private const val SELECTED_BAND_ALPHA = 0.04f
private const val WHEEL_SCALE_FACTOR = 0.46f
private const val WHEEL_MAX_ROTATION_DEGREES = 50f
private const val WHEEL_CAMERA_DISTANCE = 12f

@Composable
fun ClassWheelPickerGroup(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(ClassWheelPickerDimens.height),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(ClassWheelPickerDimens.itemHeight)
                .background(
                    PickeTheme.colors.textDefault.copy(alpha = SELECTED_BAND_ALPHA),
                    RoundedCornerShape(ClassWheelPickerDimens.selectedRadius)
                )
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s16),
            content = content
        )
    }
}

@Composable
fun ClassWheelPicker(
    items: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minIndex: Int = 0,
    textAlign: TextAlign = TextAlign.Center
) {
    val state = rememberClassWheelPickerState(
        initialIndex = selectedIndex,
        minIndex = minIndex,
        itemHeight = ClassWheelPickerDimens.itemHeight
    )
    val currentOnSelectedIndexChange by rememberUpdatedState(onSelectedIndexChange)

    LaunchedEffect(state) {
        snapshotFlow { state.isMoving }
            .filter { isMoving -> !isMoving }
            .collect { currentOnSelectedIndexChange(state.centerIndex) }
    }

    LaunchedEffect(minIndex) {
        state.scrollIntoRange(minIndex)
    }

    LazyColumn(
        modifier = modifier
            .height(ClassWheelPickerDimens.height)
            .draggable(
                state = state.draggableState,
                orientation = Orientation.Vertical,
                startDragImmediately = state.isFlinging,
                onDragStarted = { state.startDrag() },
                onDragStopped = { velocity -> state.fling(velocity) }
            ),
        state = state.listState,
        contentPadding = PaddingValues(vertical = ClassWheelPickerDimens.verticalPadding),
        userScrollEnabled = false
    ) {
        itemsIndexed(items) { index, item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ClassWheelPickerDimens.itemHeight)
                    .graphicsLayer {
                        val fraction = state.distanceFromCenter(index)
                        val scale = 1f - WHEEL_SCALE_FACTOR * abs(fraction)
                        scaleX = scale
                        scaleY = scale
                        rotationX = -fraction * WHEEL_MAX_ROTATION_DEGREES
                        cameraDistance = WHEEL_CAMERA_DISTANCE * density
                        transformOrigin = textAlign.toTransformOrigin()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    modifier = Modifier.fillMaxWidth(),
                    color = when {
                        index < minIndex -> PickeTheme.colors.gray100
                        index == state.centerIndex -> PickeTheme.colors.textDefault
                        else -> PickeTheme.colors.textDefault.copy(alpha = UNSELECTED_ITEM_ALPHA)
                    },
                    textAlign = textAlign,
                    maxLines = 1,
                    style = PickeTheme.typography.bodyLgRegular
                )
            }
        }
    }
}

private fun TextAlign.toTransformOrigin(): TransformOrigin = when (this) {
    TextAlign.End -> TransformOrigin(1f, 0.5f)
    TextAlign.Start -> TransformOrigin(0f, 0.5f)
    else -> TransformOrigin.Center
}

@Preview(showBackground = true)
@Composable
private fun ClassWheelPickerPreview() {
    var selectedIndex by remember { mutableIntStateOf(9) }

    PickeTheme {
        ClassWheelPickerGroup(modifier = Modifier.padding(SpacingTokens.s16)) {
            ClassWheelPicker(
                items = List(24) { it.toString() },
                selectedIndex = selectedIndex,
                onSelectedIndexChange = { selectedIndex = it },
                modifier = Modifier.weight(1f),
                minIndex = 6
            )
        }
    }
}