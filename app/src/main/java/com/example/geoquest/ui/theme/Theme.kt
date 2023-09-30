package com.example.geoquest.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorPalette = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorPalette = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun GeoQuestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Testing dark mode theming


//private val LightThemeColors = lightColors(
//    primary = Blue600,
//    primaryVariant = Blue400,
//    onPrimary = Black2,
//    secondary = Color.White,
//    secondaryVariant = Teal300,
//    onSecondary = Color.Black,
//    error = RedErrorDark,
//    onError = RedErrorLight,
//    background = Grey1,
//    onBackground = Color.Black,
//    surface = Color.White,
//    onSurface = Black2,
//)

//private val DarkThemeColors = darkColors(
//    primary = Blue700,
//    primaryVariant = Color.White,
//    onPrimary = Color.White,
//    secondary = Black1,
//    onSecondary = Color.White,
//    error = RedErrorLight,
//    background = Color.Black,
//    onBackground = Color.White,
//    surface = Black1,
//    onSurface = Color.White,
//)


//@Composable
//fun AppTheme(
//    darkTheme: Boolean,
//    content: @Composable () -> Unit,
//) {
//    MaterialTheme(
//        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
//    ){
//        content()
//    }
//}