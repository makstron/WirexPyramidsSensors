package com.klim.wirexpyramidssensors.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.klim.wirexpyramidssensors.data.SensorManager
import com.klim.wirexpyramidssensors.presentation.SensorDashboardViewModel
import com.klim.wirexpyramidssensors.presentation.entity.SensorsData


@Composable
fun SensorDashboardUI(viewModel: SensorDashboardViewModel) {
    val screenState by viewModel.screenState.collectAsState(SensorDashboardViewModel.ScreenState.initial())

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Sensors Dashboard")

        /**
         * Is there anything confusing for you with these buttons below?
         *
         * There is a problem with Movement button
         */

        Button(
            onClick = { viewModel.toggleSensor(SensorManager.NOISE_SENSOR) }
        ) {
            Text("NOISE: " + screenState.sensorsState.getOrDefault(SensorManager.NOISE_SENSOR, false))
        }

        Button(
            onClick = { viewModel.toggleSensor(SensorManager.LIGHT_SENSOR) }
        ) {
            Text("LIGHT: " + screenState.sensorsState.getOrDefault(SensorManager.LIGHT_SENSOR, false))
        }

        Button(
            onClick = { viewModel.toggleSensor(SensorManager.MOVEMENT_SENSOR) }
        ) {
            Text("MOVEMENT: " + screenState.sensorsState.getOrDefault(SensorManager.MOVEMENT_SENSOR, false))
        }

        BuildChart(
            data = screenState.measurements,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondary,
        )
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
