package com.playsnyc.realistix.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.playsnyc.realistix.utils.MyFonts

private val DarkColorScheme = darkColorScheme(
    primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80,

)

private val LightColorScheme = lightColorScheme(
    primary = Purple40, secondary = PurpleGrey40, tertiary = Pink40,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)
@Suppress("DEPRECATION")
private val DefaultPlatformTextStyle = PlatformTextStyle(
    includeFontPadding = false
)
internal val DefaultTextStyle = TextStyle.Default.copy(platformStyle = DefaultPlatformTextStyle)

@Composable
fun RealisTixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
)
{

    val Typography = Typography(
            bodyLarge = TextStyle(
                    fontFamily = MyFonts.poppins(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.5.sp
            ),
            titleMedium = Typography().titleMedium.copy(fontFamily = MyFonts.poppins()),
            titleLarge = Typography().titleLarge.copy(fontSize = 19.sp,fontFamily = MyFonts.poppins()),
            titleSmall = Typography().titleSmall.copy(fontFamily = MyFonts.poppins()),
            headlineMedium = Typography().headlineMedium.copy(fontFamily = MyFonts.poppins()),
            headlineSmall = Typography().headlineSmall.copy(fontFamily = MyFonts.poppins()),
            headlineLarge = Typography().headlineLarge.copy(fontFamily = MyFonts.poppins()), //    labelSmall = TextStyle(
            //        fontFamily = FontFamily.Default,
            //        fontWeight = FontWeight.Medium,
            //        fontSize = 11.sp,
            //        lineHeight = 16.sp,
            //        letterSpacing = 0.5.sp
            //    )
            //
    )
//
//    val colorScheme = when
//    {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
//        {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }

    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode)
    {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    val customColorsPalette =
        if (darkTheme) OnDarkMyAppColors
        else OnLightMyAppColors

    val ripple = rememberRipple(bounded = false)
    CompositionLocalProvider(
        LocalTextStyle provides DefaultTextStyle,
        LocalIndication provides ripple,
        MyColors provides customColorsPalette
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}