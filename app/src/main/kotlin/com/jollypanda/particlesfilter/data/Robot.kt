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
//        x =  50.0
        y = randomizer.nextDouble() * heightConsntraint
//        y = heightConsntraint / 2.0
        oldX = x
        oldY = y
    }

    fun move() {
        oldX = x
        oldY = y

        x = randomizer.nextDouble() * widthConstraint + (randomizer.nextDouble() * ERROR - ERROR / 2)
//        x += 30
        y = randomizer.nextDouble() * heightConsntraint + (randomizer.nextDouble() * ERROR - ERROR / 2)
//        y = oldY

        sensorDx = (x - oldX) //+ (randomizer.nextDouble() * ERROR - ERROR / 2)
//        sensorDx = (dx) + (randomizer.nextDouble() * ERROR - ERROR / 2)
        sensorDy = (y - oldY) //+ (randomizer.nextDouble() * ERROR - ERROR / 2)
//        sensorDy = (dy) + (randomizer.nextDouble() * ERROR - ERROR / 2)
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
