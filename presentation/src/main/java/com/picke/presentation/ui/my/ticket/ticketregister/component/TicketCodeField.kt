package com.picke.presentation.ui.my.ticket.ticketregister.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.component.CustomTextField
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun TicketCodeField(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val label = stringResource(R.string.ticket_register_code_label)
    val requiredMark = stringResource(R.string.ticket_register_code_required)
    val requiredColor = PickeTheme.colors.textPrimary

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
    ) {
        Text(
            text = buildAnnotatedString {
                append(label)
                withStyle(SpanStyle(color = requiredColor)) { append(requiredMark) }
            },
            color = PickeTheme.colors.textDefault,
            style = PickeTheme.typography.bodyMdSemiBold
        )
        CustomTextField(
            value = code,
            onValueChange = onCodeChange,
            minHeight = ComponentNumberTokens.inputTextfieldHeight,
            textStyle = PickeTheme.typography.bodyLgMedium,
            placeholder = stringResource(R.string.ticket_register_code_placeholder),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Done
            )
        )
    }
}

@Preview(name = "입력 전", showBackground = true)
@Composable
private fun TicketCodeFieldPreview() {
    PickeTheme {
        TicketCodeField(
            code = "",
            onCodeChange = {}
        )
    }
}

@Preview(name = "입력 후", showBackground = true)
@Composable
private fun TicketCodeFieldFilledPreview() {
    PickeTheme {
        TicketCodeField(
            code = "PICKE-30-DEMO",
            onCodeChange = {}
        )
    }
}