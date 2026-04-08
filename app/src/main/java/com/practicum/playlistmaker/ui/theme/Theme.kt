package com.practicum.playlistmaker.ui.theme

import TypographyProject
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun ThemeProject(
    isDark: Boolean = isSystemInDarkTheme(),
    composable: @Composable () -> Unit
){
    MaterialTheme(
        colorScheme = if (isDark) LightColorScheme else DarkColorScheme,
        content = composable,
        typography = TypographyProject
    )
}

private val DarkColorScheme = darkColorScheme(
    primary = SwitchThumbActiveColor,
    onPrimary = White,
    secondary = Grey,
    onSecondary = LightGray, //фон поисковой строки
    background = White, //фон подложки приложения
    onBackground = YPBlack, //цвет текста
    surface = Grey, //цвет иконки
    onSurface = SwitchThumbInactiveColor
)

private val LightColorScheme = lightColorScheme(
    primary = SwitchThumbActiveColor,
    onPrimary = White,
    secondary = YPBlack,
    onSecondary = White, //фон поисковой строки
    background = YPBlack, //фон подложки приложения
    onBackground = White, //цвет текста
    surface = White, //цвет иконки
    onSurface = SwitchThumbInactiveColor

)