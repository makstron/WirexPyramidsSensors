package com.klim.wirexpyramidssensors.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.klim.wirexpyramidssensors.presentation.SensorDashboardViewModel
import com.klim.wirexpyramidssensors.data.SensorManager

@Composable
fun SensorDashboardUI(viewModel: SensorDashboardViewModel) {
    val data = viewModel.measurements
        .collectAsState(initial = null)
        .value ?: return

    Column {
        Text("Sensors Dashboard")

        /**
         * Is there anything confusing for you with these buttons below?
         *
         * There is a problem with Movement button
         */

        /**
         * Is there anything confusing for you with these buttons below?
         *
         * There is a problem with Movement button
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
            onClick = { viewModel.toggleSensor(SensorManager.MOVEMENT_SENSOR) }
        ) {
            Text("MOVEMENT" + (SensorManager.MOVEMENT_SENSOR in data.measurements.keys))
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
