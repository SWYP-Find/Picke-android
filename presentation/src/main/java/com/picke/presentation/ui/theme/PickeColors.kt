package com.picke.presentation.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.picke.presentation.ui.theme.tokens.BrandColorTokens
import com.picke.presentation.ui.theme.tokens.ComponentColorTokens
import com.picke.presentation.ui.theme.tokens.SemanticColorTokens

data class PickeColors(
    // Text
    val textPrimary: Color = SemanticColorTokens.textDefault,
    val textSecondary: Color = SemanticColorTokens.textSubtle,
    val textTertiary: Color = SemanticColorTokens.textSubtler,
    val textMuted: Color = SemanticColorTokens.textMuted,
    val textInverse: Color = SemanticColorTokens.textInverse,
    val textBrand: Color = SemanticColorTokens.textPrimary,
    val textBody: Color = SemanticColorTokens.textBody,
    val textHighlight: Color = SemanticColorTokens.textSecondary,
    val textError: Color = SemanticColorTokens.textError,

    // Border
    val borderDefault: Color = SemanticColorTokens.borderBeigeDefault,
    val borderSubtle: Color = SemanticColorTokens.borderBeigeSelected,
    val borderDisabled: Color = SemanticColorTokens.borderBeigeDisabled,
    val borderSelected: Color = SemanticColorTokens.borderSecondarySelected,
    val borderStrong: Color = SemanticColorTokens.borderPrimaryDefault,
    val borderFocus: Color = SemanticColorTokens.borderBeigeFocus,
    val borderError: Color = SemanticColorTokens.borderErrorDefault,
    val borderGray: Color = SemanticColorTokens.borderGrayDefault,
    val borderGraySubtle: Color = SemanticColorTokens.borderGraySubtle,

    // Surface
    val surfaceDefault: Color = SemanticColorTokens.surfaceBeigeDefault,
    val surfaceSubtle: Color = SemanticColorTokens.surfaceBeigeSubtle,
    val surfaceTertiary: Color = SemanticColorTokens.surfaceBeigeStrong,
    val surfaceSelected: Color = SemanticColorTokens.surfacePrimaryDefault,
    val surfaceDisabled: Color = SemanticColorTokens.actionPrimaryDisabled,
    val surfacePrimarySubtle: Color = SemanticColorTokens.surfacePrimarySubtle,

    // Icon
    val iconDefault: Color = SemanticColorTokens.iconGrayDefault,
    val iconSubtle: Color = SemanticColorTokens.iconGraySubtle,
    val iconInverse: Color = SemanticColorTokens.iconGrayInverse,
    val iconPrimary: Color = SemanticColorTokens.iconPrimaryDefault,

    // Background
    val backgroundDefault: Color = SemanticColorTokens.backgroundDefault,
    val backgroundSubtle: Color = SemanticColorTokens.backgroundSubtle,
    val backgroundTertiary: Color = SemanticColorTokens.backgroundSubtler,
    val backgroundBrand: Color = SemanticColorTokens.backgroundBeige,
    val backgroundInverse: Color = BrandColorTokens.gray800,

    // Status
    val statusError: Color = BrandColorTokens.errorDefault,
    val statusWarning: Color = BrandColorTokens.warningDefault,

    // Component — Button
    val buttonPrimaryBackground: Color = ComponentColorTokens.buttonPrimaryBackgroundDefault,
    val buttonPrimaryBackgroundPressed: Color = ComponentColorTokens.buttonPrimaryBackgroundPressed,
    val buttonPrimaryBackgroundDisabled: Color = ComponentColorTokens.buttonPrimaryBackgroundDisabled,
    val buttonPrimaryText: Color = ComponentColorTokens.buttonPrimaryTextDefault,
    val buttonSecondaryBackground: Color = ComponentColorTokens.buttonSecondaryBackgroundDefault,
    val buttonSecondaryBackgroundPressed: Color = ComponentColorTokens.buttonSecondaryBackgroundPressed,
    val buttonSecondaryText: Color = ComponentColorTokens.buttonSecondaryTextDefault,

    // Component — Input
    val inputBorderDefault: Color = ComponentColorTokens.inputTextfieldBorderDefault,
    val inputBorderActive: Color = ComponentColorTokens.inputTextfieldBorderFocus,
    val inputBorderError: Color = ComponentColorTokens.inputTextfieldBorderError,
    val inputSurface: Color = ComponentColorTokens.inputTextfieldBackgroundDefault,
    val inputSurfaceDisabled: Color = ComponentColorTokens.inputTextfieldBackgroundDisabled,
    val inputTextDefault: Color = ComponentColorTokens.inputTextfieldTextDefault,
    val inputTextActive: Color = ComponentColorTokens.inputTextfieldTextFocus,
    val inputTextError: Color = ComponentColorTokens.inputTextfieldTextError,

    // Component — Badge
    val badgeBackground: Color = ComponentColorTokens.badgeFilledBackground,
    val badgeBackgroundInverse: Color = ComponentColorTokens.badgePrimaryBackground,
    val badgeText: Color = ComponentColorTokens.badgeFilledText,
    val badgeTextInverse: Color = ComponentColorTokens.badgePrimaryText,
    val badgeOutlineText: Color = ComponentColorTokens.badgeOutlineText,
    val badgeOutlineBackground: Color = ComponentColorTokens.badgeOutlineBackground,
    val badgeOutlineBorder: Color = ComponentColorTokens.badgeOutlineBorder,

    // Brand palette — colors not covered by semantic tokens
    val primary: Color = BrandColorTokens.primary500,
    val primaryPressed: Color = BrandColorTokens.primary600,
    val primaryDark: Color = BrandColorTokens.primary800,
    val primaryDarkest: Color = BrandColorTokens.primary900,
    val primaryDisabled: Color = BrandColorTokens.primary300,
    val primaryLight: Color = BrandColorTokens.primary50,
    val primary100: Color = BrandColorTokens.primary100,
    val primary700: Color = BrandColorTokens.primary700,
    val secondary: Color = BrandColorTokens.secondary500,
    val secondaryLight: Color = BrandColorTokens.secondary200,
    val secondary50: Color = BrandColorTokens.secondary50,
    val secondary100: Color = BrandColorTokens.secondary100,
    val secondary300: Color = BrandColorTokens.secondary300,
    val secondary700: Color = BrandColorTokens.secondary700,
    val neutral50: Color = BrandColorTokens.gray50,
    val neutral100: Color = BrandColorTokens.gray100,
    val neutral200: Color = BrandColorTokens.gray200,
    val neutral400: Color = BrandColorTokens.gray400,
    val neutral600: Color = BrandColorTokens.gray600,
    val beige100: Color = BrandColorTokens.beige100,
    val beige600: Color = BrandColorTokens.beige600,
    val beige800: Color = BrandColorTokens.beige800,
    val beige900: Color = BrandColorTokens.beige900,

    // Backward-compat aliases for existing PickeTheme.colors usages
    val surface: Color = SemanticColorTokens.surfaceBeigeDefault,
    val outline: Color = SemanticColorTokens.textMuted,
)

val LocalPickeColors = staticCompositionLocalOf { PickeColors() }
