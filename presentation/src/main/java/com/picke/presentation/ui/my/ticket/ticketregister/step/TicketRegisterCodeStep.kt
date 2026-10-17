package com.picke.presentation.ui.my.ticket.ticketregister.step

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.my.ticket.component.TicketHeadline
import com.picke.presentation.ui.my.ticket.ticketregister.component.TicketCodeField
import com.picke.presentation.ui.my.ticket.ticketregister.component.TicketCodeGuideItem
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketCodeGuide
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
internal fun TicketRegisterCodeStep(
    code: String,
    isCodeCheckEnabled: Boolean,
    onCodeChange: (String) -> Unit,
    onCodeCheckClick: () -> Unit,
    modifier: Modifier = Modifier,
    initiallyExpandedGuides: Boolean = false
) {
    // TODO: 토큰 동기화 - button/secondary-fill-disabled, text/disabled
    val buttonBackgroundColor = if (isCodeCheckEnabled) {
        PickeTheme.colors.buttonPrimaryBackgroundDefault
    } else {
        PickeTheme.colors.buttonPrimaryBackgroundDisabled
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s40)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s32)) {
            TicketHeadline(
                title = stringResource(R.string.ticket_register_code_headline),
                description = stringResource(R.string.ticket_register_code_description)
            )
            TicketCodeField(
                code = code,
                onCodeChange = onCodeChange
            )

            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)) {
                TicketCodeGuide.entries.forEach { guide ->
                    TicketCodeGuideItem(
                        question = stringResource(guide.questionRes()),
                        answer = stringResource(guide.answerRes()),
                        initiallyExpanded = initiallyExpandedGuides
                    )
                }
            }
        }

        CustomButton(
            text = stringResource(R.string.ticket_register_code_check),
            onClick = onCodeCheckClick,
            backgroundColor = buttonBackgroundColor,
            textColor = PickeTheme.colors.buttonPrimaryTextDefault,
            enabled = isCodeCheckEnabled,
            minHeight = ComponentNumberTokens.buttonPrimaryLargeHeight
        )
    }
}

@StringRes
private fun TicketCodeGuide.questionRes(): Int = when (this) {
    TicketCodeGuide.CODE_LOCATION -> R.string.ticket_register_guide_code_location
    TicketCodeGuide.JOIN_CODE -> R.string.ticket_register_guide_join_code
}

@StringRes
private fun TicketCodeGuide.answerRes(): Int = when (this) {
    TicketCodeGuide.CODE_LOCATION -> R.string.ticket_register_guide_code_location_answer
    TicketCodeGuide.JOIN_CODE -> R.string.ticket_register_guide_join_code_answer
}

@Preview(name = "입력 전", showBackground = true)
@Composable
private fun TicketRegisterCodeStepPreview() {
    PickeTheme {
        TicketRegisterCodeStep(
            code = "",
            isCodeCheckEnabled = false,
            onCodeChange = {},
            onCodeCheckClick = {}
        )
    }
}

@Preview(name = "입력 후, 안내 펼침", showBackground = true)
@Composable
private fun TicketRegisterCodeStepFilledPreview() {
    PickeTheme {
        TicketRegisterCodeStep(
            code = "PICKE-30-DEMO",
            isCodeCheckEnabled = true,
            onCodeChange = {},
            onCodeCheckClick = {},
            initiallyExpandedGuides = true
        )
    }
}