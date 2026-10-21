package com.picke.presentation.ui.component.dialog

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object PickeReportDialogDimens {
    val cornerRadius = 6.dp
    val radioSize = 20.dp
    val radioDotSize = 10.dp
}

private const val REASON_COLUMNS = 2

enum class ReportReason(@param:StringRes val labelRes: Int) {
    PROMOTION(R.string.comment_report_reason_promotion),
    SPAM(R.string.comment_report_reason_spam),
    OBSCENE(R.string.comment_report_reason_obscene),
    ABUSE(R.string.comment_report_reason_abuse),
    ETC(R.string.comment_report_reason_etc)
}

@Composable
fun PickeReportDialog(
    onConfirm: (ReportReason) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedReason by rememberSaveable { mutableStateOf<ReportReason?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(PickeReportDialogDimens.cornerRadius),
            color = PickeTheme.colors.beige500,
            border = BorderStroke(
                ComponentNumberTokens.borderWidthMedium,
                PickeTheme.colors.primary500
            )
        ) {
            Column(
                modifier = Modifier.padding(top = SpacingTokens.s20),
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
            ) {
                Text(
                    text = stringResource(R.string.comment_report_title),
                    modifier = Modifier.padding(horizontal = SpacingTokens.s20),
                    color = PickeTheme.colors.primary800,
                    style = PickeTheme.typography.headingSm
                )

                Column(
                    modifier = Modifier.padding(
                        horizontal = SpacingTokens.s20,
                        vertical = SpacingTokens.s12
                    ),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
                ) {
                    ReportReason.entries.chunked(REASON_COLUMNS).forEach { rowReasons ->
                        Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s16)) {
                            rowReasons.forEach { reason ->
                                ReportReasonRadio(
                                    reason = reason,
                                    isSelected = reason == selectedReason,
                                    onClick = { selectedReason = reason },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            repeat(REASON_COLUMNS - rowReasons.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    ReportDialogButton(
                        text = stringResource(R.string.comment_report_confirm),
                        backgroundColor = PickeTheme.colors.secondary50,
                        textColor = if (selectedReason != null) PickeTheme.colors.primary800 else PickeTheme.colors.textMuted,
                        enabled = selectedReason != null,
                        onClick = { selectedReason?.let(onConfirm) },
                        modifier = Modifier.weight(1f)
                    )
                    ReportDialogButton(
                        text = stringResource(R.string.dialog_dismiss),
                        backgroundColor = PickeTheme.colors.primary500,
                        textColor = PickeTheme.colors.secondary50,
                        enabled = true,
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ReportReasonRadio(
    reason: ReportReason,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        ),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(PickeReportDialogDimens.radioSize)
                .border(
                    width = ComponentNumberTokens.borderWidthMedium,
                    color = if (isSelected) PickeTheme.colors.primary500 else PickeTheme.colors.textMuted,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(PickeReportDialogDimens.radioDotSize)
                        .background(PickeTheme.colors.primary500, CircleShape)
                )
            }
        }
        // TODO: 토큰 동기화 - 신고 사유 글자색 (Figma 변수 없이 검정)
        Text(
            text = stringResource(reason.labelRes),
            color = PickeTheme.colors.textDefault,
            style = PickeTheme.typography.bodySmMedium
        )
    }
}

@Composable
private fun ReportDialogButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(SpacingTokens.s12),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = PickeTheme.typography.bodySmMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PickeReportDialogPreview() {
    PickeTheme {
        PickeReportDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}