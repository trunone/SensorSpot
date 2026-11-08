/*
 *     This file is a part of SensorSpot (https://www.github.com/UmerCodez/SensorSpot)
 *     Copyright (C) 2025 Umer Farooq (umerfarooq2383@gmail.com)
 *
 *     SensorSpot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SensorSpot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SensorSpot. If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.github.umercodez.sensorspot.ui.screens.sensors.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.umercodez.sensorspot.ui.screens.sensors.formatSensorAxisLabels
import java.util.Locale

// Distinct standard colors for multidimensional sensor curves (e.g. X, Y, Z, W...)
val SensorChartColors = listOf(
    Color(0xFFE53935), // Red - X
    Color(0xFF43A047), // Green - Y
    Color(0xFF1E88E5), // Blue - Z
    Color(0xFFFB8C00), // Orange - W / 4th
    Color(0xFF8E24AA), // Purple - 5th
    Color(0xFF00ACC1), // Cyan - 6th
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SensorRawDataView(
    values: List<Float>,
    modifier: Modifier = Modifier
) {
    val labels = remember(values) { formatSensorAxisLabels(values) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Raw Data",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        if (values.isEmpty()) {
            Text(
                text = "Waiting for data...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                labels.forEachIndexed { index, label ->
                    val color = SensorChartColors.getOrElse(index) { Color.Gray }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SensorLiveChart(
    history: List<List<Float>>,
    modifier: Modifier = Modifier,
    maxDataPoints: Int = 50
) {
    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
    val textStyle = MaterialTheme.typography.labelSmall.copy(
        fontSize = 9.sp,
        fontFamily = FontFamily.Monospace
    )
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant

    val numSeries = history.firstOrNull()?.size ?: 0

    // Compute min and max across all series in history for auto-scaling
    val (minVal, maxVal) = if (history.isEmpty() || numSeries == 0) {
        -1f to 1f
    } else {
        var min = Float.MAX_VALUE
        var max = -Float.MAX_VALUE
        for (point in history) {
            for (v in point) {
                if (v < min) min = v
                if (v > max) max = v
            }
        }
        if (min == max) {
            min - 1f to max + 1f
        } else {
            val padding = (max - min) * 0.1f
            (min - padding) to (max + padding)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Live Chart",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = String.format(Locale.US, "Max: %.2f | Min: %.2f", maxVal, minVal),
                style = textStyle,
                color = textColor
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(top = 4.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                val width = size.width
                val height = size.height

                // Draw background grid lines (horizontal: top, middle, bottom)
                val midY = height / 2f
                drawLine(
                    color = gridColor,
                    start = Offset(0f, 0f),
                    end = Offset(width, 0f),
                    strokeWidth = 1f
                )
                drawLine(
                    color = gridColor,
                    start = Offset(0f, midY),
                    end = Offset(width, midY),
                    strokeWidth = 1f
                )
                drawLine(
                    color = gridColor,
                    start = Offset(0f, height),
                    end = Offset(width, height),
                    strokeWidth = 1f
                )

                if (history.size < 2 || numSeries == 0) return@Canvas

                val range = if (maxVal - minVal == 0f) 1f else (maxVal - minVal)
                val stepX = width / (maxDataPoints - 1).coerceAtLeast(1)
                val startX = width - (history.size - 1) * stepX

                // Draw path for each series/axis
                for (seriesIndex in 0 until numSeries) {
                    val strokeColor = SensorChartColors.getOrElse(seriesIndex) { Color.Gray }
                    val path = Path()

                    history.forEachIndexed { pointIndex, valuesList ->
                        if (seriesIndex < valuesList.size) {
                            val v = valuesList[seriesIndex]
                            val x = startX + (pointIndex * stepX)
                            // Map value: maxVal -> 0, minVal -> height
                            val y = height - ((v - minVal) / range) * height

                            if (pointIndex == 0) {
                                path.moveTo(x, y)
                            } else {
                                path.lineTo(x, y)
                            }
                        }
                    }

                    drawPath(
                        path = path,
                        color = strokeColor,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }
        }
    }
}
