package com.klim.wirexpyramidssensors.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import kotlin.random.Random

/**
 * Sensors Dashboard - is the application to visualize measurements
 * from the remote sensors placed in the ancient Egyptian pyramid.
 * The operator (user) can enable and disable sensors within
 * the application. The data from enabled sensors is visualized on the
 * chart. Each sensor is represented by its own line on the chart.
 *
 * There are 3 sensors available: noise, light, and movement. Each of them
 * performs measurement and returns its value by HTTP request.
 * Each sensor returns data within 0f..1f range.
 *
 * To know the URL address of the sensor, the app should access the configuration
 * server. The server keeps the URL of each sensor and returns it by HTTP call.
 *
 * One of your developers started writing the code of the application,
 * but then suddenly disappeared. Right before that, our cameras recorded
 * strange moving object that looked like walking dead or mummy. Anyway,
 * don't bother with that. I'm sure it's some dumb jokes from our colleagues.
 *
 * Your goal is to:
 * 1. Understand the requirements
 * 2. Inspect the existing code
 * 3. Fill the gaps in it
 * 4. Fix the present issues
 *
 * You're free to use Google if you need to. You can use your IDE to write
 * the code. You will need dependencies for Compose, Kotlin Coroutines/Flow,
 * ViewModel. You can just create new "Blank Activity" project within
 * Android Studio, it will add all required dependencies automatically.
 * There are some unclear things (for example, error handling) that
 * you should solve yourself.
 *
 * Please don't change the functions with *DON'T MODIFY THIS FUNCTION* comment
 *
 * Good luck!
 */

class Sensor(val url: String) {

    /**
     * DON'T MODIFY THIS FUNCTION
     *
     * Make blocking IO operation and return a single measurement.
     * May occasionally crash due to network or hardware
     * temporary issues.
     */
    private fun readNetworkSensor(): Float {
        //Long blocking IO operation that read data from url
        Thread.sleep(Random.nextLong(100))
        //that may randomly end with en error
        if (Random.nextBoolean()) {
            throw IOException("sensor temporarily unavailable")
        }
        return Random.nextFloat()
    }

    /**
     * Implement flow of measurements. Function
     * should constantly read data from [readNetworkSensor]
     * and emit it.
     */
    fun dataFlow(): Flow<Float> =

}

class SensorManager {
    private val sensors = mutableMapOf<String, Sensor>()

    /**
     * One sensor can be read by multiple consumers.
     * Optimise for it.
     */
    fun getSensorData(
        sensorId: String,
    ): Flow<Float> = sensors
        .getOrPut(sensorId) {
            readConfigurationFromNetworkAndSetupSensor(sensorId)
        }
        .dataFlow()

    /**
     * DON'T MODIFY THIS FUNCTION
     *
     * Blocking IO operation that reads remote configuration
     * and setups the Sensor.
     */
    private fun readConfigurationFromNetworkAndSetupSensor(
        sensorId: String,
    ): Sensor {
        //Long blocking IO operation that reads data from the remote config server
        Thread.sleep(Random.nextLong(100))
        //that may randomly end with en error'
        if (Random.nextBoolean()) {
            throw IOException("configuration server is temporarily unavailable")
        }
        return Sensor("url of the sensor from the remote configuration")
    }

    companion object {
        const val NOISE_SENSOR = "noise"
        const val LIGHT_SENSOR = "light"
        const val MOVEMENT_SENSOR = "movement"
    }
}

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
    val measurements: Flow<SensorsData> =

    /**
     * Implement function to start/stop listening for the measurements
     * from sensor with id = [id] within the [measurements] flow
     */
    fun toggleSensor(id: String)
}

data class SensorsData(
    val measurements: Map<String, List<Float>>
)

@Composable
fun SensorDashboardUI(viewModel: SensorDashboardViewModel) {
    val data = viewModel.measurements
        .collectAsState(initial = null)
        .value ?: return

    Column {
        Text("Sensors Dashboard")

        /**
         * Is there anything confusing for you with these buttons below?
         */
        Button(
            onClick = { viewModel.toggleSensor(SensorManager.NOISE_SENSOR) }
        ) {
            Text("NOISE: " + (SensorManager.NOISE_SENSOR in data.measurements.keys))
        }

        Button(
            onClick = { viewModel.toggleSensor(SensorManager.LIGHT_SENSOR) }
        ) {
            Text("LIGHT" + (SensorManager.LIGHT_SENSOR in data.measurements.keys))
        }

        Button(
            onClick = { viewModel.toggleSensor(SensorManager.LIGHT_SENSOR) }
        ) {
            Text("MOVEMENT" + (SensorManager.LIGHT_SENSOR in data.measurements.keys))
        }

        BuildChart(data.measurements)
    }
}

/**
 * Do we have enough data to build line charts? The chart
 * should contain 0..N lines. Each line should represent
 * the series of measurements from one particular sensor.
 * Axis X - measurement timestamp.
 * Axis Y - measurement value.
 *
 * DO NOT implement this function
 */
@Composable
fun BuildChart(measurements: Map<String, List<Float>>) {
    // some chart drawing code
}
