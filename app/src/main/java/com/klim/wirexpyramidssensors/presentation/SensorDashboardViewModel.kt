package com.klim.wirexpyramidssensors.presentation

import androidx.lifecycle.ViewModel
import com.klim.wirexpyramidssensors.data.SensorManager
import com.klim.wirexpyramidssensors.presentation.entity.SensorsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Should remember user picked sensors even if the app was suspended by
 * the system and then restored.
 */
class SensorDashboardViewModel(
    private val sensorDataManager: SensorManager,
) : ViewModel() {

    /**
     * Implement flow of combined measurements from
     * multiple user picked sensors.
     */
    //TODO: implement it
    val measurements: Flow<SensorsData> = flowOf(SensorsData(emptyMap()))

    /**
     * Implement function to start/stop listening for the measurements
     * from sensor with id = [id] within the [measurements] flow
     */
    fun toggleSensor(id: String) {
        //TODO: implement it
    }
}