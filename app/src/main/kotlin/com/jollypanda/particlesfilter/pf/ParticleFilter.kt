package com.jollypanda.particlesfilter.pf

import android.util.Log
import com.jollypanda.particlesfilter.data.Particle
import com.jollypanda.particlesfilter.data.Robot
import java.util.*


/**
 * @author Yamushev Igor
 * @since  14.05.17
 */
class ParticleFilter(val robot: Robot,
                     val widthConstraint: Int,
                     val heightConsntraint: Int) {

    private val PARTICLES_COUNT = 1000
    private val INIT_WEIGHT = 1.0 / PARTICLES_COUNT
    private val randomizer = Random()
    var particlesList = mutableListOf<Particle>()

    var step = 0

    init {
        for (i in 0..PARTICLES_COUNT - 1) {
            particlesList.add(Particle(randomizer.nextDouble() * widthConstraint,
                                        randomizer.nextDouble() * heightConsntraint,
                                        INIT_WEIGHT)
            )
        }

        Log.e("INIT", "#####################")
        var sumWeight = 0.0
        var maxWeight = 0.0
        var minWeight = particlesList.first().weight
        particlesList.forEach {
            if (it.weight > maxWeight)
                maxWeight = it.weight
            if (it.weight < minWeight)
                minWeight = it.weight
            sumWeight += it.weight
        }
        Log.e("INIT", "MIN-MAX WEIGHT, SW $minWeight - $maxWeight, $sumWeight")
        Log.e("INIT", "#####################")
    }

    fun move() {
        robot.move()

        val forDelete = arrayListOf<Particle>()
        particlesList.forEach {
            it.move(robot)
            if (it.x < 0 || it.x >= widthConstraint
                    || it.y < 0 || it.y >= heightConsntraint)
                it.weight = 0.0
        }
        var i = 0
        i += 1
        particlesList.removeAll(forDelete)
        particlesList.forEach { it.weight = 1.0 / particlesList.size }
        Log.e("MOVE", "#####################")
        var sumWeight = 0.0
        var maxWeight = 0.0
        var minWeight = particlesList.first().weight
        particlesList.forEach {
            if (it.weight > maxWeight)
                maxWeight = it.weight
            if (it.weight < minWeight)
                minWeight = it.weight
            sumWeight += it.weight
        }
        Log.e("MOVE", "MIN-MAX WEIGHT, SW, COUNT $minWeight - $maxWeight, $sumWeight, ${particlesList.size}")
        Log.e("MOVE", "#####################")
    }

    fun filtrate() {
        step += 1

        Log.e("FILTER", "#####################")
        Log.e("FILTER", "STEP $step")

        var sumWeight = 0.0
        var maxWeight = 0.0
        var minWeight = Double.MAX_VALUE
        particlesList.forEach {
            it.distance = Math.sqrt(Math.pow(robot.x - it.x, 2.0) + Math.pow(robot.y - it.y, 2.0))

            if (it.weight > 0.0) {
                it.weight = calculateNormalDistribution(it.distance, 0.0, Math.pow(robot.ERROR, 2.0))
                sumWeight += it.weight
            }

            if (it.weight > maxWeight)
                maxWeight = it.weight
            if (it.weight < minWeight)
                minWeight = it.weight
        }
        Log.e("FILTER", "MIN-MAX WEIGHT, SW, COUNT AFTER DISTANCE $minWeight - $maxWeight, $sumWeight, ${particlesList.size}")

        maxWeight = 0.0
        minWeight = particlesList.first().weight
        particlesList.forEach {
            it.weight /= sumWeight
            if (it.weight > maxWeight)
                maxWeight = it.weight
            if (it.weight < minWeight)
                minWeight = it.weight
        }

        sumWeight = 0.0
        particlesList.forEach {
            sumWeight += it.weight
        }
        Log.e("FILTER", "MIN-MAX WEIGHT, SW, COUNT AFTER NORMALIZE $minWeight - $maxWeight, $sumWeight, ${particlesList.size}")

        val PART_COUNT = particlesList.size
        var index = randomizer.nextInt(PART_COUNT)
        var betta = 0.0
        sumWeight = 0.0
        val newParticles = arrayListOf<Particle>()
        var particle = particlesList[index]
        for (i in 0..PART_COUNT - 1) {
            betta += randomizer.nextDouble() * 2.0 * maxWeight
            while (betta > particle.weight) {
                betta -= particle.weight
                index = (index + 1) % PART_COUNT
                particle = particlesList[index]
            }
            if (!newParticles.contains(particle)) {
                newParticles.add(particle)
                sumWeight += particle.weight
            }
        }
        particlesList = newParticles

        maxWeight = 0.0
        minWeight = particlesList.first().weight
        particlesList.forEach {
            if (it.weight > maxWeight)
                maxWeight = it.weight
            if (it.weight < minWeight)
                maxWeight = it.weight
        }
        Log.e("FILTER", "MIN-MAX WEIGHT, SW, COUNT AFTER RESAMPLE $minWeight-$maxWeight, $sumWeight, ${particlesList.size} ")

//        for (p in particlesList) p.weight /= sumWeight
//
//        maxWeight = 0.0
//        minWeight = particlesList.first().weight
//        sumWeight = 0.0
//        particlesList.forEach {
//            if (it.weight > maxWeight)
//                maxWeight = it.weight
//            if (it.weight < minWeight)
//                maxWeight = it.weight
//            sumWeight += it.weight
//        }
//        Log.e("FILTER", "MIN-MAX WEIGHT, SW, COUNT AFTER RESAMPLE NORMILIZE $minWeight-$maxWeight, $sumWeight, ${particlesList.size}")
        Log.e("FILTER", "#####################")

//        particlesList.sortWith(kotlin.Comparator { l, r ->
//            return@Comparator if (l.distance > r.distance) 1 else if (l.distance < r.distance) -1 else 0
//        })
    }

    private fun calculateNormalDistribution(value: Double, middle: Double, error: Double): Double {
        return 1 / Math.sqrt(2.0 * Math.PI * error) * Math.exp(-Math.pow(value - middle, 2.0) / error)
    }

}