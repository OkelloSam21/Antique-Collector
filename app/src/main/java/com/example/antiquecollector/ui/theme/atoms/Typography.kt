package com.example.antiquecollector.ui.theme.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily

@Composable
private fun getTypography(family: FontFamily): Typography {
    return Typography(
        displayLarge = MaterialTheme.typography.displayLarge.copy(fontFamily = family),
        displayMedium = MaterialTheme.typography.displayMedium.copy(fontFamily = family),
        displaySmall = MaterialTheme.typography.displaySmall.copy(fontFamily = family),
        titleLarge = MaterialTheme.typography.titleLarge.copy(fontFamily = family),
        titleMedium = MaterialTheme.typography.titleMedium.copy(fontFamily = family),
        titleSmall = MaterialTheme.typography.titleSmall.copy(fontFamily = family),
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = family),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(fontFamily = family),
        bodySmall = MaterialTheme.typography.bodySmall.copy(fontFamily = family),
        labelLarge = MaterialTheme.typography.labelLarge.copy(fontFamily = family),
        labelMedium = MaterialTheme.typography.labelMedium.copy(fontFamily = family),
        labelSmall = MaterialTheme.typography.labelSmall.copy(fontFamily = family),
    )
}

//app typography



//enum class SafTypography {
//    SANS,
//    MONO,
//    NOTO, ;
//
//    val label: String
//        get() =
//            when (this) {
//                SANS -> "Sans Serif"
//                MONO -> "Monospace"
//                NOTO -> "Noto"
//            }
//}


