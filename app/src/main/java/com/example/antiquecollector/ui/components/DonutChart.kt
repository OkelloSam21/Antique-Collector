package com.example.antiquecollector.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.antiquecollector.R
import com.example.antiquecollector.ui.theme.atoms.*

@Composable
fun DonutChart(
    data: List<Pair<String, Float>>,
    modifier: Modifier = Modifier
) {
    val total = data.sumOf { it.second.toDouble() }.toFloat().coerceAtLeast(1f)
    val colors = listOf(
        primaryLight,
        primaryContainerLight,
        inversePrimaryLight,
    )
    
    Canvas(
        modifier = modifier
            .size(80.dp)
            .padding(4.dp)
    ) {
        var startAngle = -90f
        val strokeWidth = size.minDimension * 0.2f
        val innerRadius = (size.minDimension - strokeWidth) / 2
        
        data.forEachIndexed { index, (_, value) ->
            val sweepAngle = 360f * (value / total)
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                size = Size(innerRadius * 2, innerRadius * 2),
                style = Stroke(width = strokeWidth)
            )
            startAngle += sweepAngle
        }
    }
}

object CategoryIconMap {
    fun getIconRes(iconName: String?): Int {
        return when (iconName?.lowercase()) {
            "furniture" -> R.drawable.ic_furniture
            "art" -> R.drawable.ic_art
            "jewelry" -> R.drawable.ic_jewelry
//            "watches", "watch" -> R.drawable.ic_watch
            "books", "book" -> R.drawable.ic_book
//            "ceramics", "ceramic" -> R.drawable.ic_ceramics
            "glassware", "glass" -> R.drawable.ic_glassware
//            "textiles", "textile" -> R.drawable.ic_textiles
            "timepieces", "timepiece" -> R.drawable.ic_clock
//            "sculptures", "sculpture" -> R.drawable.ic_sculpture
//            "paintings", "painting" -> R.drawable.ic_painting
//            "accessories", "accessory" -> R.drawable.ic_accessories
            else -> R.drawable.ic_growth
        }
    }
}