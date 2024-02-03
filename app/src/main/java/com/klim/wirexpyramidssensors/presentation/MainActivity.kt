package com.klim.wirexpyramidssensors.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.klim.wirexpyramidssensors.data.SensorManager
import com.klim.wirexpyramidssensors.presentation.ui.SensorDashboardUI
import com.klim.wirexpyramidssensors.presentation.ui.theme.WirexPyramidsSensorsTheme

class MainActivity : ComponentActivity() {

    private val viewModel: SensorDashboardViewModel by viewModels {
        ViewModelFactory(SensorManager())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WirexPyramidsSensorsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SensorDashboardUI(viewModel)
                }
            }
        }
    }
}

open class ViewModelFactory
constructor(
    private val sensorDataManager: SensorManager,
) : AbstractSavedStateViewModelFactory() {

    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        return SensorDashboardViewModel(
            handle,
            sensorDataManager,
        ) as T
    }

}