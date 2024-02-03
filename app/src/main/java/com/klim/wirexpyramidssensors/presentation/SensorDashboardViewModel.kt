package com.klim.wirexpyramidssensors.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klim.wirexpyramidssensors.data.SensorManager
import com.klim.wirexpyramidssensors.presentation.entity.ChartData
import com.klim.wirexpyramidssensors.presentation.entity.SensorsData
import com.klim.wirexpyramidssensors.presentation.entity.SensorsDataChunk
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * Should remember user picked sensors even if the app was suspended by
 * the system and then restored.
 */
class SensorDashboardViewModel(
    private val sensorDataManager: SensorManager,
) : ViewModel() {

    data class ScreenState(
        val sensorsState: Map<String, Boolean>,
        /**
         * Combined measurements from multiple user picked sensors.
         */
        val measurements: SensorsData,
    ) {
        companion object {
            fun initial() =
                ScreenState(
                    sensorsState = emptyMap(),
                    measurements = SensorsData(emptyMap())
                )
        }
    }

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.initial())
    val screenState: Flow<ScreenState> = _screenState

    private val sensorsState = HashMap<String, Boolean>()
    private val sensorsJobs = HashMap<String, Job>()
    private var measurements = HashMap<String, ChartData>()

    /**
     * Implement function to start/stop listening for the measurements
     * from sensor with id = [id] within the [measurements] flow
     */
    fun toggleSensor(id: String) {
        if (getToggleState(id)) {
            switchToggleState(id)
            cancelSubscription(id)
        } else {
            sensorsJobs[id] = launchCollector(id)
        }
    }

    private fun getToggleState(id: String): Boolean {
        return sensorsState.getOrPut(id) {
            false
        }
    }

    private fun cancelSubscription(id: String) {
        sensorsJobs[id]?.cancel()
        sensorsJobs.remove(id)
    }

    private fun switchToggleState(id: String): Boolean {
        val newState = getToggleState(id).not()
        sensorsState[id] = newState
        _screenState.tryEmit(
            _screenState.value.copy(
                sensorsState = sensorsState.toMap()
            )
        )
        return newState
    }

    private fun launchCollector(id: String): Job {
        return viewModelScope.launch {
            try {
                val flow = sensorDataManager.getSensorData(id)
                switchToggleState(id)
                flow.collect { newValue ->
                    updateMeasurements(id, newValue)
                }
            } catch (e: Exception) {
                //TODO: show some error for user
            }
        }
    }

    private fun updateMeasurements(id: String, newValue: SensorsDataChunk) {
        val data = measurements.getOrPut(id) {
            ChartData(
                color = getColorForSensor(id),
                data = mutableListOf(newValue)
            )
        }
        val list = data.data.toMutableList().apply {
            add(newValue)
        }

        measurements[id] = data.copy(data = list)

        _screenState.tryEmit(
            _screenState.value.copy(
                measurements = SensorsData(measurements)
            )
        )
    }

    private fun getColorForSensor(id: String): Color {
        return when (id) {
            SensorManager.NOISE_SENSOR -> Color.Cyan.copy(alpha = 0.5f)
            SensorManager.LIGHT_SENSOR -> Color.Yellow.copy(alpha = 0.5f)
            SensorManager.MOVEMENT_SENSOR -> Color.Magenta.copy(alpha = 0.5f)
            else -> Color.Black
        }
    }

    override fun onCleared() {
        super.onCleared()
        sensorDataManager.dispose()
    }

}