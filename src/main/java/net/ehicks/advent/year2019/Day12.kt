package net.ehicks.advent.year2019

import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs

// full disclosure: saw a hint for how to approach part2
fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/year2019/12.txt"))
    println("Part 1: " + solvePart1(input.map { inputLineToMoon(it) }))
    println("Part 2: " + solvePart2(input.map { inputLineToMoon(it) }))
}

private fun inputLineToMoon(input: String): Moon {
    val chunks = input
            .substring(1, input.length - 1)
            .split(", ")
            .map { it.substring(it.indexOf("=") + 1).toInt() }

    return Moon(chunks[0], chunks[1], chunks[2])
}

data class Moon(var x: Int, var y: Int, var z: Int, var vx: Int = 0, var vy: Int = 0, var vz: Int = 0) {
    fun applyV() {
        x += vx
        y += vy
        z += vz
    }

    val potEnergy: Int
        get() = abs(x) + abs(y) + abs(z)

    val kinEnergy: Int
        get() = abs(vx) + abs(vy) + abs(vz)

    val energy: Int
        get() = potEnergy * kinEnergy
}

private fun solvePart1(moons: List<Moon>): Int {
    for (step in 1..1000)
        stepSimulation(moons)

    return moons.map { it.energy }.sum()
}

private fun solvePart2(moons: List<Moon>): Long {
    val xInitialState = moons.flatMap { listOf(it.x, it.vx) }
    val yInitialState = moons.flatMap { listOf(it.y, it.vy) }
    val zInitialState = moons.flatMap { listOf(it.z, it.vz) }

    var xCycleLength = 0L
    var yCycleLength = 0L
    var zCycleLength = 0L
    var steps = 0L

    while (true) {
        steps++
        stepSimulation(moons)

        if (xCycleLength == 0L && moons.flatMap { listOf(it.x, it.vx) } == xInitialState)
            xCycleLength = steps
        if (yCycleLength == 0L && moons.flatMap { listOf(it.y, it.vy) } == yInitialState)
            yCycleLength = steps
        if (zCycleLength == 0L && moons.flatMap { listOf(it.z, it.vz) } == zInitialState)
            zCycleLength = steps

        if (xCycleLength > 0 && yCycleLength > 0 && zCycleLength > 0)
            break
    }

    return lcm(lcm(BigInteger(xCycleLength.toString()), BigInteger(yCycleLength.toString())), BigInteger(zCycleLength.toString())).longValueExact()
}

private fun lcm(a: BigInteger, b: BigInteger): BigInteger {
    return a.multiply(b.div(a.gcd(b)))
}

private fun stepSimulation(moons: List<Moon>) {
    for (i in moons.indices) {
        for (j in i + 1 until moons.size) {
            val m1 = moons[i]
            val m2 = moons[j]

            m1.vx += getDeltaV(m1.x, m2.x)
            m1.vy += getDeltaV(m1.y, m2.y)
            m1.vz += getDeltaV(m1.z, m2.z)
            m2.vx += getDeltaV(m2.x, m1.x)
            m2.vy += getDeltaV(m2.y, m1.y)
            m2.vz += getDeltaV(m2.z, m1.z)
        }
    }

    moons.forEach { it.applyV() }
}

private fun getDeltaV(i: Int, j: Int): Int {
    return when {
        i < j -> 1
        i > j -> -1
        else -> 0
    }
}