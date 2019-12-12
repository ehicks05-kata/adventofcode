package net.ehicks.advent.year2019

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs
import kotlin.math.min

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/year2019/10.txt"))
    val asteroidMap = input.map { row -> row.map { it == '#'  } }
    println("Part 1: " + solvePart1(asteroidMap))
    println("Part 2: " + solvePart2(asteroidMap))
}

private fun solvePart1(asteroidMap: List<List<Boolean>>): Map.Entry<Coord, Int> {
    val w = asteroidMap[0].size
    val h = asteroidMap.size

    val originToAsteroidsInSight = mutableMapOf<Coord, Int>()

    for (x in 0 until h)
        for (y in 0 until w) {
            if (!asteroidMap[y][x])
                continue

            val angleCounts = mutableMapOf<Pair<Int, Int>, Int>()
            val origin = Coord(x, y)
            val visited = mutableSetOf(origin)
            var neighbors = mutableSetOf<Coord>()
            neighbors.addAll(getNeighbors(origin, asteroidMap, visited))

            while (neighbors.isNotEmpty())
            {
                // process each neighbor
                neighbors
                        .filter { asteroidMap[it.y][it.x] }
                        .forEach { processNeighbor(origin, it, angleCounts) }

                // replace 'neighbors' with each neighbor's neighbor
                visited.addAll(neighbors)
                neighbors = neighbors.flatMap { getNeighbors(it, asteroidMap, visited) }.toMutableSet()
            }

            originToAsteroidsInSight[origin] = angleCounts.size
        }

    return originToAsteroidsInSight.maxBy { it.value }!!
}

private fun processNeighbor(origin: Coord, neighbor: Coord, angleCounts: MutableMap<Pair<Int, Int>, Int>) {
    var deltaX = neighbor.x - origin.x
    var deltaY = neighbor.y - origin.y

    for (i in min(abs(deltaX), abs(deltaY)) downTo 2)
    {
        if (i < 2)
            break
        if (deltaX % i == 0 && deltaY % i == 0)
        {
            deltaX /= i
            deltaY /= i
            break
        }
    }

    if (deltaX == 0) deltaY /= abs(deltaY)
    if (deltaY == 0) deltaX /= abs(deltaX)

    angleCounts.merge(Pair(deltaX, deltaY), 1) { t, u -> t + u }
}

private fun solvePart2(asteroidMap: List<List<Boolean>>): Int {
    return 0
}

private data class Coord(val x: Int, val y: Int)

private fun getNeighbors(origin: Coord, asteroidMap: List<List<Boolean>>, visited : Set<Coord>): Set<Coord> {
    val neighbors = mutableSetOf<Coord>()
    if (origin.x > 0) neighbors.add(Coord(origin.x - 1, origin.y))
    if (origin.y > 0) neighbors.add(Coord(origin.x, origin.y - 1))
    if (origin.x < asteroidMap[0].size - 1) neighbors.add(Coord(origin.x + 1, origin.y))
    if (origin.y < asteroidMap.size - 1) neighbors.add(Coord(origin.x, origin.y + 1))
    return neighbors subtract visited
}