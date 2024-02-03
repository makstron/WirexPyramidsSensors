package com.klim.wirexpyramidssensors.data

import kotlinx.coroutines.flow.Flow
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

