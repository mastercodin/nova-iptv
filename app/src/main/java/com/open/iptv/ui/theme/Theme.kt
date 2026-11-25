package com.open.iptv.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BrandPrimary = Color(0xFF6C5CE7)
private val BrandSecondary = Color(0xFF00B8D4)
private val BrandTertiary = Color(0xFFFF8A65)

private val LightColors = lightColorScheme(
    primary = BrandPrimary,
    secondary = BrandSecondary,
    tertiary = BrandTertiary,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB9A7FF),
    onPrimary = Color(0xFF211A45),
    primaryContainer = Color(0xFF382D69),
    secondary = Color(0xFF5AD7E8),
    onSecondary = Color(0xFF00363D),
    tertiary = Color(0xFFFFB59B),
    background = Color(0xFF0A0B14),
    surface = Color(0xFF121424),
    surfaceVariant = Color(0xFF252841),
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(40.dp),
)

private val AppTypography = Typography(
    titleLarge = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OpenIptvTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme: ColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        motionScheme = MotionScheme.expressive(),
        shapes = AppShapes,
        typography = AppTypography,
        content = content,
    )
}
