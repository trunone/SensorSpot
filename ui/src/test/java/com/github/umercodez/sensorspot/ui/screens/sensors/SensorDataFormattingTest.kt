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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SensorDataFormattingTest {

    @Test
    fun `test formatSensorAxisLabels for 3-axis sensor`() {
        val values = listOf(0.123f, -9.814f, 0.056f)
        val labels = formatSensorAxisLabels(values)
        assertEquals(3, labels.size)
        assertEquals("X: 0.12", labels[0])
        assertEquals("Y: -9.81", labels[1])
        assertEquals("Z: 0.06", labels[2])
    }

    @Test
    fun `test formatSensorAxisLabels for 1-axis sensor`() {
        val values = listOf(1013.25f)
        val labels = formatSensorAxisLabels(values)
        assertEquals(1, labels.size)
        assertEquals("Value: 1013.25", labels[0])
    }

    @Test
    fun `test formatSensorAxisLabels for empty values`() {
        val values = emptyList<Float>()
        val labels = formatSensorAxisLabels(values)
        assertTrue(labels.isEmpty())
    }

    @Test
    fun `test formatSensorAxisLabels for 4-axis sensor`() {
        val values = listOf(0.1f, 0.2f, 0.3f, 0.9f)
        val labels = formatSensorAxisLabels(values)
        assertEquals(4, labels.size)
        assertEquals("X: 0.10", labels[0])
        assertEquals("Y: 0.20", labels[1])
        assertEquals("Z: 0.30", labels[2])
        assertEquals("W: 0.90", labels[3])
    }
}
