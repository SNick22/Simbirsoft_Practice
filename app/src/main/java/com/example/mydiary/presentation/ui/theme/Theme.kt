package com.example.mydiary.presentation.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MyDiaryTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
//        typography = Typography,
        content = content
    )
}