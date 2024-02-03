package com.klim.wirexpyramidssensors.data

import com.klim.wirexpyramidssensors.presentation.entity.SensorsDataChunk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception
import kotlin.random.Random

class SensorManager(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val job: Job = SupervisorJob(),
    private val coroutineScope: CoroutineScope = GlobalScope + job + dispatcher,
    private val connectionRetryCount: Int = 3,
) {

    private val sensors = mutableMapOf<String, SensorContainer>()
    private val mutexSensorCreation = Mutex()

    private class SensorContainer(
        val sensor: Sensor,
        val dataFlow: MutableSharedFlow<SensorsDataChunk>,
    )

    /**
     * One sensor can be read by multiple consumers.
     * Optimise for it.
     *
     * Idea behind
     * Use a [mutexSensorCreation] to prevent possible problems with concurrent create operations
     *
     * Use a MutableSharedFlow to have the possibility to share the same data between
     * many subscribers without the need to make the same requests for the sensor again.
     * [subscribeSensorSubscriptionsCount] it will follow for the subscribers count and in
     * case the count is 0, it stops the internal flow otherwise runs the internal flow again [manageSensorFlow]
     */
    suspend fun getSensorData(
        sensorId: String,
        extraBufferCapacity: Int = 10,
        onBufferOverflow: BufferOverflow = BufferOverflow.DROP_OLDEST
    ): Flow<SensorsDataChunk> = withContext<Flow<SensorsDataChunk>>(dispatcher) {
        getSensorContainer(sensorId, extraBufferCapacity, onBufferOverflow).dataFlow
    }

    private suspend fun getSensorContainer(
        sensorId: String,
        extraBufferCapacity: Int = 10,
        onBufferOverflow: BufferOverflow = BufferOverflow.DROP_OLDEST
    ): SensorContainer {
        return mutexSensorCreation.withLock {
            sensors.getOrPut(sensorId) {
                SensorContainer(
                    sensor = connectToSensor(sensorId),
                    dataFlow = MutableSharedFlow(
                        extraBufferCapacity = extraBufferCapacity,
                        onBufferOverflow = onBufferOverflow,
                    ),
                ).apply {
                    subscribeSensorSubscriptionsCount(this)
                }
            }
        }
    }

    private fun subscribeSensorSubscriptionsCount(sensorWrapper: SensorContainer) {
        coroutineScope.launch {
            sensorWrapper.dataFlow.subscriptionCount
                .map { count -> count > 0 }
                .distinctUntilChanged()
                .onEach { isActive ->
                    sensorWrapper.sensor.isActive = isActive
                    manageSensorFlow(sensorWrapper)
                }
                .launchIn(this)
        }
    }

    private fun manageSensorFlow(sensorWrapper: SensorContainer) {
        if (sensorWrapper.sensor.isActive) {
            coroutineScope.launch {
                sensorWrapper.sensor.dataFlow()
                    .collect {
                        sensorWrapper.dataFlow.emit(it)
                    }
            }
        }
    }

    private fun connectToSensor(sensorId: String): Sensor {
        var connectionRetries = connectionRetryCount
        var sensor: Sensor? = null
        while (sensor == null) {
            try {
                sensor = readConfigurationFromNetworkAndSetupSensor(sensorId)
            } catch (e: Exception) {
                if (connectionRetries == 0)
                    throw e
                //TODO: log error if needed
            }
            connectionRetries--
        }
        return sensor
    }

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

    fun dispose() {
        job.cancel()
        sensors.clear()
    }

    companion object {
        const val NOISE_SENSOR = "noise"
        const val LIGHT_SENSOR = "light"
        const val MOVEMENT_SENSOR = "movement"
    }
}