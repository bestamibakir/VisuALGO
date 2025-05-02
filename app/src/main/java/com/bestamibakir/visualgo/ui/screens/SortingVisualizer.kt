package com.bestamibakir.visualgo.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SortingVisualizer(
    numbers: List<Int>,
    comparingIndices: Pair<Int, Int>? = null,
    swappingIndices: Pair<Int, Int>? = null,
    currentOperation: String = "",
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (currentOperation.isNotEmpty()) {
            Text(
                text = currentOperation,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(Color(0xFFF5F5F5))
        ) {
            if (numbers.isEmpty()) return@Canvas

            val maxValue = numbers.maxOrNull() ?: 0
            val width = size.width / numbers.size

            numbers.forEachIndexed { index, value ->
                val height = (value / maxValue.toFloat()) * size.height * 0.9f

                val color = when {
                    swappingIndices != null && (index == swappingIndices.first || index == swappingIndices.second) ->
                        Color(0xFFFF5252)
                    comparingIndices != null && (index == comparingIndices.first || index == comparingIndices.second) ->
                        Color(0xFFFFD740)
                    else -> Color(0xFF2196F3)
                }

                drawRect(
                    color = color,
                    topLeft = Offset(index * width, size.height - height),
                    size = Size(width * 0.85f, height)
                )

                drawContext.canvas.nativeCanvas.apply {
                    val textPaint = android.graphics.Paint().apply {
                        this.color = android.graphics.Color.BLACK
                        this.textSize = 30f
                        this.textAlign = android.graphics.Paint.Align.CENTER
                    }
                    drawText(
                        value.toString(),
                        index * width + width / 2,
                        size.height - height - 10,
                        textPaint
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            ColorLegendItem(color = Color(0xFF2196F3), text = "Normal")
            ColorLegendItem(color = Color(0xFFFFD740), text = "Karşılaştırılan")
            ColorLegendItem(color = Color(0xFFFF5252), text = "Yer Değiştirilen")
        }
    }
}

@Composable
fun ColorLegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color)
        )
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}