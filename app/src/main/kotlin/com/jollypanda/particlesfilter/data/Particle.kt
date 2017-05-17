package com.jollypanda.particlesfilter.data

/**
 * @author Yamushev Igor
 * @since  14.05.17
 */
data class Particle(var x: Double,
                    var y: Double,
                    var weight: Double,
                    var angle: Float = 0f,
                    var distance: Double = 0.0) {

    fun move(robot: Robot) {
        x += robot.sensorDx
        y += robot.sensorDy
    }

}
