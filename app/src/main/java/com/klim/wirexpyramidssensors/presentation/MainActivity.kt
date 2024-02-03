package com.klim.wirexpyramidssensors.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.klim.wirexpyramidssensors.data.SensorManager
import com.klim.wirexpyramidssensors.presentation.ui.SensorDashboardUI
import com.klim.wirexpyramidssensors.presentation.ui.theme.WirexPyramidsSensorsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //to keep it simple I just left it here
            val sensorDataManager = SensorManager()
            val viewModel = SensorDashboardViewModel(sensorDataManager)

            WirexPyramidsSensorsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SensorDashboardUI(viewModel)
                }
            }
        }
    }
}