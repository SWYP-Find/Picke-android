package com.picke.presentation.ui.classroom.classcreate.component.deadline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.component.ClassBottomSheet
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import java.time.LocalDateTime

@Composable
fun ClassDeadlineSheet(
    deadline: LocalDateTime,
    onConfirm: (LocalDateTime) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ClassBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        ClassDeadlineSheetContent(
            pickerState = rememberClassDeadlinePickerState(deadline),
            onConfirm = onConfirm
        )
    }
}

@Composable
private fun ClassDeadlineSheetContent(
    pickerState: ClassDeadlinePickerState,
    onConfirm: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val todayLabel = stringResource(R.string.class_deadline_sheet_today)
    val dateFormat = stringResource(R.string.class_deadline_sheet_date_format)
    val dateLabels = remember { pickerState.dateLabels(todayLabel, dateFormat) }

    Column(
        modifier = modifier.padding(top = SpacingTokens.s20, bottom = SpacingTokens.s8),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s20)
    ) {
        ClassWheelPickerGroup {
            ClassWheelPicker(
                items = dateLabels,
                selectedIndex = pickerState.dateIndex,
                onSelectedIndexChange = pickerState::selectDate,
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.End
            )
            ClassWheelPicker(
                items = pickerState.hourLabels,
                selectedIndex = pickerState.hour,
                onSelectedIndexChange = pickerState::selectHour,
                modifier = Modifier.weight(1f),
                minIndex = pickerState.minHour
            )
            ClassWheelPicker(
                items = pickerState.minuteLabels,
                selectedIndex = pickerState.minute,
                onSelectedIndexChange = pickerState::selectMinute,
                modifier = Modifier.weight(1f),
                minIndex = pickerState.minMinute,
                textAlign = TextAlign.Start
            )
        }

        CustomButton(
            text = stringResource(R.string.class_deadline_sheet_confirm),
            onClick = { onConfirm(pickerState.selectedDeadline) },
            modifier = Modifier.height(ComponentNumberTokens.buttonPrimaryLargeHeight),
            backgroundColor = PickeTheme.colors.buttonPrimaryBackgroundDefault,
            textColor = PickeTheme.colors.buttonPrimaryTextDefault
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassDeadlineSheetContentPreview() {
    PickeTheme {
        ClassDeadlineSheetContent(
            pickerState = rememberClassDeadlinePickerState(LocalDateTime.now()),
            onConfirm = {},
            modifier = Modifier
                .background(PickeTheme.colors.surfaceBeigeDefault)
                .padding(horizontal = SpacingTokens.s16)
        )
    }
}