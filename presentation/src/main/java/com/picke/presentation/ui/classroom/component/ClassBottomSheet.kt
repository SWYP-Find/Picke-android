package com.picke.presentation.ui.classroom.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassBottomSheetDimens {
    val cornerRadius = 28.dp
    val handleBottomPadding = 10.dp
    val handleWidth = 40.dp
    val handleHeight = 4.dp
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = ClassBottomSheetDimens.cornerRadius, topEnd = ClassBottomSheetDimens.cornerRadius),
        containerColor = PickeTheme.colors.surfaceBeigeDefault,
        dragHandle = { ClassBottomSheetHandle() }
    ) {
        ClassBottomSheetBody(content = content)
    }
}

@Composable
private fun ClassBottomSheetHandle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = SpacingTokens.s12, bottom = ClassBottomSheetDimens.handleBottomPadding),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = ClassBottomSheetDimens.handleWidth, height = ClassBottomSheetDimens.handleHeight)
                .background(PickeTheme.colors.gray50, RoundedCornerShape(RadiusTokens.default))
        )
    }
}

@Composable
private fun ClassBottomSheetBody(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SpacingTokens.s16)
            .navigationBarsPadding()
            .padding(bottom = SpacingTokens.s16),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun ClassBottomSheetBodyPreview() {
    PickeTheme {
        Column(modifier = Modifier.background(PickeTheme.colors.surfaceBeigeDefault)) {
            ClassBottomSheetHandle()
            ClassBottomSheetBody {
                Text(text = "바텀시트 내용", style = PickeTheme.typography.headingMd)
            }
        }
    }
}