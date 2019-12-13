package net.ehicks.advent.year2019

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.atan2
import kotlin.math.sqrt

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/year2019/10.txt"))
    val asteroidMap = input.map { row -> row.map { it == '#' } }
    val monitoringStationLocation = solvePart1(asteroidMap)
    println("Part 1: $monitoringStationLocation")
    println("Part 2: " + solvePart2(asteroidMap, monitoringStationLocation.key))
}

private fun solvePart1(asteroidMap: List<List<Boolean>>): Map.Entry<Coord, Int> {
    val w = asteroidMap[0].size
    val h = asteroidMap.size

    val originToAsteroidsInSight = mutableMapOf<Coord, Int>()

    for (x in 0 until h)
        for (y in 0 until w) {
            if (!asteroidMap[y][x])
                continue

            val origin = Coord(x, y)
            val angleCounts = determineAngleCounts(origin, asteroidMap)

            originToAsteroidsInSight[origin] = angleCounts.size
        }

    return originToAsteroidsInSight.maxBy { it.value }!!
}

private fun solvePart2(asteroidMap: List<List<Boolean>>, monitoringStationLocation: Coord): Pair<RelativeCoord, Int> {
    val angleCounts = determineAngleCounts(monitoringStationLocation, asteroidMap)

    val asteroidDestructionOrder = mutableListOf<RelativeCoord>()

    while (angleCounts.isNotEmpty()) {
        val keys = angleCounts.keys.sorted()
        keys.forEach { angleKey ->
            val asteroidsOfAngle = angleCounts[angleKey]!!
            asteroidsOfAngle.sortBy { it.distance }
            val destroyedAsteroid = asteroidsOfAngle.first()
            asteroidDestructionOrder.add(destroyedAsteroid)

            angleCounts[angleKey]!!.remove(destroyedAsteroid)
            if (angleCounts[angleKey]!!.isEmpty())
                angleCounts.remove(angleKey)
        }
    }

    val twoHundredth = asteroidDestructionOrder[199]

    return Pair(twoHundredth, twoHundredth.x * 100 + twoHundredth.y)
}

private fun determineAngleCounts(origin: Coord, asteroidMap: List<List<Boolean>>): MutableMap<Double, MutableList<RelativeCoord>> {
    val angleCounts = mutableMapOf<Double, MutableList<RelativeCoord>>()
    val visited = mutableSetOf(origin)
    var neighbors = mutableSetOf<Coord>()
    neighbors.addAll(getNeighbors(origin, asteroidMap, visited))

    while (neighbors.isNotEmpty()) {
        // process each neighbor
        neighbors
                .filter { asteroidMap[it.y][it.x] }
                .forEach { processNeighbor(origin, it, angleCounts) }

        // replace 'neighbors' with each neighbor's neighbor
        visited.addAll(neighbors)
        neighbors = neighbors.flatMap { getNeighbors(it, asteroidMap, visited) }.toMutableSet()
    }
    return angleCounts
}

private fun processNeighbor(origin: Coord, neighbor: Coord, angleCounts: MutableMap<Double, MutableList<RelativeCoord>>) {
    val deltaX = neighbor.x - origin.x
    val deltaY = neighbor.y - origin.y
    val distance = sqrt((deltaX * deltaX + deltaY * deltaY).toDouble())

    var angle = Math.toDegrees(atan2(deltaY.toDouble(), deltaX.toDouble())) - Math.toDegrees(atan2(-1.0, 0.0))
    if (angle < 0)
        angle += 360

    val coords = angleCounts.getOrDefault(angle, mutableListOf())
    coords.add(RelativeCoord(neighbor.x, neighbor.y, distance))
    angleCounts[angle] = coords
}

private data class Coord(val x: Int, val y: Int)
private data class RelativeCoord(val x: Int, val y: Int, val distance: Double)

private fun getNeighbors(origin: Coord, asteroidMap: List<List<Boolean>>, visited: Set<Coord>): Set<Coord> {
    val neighbors = mutableSetOf<Coord>()
    if (origin.x > 0) neighbors.add(Coord(origin.x - 1, origin.y))
    if (origin.y > 0) neighbors.add(Coord(origin.x, origin.y - 1))
    if (origin.x < asteroidMap[0].size - 1) neighbors.add(Coord(origin.x + 1, origin.y))
    if (origin.y < asteroidMap.size - 1) neighbors.add(Coord(origin.x, origin.y + 1))
    return neighbors subtract visited
}