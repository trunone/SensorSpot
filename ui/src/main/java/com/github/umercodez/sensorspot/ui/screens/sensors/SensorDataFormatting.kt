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
package com.github.umercodez.sensorspot.ui.screens.sensors

import java.util.Locale

fun formatSensorAxisLabels(values: List<Float>): List<String> {
    if (values.isEmpty()) return emptyList()
    val axisNames = when (values.size) {
        1 -> listOf("Value")
        2 -> listOf("X", "Y")
        3 -> listOf("X", "Y", "Z")
        4 -> listOf("X", "Y", "Z", "W")
        else -> values.indices.map { "Val $it" }
    }
    return values.mapIndexed { index, value ->
        val label = axisNames.getOrElse(index) { "Val $it" }
        String.format(Locale.US, "%s: %.2f", label, value)
    }
}
