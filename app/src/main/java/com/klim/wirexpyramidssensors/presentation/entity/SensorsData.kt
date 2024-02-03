package com.klim.wirexpyramidssensors.presentation.entity

import androidx.compose.ui.graphics.Color

data class SensorsData(
     val measurements: Map<String, ChartData>
)

data class ChartData(
    val color: Color,
    val data: List<SensorsDataChunk>
)


data class SensorsDataChunk(
    val time: Long,
    val value: Float,
)