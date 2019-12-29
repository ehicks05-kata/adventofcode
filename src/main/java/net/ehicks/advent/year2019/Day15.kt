package net.ehicks.advent.year2019

import edu.princeton.cs.algs4.StdDraw
import java.awt.Color
import java.awt.Point
import java.lang.Thread.sleep
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.math.abs

fun main() {
    val input = Files.readString(Paths.get("src/main/resources/year2019/15.txt"))

    val grid = runRepairRobot(IC(input), true)
    println("Part 1: " + solvePart1(grid))
    println("Part 2: " + solvePart2(grid))
}

private fun solvePart1(grid: Map<Point, Long>): Int{
    return getDistance(grid, Point(0,0), grid.entries.find { it.value == 2L }!!.key)
}

private fun solvePart2(grid: Map<Point, Long>): Int{
    val oxygenSystemLocation = grid.entries.find { it.value == 2L }!!.key
    return grid.entries.filter { it.value == 1L }.map { getDistance(grid, oxygenSystemLocation, it.key) }.max()!!
}

private fun runRepairRobot(ic: IC, render: Boolean = false): Map<Point, Long> {
    var step = 0
    var scale = 4.0
    initStdDraw(scale)

    val grid = mutableMapOf<Point, Long>()

    var location = Point(0, 0)
    grid[location] = 1
    val pointsToExplore = filterUnexplored(grid, getNeighbors(location))

    while (pointsToExplore.isNotEmpty()) {
        if (ic.machineState.halted)
            break

        val targetDestination = sortByDistance(location, pointsToExplore)[0]
        val pathToDestination = findPath(grid, location, targetDestination)

        for (pathPoint in pathToDestination) {
            val movementCommand = getMovementCommand(location, pathPoint)
            val targetLocation = transformLocation(movementCommand.toInt(), location)
            ic.feedInput(movementCommand)
            ic.run()
            val statusCode = ic.getAndClearOutput()[0]

            if (!grid.containsKey(targetLocation)) {
                grid[targetLocation] = statusCode
                pointsToExplore.remove(targetLocation)
                
                if (statusCode != 0L) {
                    val newPointsToExplore = filterUnexplored(grid, getNeighbors(targetLocation))
                    newPointsToExplore.removeAll(pointsToExplore)
                    pointsToExplore.addAll(newPointsToExplore)
                    location = targetLocation
                }

            }
            else {
                if (grid[targetLocation] != 0L)
                    location = targetLocation
            }

            if (abs(location.x) > scale - 2 || abs(location.y) > scale - 2)
            {
                scale += 1
                StdDraw.setScale(-scale, scale)
            }

            step++
            if (render)
                render(grid, location, scale, step)
            sleep(1)
        }
    }

    return grid
}

private fun sortByDistance(start: Point, list: List<Point>): List<Point> {
    return list.sortedBy { getManhattanDistance(start, it) }
}

private fun getDistance(grid: Map<Point, Long>, start: Point, end: Point): Int {
    return findPath(grid, start, end).size
}

private fun findPath(grid: Map<Point, Long>, start: Point, end: Point): List<Point> {
    if (start == end)
        return listOf()

    val cameFrom = mutableMapOf<Point, Point?>()
    cameFrom[start] = null
    
    val queue = ArrayDeque<Point>()
    queue.add(start)

    outer@
    while (queue.isNotEmpty()) {
        val current = queue.pop()

        val neighbors =
            if (getManhattanDistance(current, end) == 1)
                filterNotAWall(grid, getNeighbors(current))
            else
                filterTraversable(grid, getNeighbors(current))
        
        for (next in neighbors) {
            if (!cameFrom.containsKey(next)) {
                queue.add(next)
                cameFrom[next] = current
            }

            if (next == end) {
                break@outer
            }
        }
    }

    val path = mutableListOf<Point>()
    var temp = end

    while (temp != start) {
        path.add(temp)
        if (cameFrom[temp] == null)
            throw Exception()
        temp = cameFrom[temp]!!
    }
    
    return path.reversed()
}

// Only four movement commands are understood: north (1), south (2), west (3), and east (4).
private fun getMovementCommand(start: Point, end: Point): Long {
    if (end.y > start.y) return 1
    if (end.y < start.y) return 2
    if (end.x < start.x) return 3
    if (end.x > start.x) return 4

    return 0
}

private fun getManhattanDistance(start: Point, end: Point): Int {
    return abs(start.x - end.x) + abs(start.y - end.y)
}

private fun transformLocation(direction: Int, location: Point): Point {
    return when (direction) {
        1 -> Point(location.x,     location.y + 1)
        2 -> Point(location.x,     location.y - 1)
        3 -> Point(location.x - 1, location.y)
        4 -> Point(location.x + 1, location.y)
        else -> throw Exception("unknown direction")
    }
}

private fun filterUnexplored(grid: Map<Point, Long>, points: Set<Point>): MutableList<Point> {
    return points.filter { !grid.containsKey(it) }.toMutableList()
}

private fun filterTraversable(grid: Map<Point, Long>, points: Set<Point>): MutableList<Point> {
    return points.filter { grid[it] != 0L }.toMutableList()
}

private fun filterNotAWall(grid: Map<Point, Long>, points: Set<Point>): MutableList<Point> {
    return points.filter { !grid.containsKey(it) || grid[it] != 0L }.toMutableList()
}

private fun getNeighbors(location: Point): Set<Point> {
    return (1..4).map { transformLocation(it, location) }.toSet()
}

private fun initStdDraw(scale: Double) {
    StdDraw.enableDoubleBuffering()
    StdDraw.setCanvasSize()
    StdDraw.setScale(-scale, scale)
}

private fun render(grid: MutableMap<Point, Long>, location: Point = Point(0, 0), scale: Double = 64.0, step: Int = -1) {
    StdDraw.clear(Color.BLACK)
    grid.forEach {

        val color = when (it.value.toInt()) {
            0 -> Color.LIGHT_GRAY
            1 -> Color.DARK_GRAY
            2 -> Color.GREEN
            else -> throw Exception("Unknown grid value")
        }

        StdDraw.setPenColor(color)
        StdDraw.filledRectangle(it.key.x.toDouble() + .5, it.key.y.toDouble() - .5, .5, .5)

        if (it.key == location) {
            StdDraw.setPenColor(Color.WHITE)
            StdDraw.filledCircle(it.key.x.toDouble() + .5, it.key.y.toDouble() - .5, .4)
        }

        if (it.key == Point(0, 0)) {
            StdDraw.setPenColor(Color.WHITE)
            StdDraw.line(it.key.x.toDouble(), it.key.y.toDouble() - 1, it.key.x.toDouble() + 1, it.key.y.toDouble())
            StdDraw.line(it.key.x.toDouble(), it.key.y.toDouble(), it.key.x.toDouble() + 1, it.key.y.toDouble() - 1)
        }
    }
    StdDraw.setPenColor(Color.RED)
    StdDraw.textLeft(-scale * .98, scale * .95, "Step: $step")
    StdDraw.textLeft(-scale * .98, scale * .88, "Location: (${location.x}, ${location.y})")
    StdDraw.show()
}