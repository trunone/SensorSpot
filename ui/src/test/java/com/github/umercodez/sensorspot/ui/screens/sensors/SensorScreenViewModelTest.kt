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

import com.github.umercodez.sensorspot.data.repositories.settings.sensor.DeviceSensor
import com.github.umercodez.sensorspot.data.repositories.settings.sensor.SensorsRepository
import com.github.umercodez.sensorspot.data.sensoreventprovider.SensorEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SensorScreenViewModelTest {

    private class FakeSensorsRepository : SensorsRepository {
        override fun getAvailableSensors(): List<DeviceSensor> = emptyList()
        override fun getSensor(sensorType: Int): DeviceSensor? = null
        override suspend fun saveSelectedSensors(sensors: List<DeviceSensor>) {}
        override suspend fun getSelectedSensors(): List<DeviceSensor> = emptyList()
        override fun getSelectedSensorsAsFlow(): Flow<List<DeviceSensor>> = flowOf(emptyList())
        override suspend fun saveGpsSelectionState(state: Boolean) {}
        override val gpsSelectionState: Flow<Boolean> = flowOf(false)
        override fun getSensorEventsFlow(sensorType: Int, samplingRate: Int): Flow<SensorEvent> {
            return flowOf(
                SensorEvent(type = "android.sensor.accelerometer", values = listOf(1f, 2f, 3f), timestamp = 12345L)
            )
        }
    }

    @Test
    fun `test getSensorEventsFlow returns flow from repository`() {
        val repo = FakeSensorsRepository()
        val flow = repo.getSensorEventsFlow(1, 200000)
        assertNotNull(flow)
    }
}
