package com.example.antiquecollector.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ConditionRatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    maxRating: Int = 5,
    onRatingChanged: (Int) -> Unit,
    starSize: Dp = 24.dp,
    starSpacing: Dp = 4.dp,
) {
    Row(modifier = modifier) {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.StarOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(starSize)
                    .clickable { onRatingChanged(i) }
            )
            
            if (i < maxRating) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}