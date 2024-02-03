package com.klim.wirexpyramidssensors.data

import kotlinx.coroutines.flow.Flow
import java.io.IOException
import kotlin.random.Random

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