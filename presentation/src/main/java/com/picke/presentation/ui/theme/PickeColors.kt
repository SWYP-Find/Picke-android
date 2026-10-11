package com.picke.presentation.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.picke.presentation.ui.theme.tokens.BrandColorTokens
import com.picke.presentation.ui.theme.tokens.ComponentColorTokens
import com.picke.presentation.ui.theme.tokens.SemanticColorTokens

data class PickeColors(
    // Text
    val textDefault: Color = SemanticColorTokens.textDefault,
    val textSubtle: Color = SemanticColorTokens.textSubtle,
    val textSubtler: Color = SemanticColorTokens.textSubtler,
    val textBody: Color = SemanticColorTokens.textBody,
    val textMuted: Color = SemanticColorTokens.textMuted,
    val textInverse: Color = SemanticColorTokens.textInverse,
    val textPrimary: Color = SemanticColorTokens.textPrimary,
    val textSecondary: Color = SemanticColorTokens.textSecondary,
    val textError: Color = SemanticColorTokens.textError,

    // Border
    val borderBeigeDefault: Color = SemanticColorTokens.borderBeigeDefault,
    val borderBeigeSelected: Color = SemanticColorTokens.borderBeigeSelected,
    val borderBeigeDisabled: Color = SemanticColorTokens.borderBeigeDisabled,
    val borderBeigeFocus: Color = SemanticColorTokens.borderBeigeFocus,
    val borderSecondarySelected: Color = SemanticColorTokens.borderSecondarySelected,
    val borderPrimaryDefault: Color = SemanticColorTokens.borderPrimaryDefault,
    val borderErrorDefault: Color = SemanticColorTokens.borderErrorDefault,
    val borderGrayDefault: Color = SemanticColorTokens.borderGrayDefault,
    val borderGraySubtle: Color = SemanticColorTokens.borderGraySubtle,

    // Surface
    val surfaceBeigeDefault: Color = SemanticColorTokens.surfaceBeigeDefault,
    val surfaceBeigeSubtle: Color = SemanticColorTokens.surfaceBeigeSubtle,
    val surfaceBeigeStrong: Color = SemanticColorTokens.surfaceBeigeStrong,
    val surfacePrimaryDefault: Color = SemanticColorTokens.surfacePrimaryDefault,
    val surfacePrimarySubtle: Color = SemanticColorTokens.surfacePrimarySubtle,

    // Background
    val backgroundDefault: Color = SemanticColorTokens.backgroundDefault,
    val backgroundSubtle: Color = SemanticColorTokens.backgroundSubtle,
    val backgroundSubtler: Color = SemanticColorTokens.backgroundSubtler,
    val backgroundBeige: Color = SemanticColorTokens.backgroundBeige,

    // Icon
    val iconGrayDefault: Color = SemanticColorTokens.iconGrayDefault,
    val iconGraySubtle: Color = SemanticColorTokens.iconGraySubtle,
    val iconGrayInverse: Color = SemanticColorTokens.iconGrayInverse,
    val iconPrimaryDefault: Color = SemanticColorTokens.iconPrimaryDefault,

    // Action
    val actionPrimaryDisabled: Color = SemanticColorTokens.actionPrimaryDisabled,

    // Component — Button
    val buttonPrimaryBackgroundDefault: Color = ComponentColorTokens.buttonPrimaryBackgroundDefault,
    val buttonPrimaryBackgroundPressed: Color = ComponentColorTokens.buttonPrimaryBackgroundPressed,
    val buttonPrimaryBackgroundDisabled: Color = ComponentColorTokens.buttonPrimaryBackgroundDisabled,
    val buttonPrimaryTextDefault: Color = ComponentColorTokens.buttonPrimaryTextDefault,
    val buttonSecondaryBackgroundDefault: Color = ComponentColorTokens.buttonSecondaryBackgroundDefault,
    val buttonSecondaryBackgroundPressed: Color = ComponentColorTokens.buttonSecondaryBackgroundPressed,
    val buttonSecondaryTextDefault: Color = ComponentColorTokens.buttonSecondaryTextDefault,

    // Component — Input
    val inputTextfieldBackgroundDefault: Color = ComponentColorTokens.inputTextfieldBackgroundDefault,
    val inputTextfieldBackgroundDisabled: Color = ComponentColorTokens.inputTextfieldBackgroundDisabled,
    val inputTextfieldBorderDefault: Color = ComponentColorTokens.inputTextfieldBorderDefault,
    val inputTextfieldBorderFocus: Color = ComponentColorTokens.inputTextfieldBorderFocus,
    val inputTextfieldBorderError: Color = ComponentColorTokens.inputTextfieldBorderError,
    val inputTextfieldTextDefault: Color = ComponentColorTokens.inputTextfieldTextDefault,
    val inputTextfieldTextFocus: Color = ComponentColorTokens.inputTextfieldTextFocus,
    val inputTextfieldTextError: Color = ComponentColorTokens.inputTextfieldTextError,

    // Component — Badge
    val badgeFilledBackground: Color = ComponentColorTokens.badgeFilledBackground,
    val badgeFilledText: Color = ComponentColorTokens.badgeFilledText,
    val badgePrimaryBackground: Color = ComponentColorTokens.badgePrimaryBackground,
    val badgePrimaryText: Color = ComponentColorTokens.badgePrimaryText,
    val badgeOutlineBackground: Color = ComponentColorTokens.badgeOutlineBackground,
    val badgeOutlineBorder: Color = ComponentColorTokens.badgeOutlineBorder,
    val badgeOutlineText: Color = ComponentColorTokens.badgeOutlineText,
    val cardGrayBackgroundDefault: Color = ComponentColorTokens.cardGrayBackgroundDefault,
    val cardBaseBackgroundDefault: Color = ComponentColorTokens.cardBaseBackgroundDefault,
    val cardBaseBorderDefault: Color = ComponentColorTokens.cardBaseBorderDefault,
    val cardBaseTextTitle: Color = ComponentColorTokens.cardBaseTextTitle,
    val cardBaseTextDecription: Color = ComponentColorTokens.cardBaseTextDecription,
    val cardOpinionBackgroundDefault: Color = ComponentColorTokens.cardOpinionBackgroundDefault,
    val checkboxBackgroundDefault: Color = ComponentColorTokens.checkboxBackgroundDefault,
    val checkboxBackgroundSelected: Color = ComponentColorTokens.checkboxBackgroundSelected,
    val checkboxBorderDefault: Color = ComponentColorTokens.checkboxBorderDefault,
    val checkboxBorderSelected: Color = ComponentColorTokens.checkboxBorderSelected,
    val toggleTrackOn: Color = ComponentColorTokens.toggleTrackOn,
    val toggleTrackOff: Color = ComponentColorTokens.toggleTrackOff,
    val toggleThumbDefault: Color = ComponentColorTokens.toggleThumbDefault,

    // Brand palette — colors not covered by semantic tokens
    val primary50: Color = BrandColorTokens.primary50,
    val primary100: Color = BrandColorTokens.primary100,
    val primary300: Color = BrandColorTokens.primary300,
    val primary500: Color = BrandColorTokens.primary500,
    val primary600: Color = BrandColorTokens.primary600,
    val primary700: Color = BrandColorTokens.primary700,
    val primary800: Color = BrandColorTokens.primary800,
    val primary900: Color = BrandColorTokens.primary900,
    val secondary50: Color = BrandColorTokens.secondary50,
    val secondary100: Color = BrandColorTokens.secondary100,
    val secondary200: Color = BrandColorTokens.secondary200,
    val secondary300: Color = BrandColorTokens.secondary300,
    val secondary500: Color = BrandColorTokens.secondary500,
    val secondary700: Color = BrandColorTokens.secondary700,
    val gray50: Color = BrandColorTokens.gray50,
    val gray100: Color = BrandColorTokens.gray100,
    val gray200: Color = BrandColorTokens.gray200,
    val gray400: Color = BrandColorTokens.gray400,
    val gray600: Color = BrandColorTokens.gray600,
    val gray800: Color = BrandColorTokens.gray800,
    val gray900: Color = BrandColorTokens.gray900,
    val beige100: Color = BrandColorTokens.beige100,
    val beige500: Color = BrandColorTokens.beige500,
    val beige600: Color = BrandColorTokens.beige600,
    val beige700: Color = BrandColorTokens.beige700,
    val beige800: Color = BrandColorTokens.beige800,
    val beige900: Color = BrandColorTokens.beige900,
    val errorDefault: Color = BrandColorTokens.errorDefault,
    val warningDefault: Color = BrandColorTokens.warningDefault,
)

val LocalPickeColors = staticCompositionLocalOf { PickeColors() }
