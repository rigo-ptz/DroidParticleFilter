package com.jollypanda.particlesfilter.data

import java.util.*


/**
 * @author Yamushev Igor
 * @since  14.05.17
 */
class Robot(val widthConstraint: Int,
            val heightConsntraint: Int) {

    val ERROR = 25.0
    private val randomizer = Random()

    private var oldX = 0.0
    private var oldY = 0.0

    var x = 0.0
    var y = 0.0

    var sensorDx = 0.0
    var sensorDy = 0.0

    init {
        x = randomizer.nextDouble() * widthConstraint
        y = randomizer.nextDouble() * heightConsntraint
        oldX = x
        oldY = y
    }

    fun move() {
        oldX = x
        oldY = y

        x = randomizer.nextDouble() * widthConstraint + (randomizer.nextDouble() * ERROR - ERROR / 2)
        y = randomizer.nextDouble() * heightConsntraint + (randomizer.nextDouble() * ERROR - ERROR / 2)

        sensorDx = (x - oldX)
        sensorDy = (y - oldY)
    }

    fun getErroredX(): Double {
        val e = randomizer.nextDouble() * ERROR - ERROR / 2
        return x + e
    }

    fun getErroredY(): Double {
        val e = randomizer.nextDouble() * ERROR - ERROR / 2
        return y + e
    }

}
