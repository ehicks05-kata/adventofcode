package net.ehicks.advent.year2019

import edu.princeton.cs.algs4.StdDraw
import java.awt.Color
import java.awt.Point
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.math.abs

fun main() {
    val input = Files.readString(Paths.get("src/main/resources/year2019/11.txt"))
    println("Part 1: " + solvePart1(IC(input)))
    println("Part 2: " + solvePart2(IC(input)))
}

private fun solvePart1(ic: IC): Int{
    val grid = runPaintRobot(ic, 0L, true)

    return grid.size
}

private fun solvePart2(ic: IC): Int{
    val grid = runPaintRobot(ic, 1L, true)

    return grid.size  // breakpoint here and visually inspect the rendered output
}

private fun runPaintRobot(ic: IC, startingColor: Long, render: Boolean = false): Map<Point, Long> {
    var step = 0
    var scale = 16.0
    initStdDraw(scale)

    val grid = mutableMapOf<Point, Long>()

    var location = Point(0, 0)
    grid[location] = startingColor
    val direction = mutableListOf("N", "E", "S", "W")

    while (true) {
        if (ic.machineState.halted)
            break

        val colorAtLocation = grid.getOrDefault(location, 0L)
        ic.feedInput(colorAtLocation)
        ic.run()

        val output = ic.getAndClearOutput()
        val paintColor = output[0]
        val turn = output[1]

        grid[location] = paintColor

        val turnParsed = if (turn == 0L) "L" else "R"
        Collections.rotate(direction, if (turnParsed == "L") 1 else -1)

        location = updateLocation(direction, location)
        step++

        if (abs(location.x) > scale || abs(location.y) > scale)
        {
            scale += 4
            StdDraw.setScale(-scale, scale)
        }

        if (render)
            render(grid, location, scale, step)
    }

    return grid
}

private fun initStdDraw(scale: Double) {
    StdDraw.enableDoubleBuffering()
    StdDraw.setCanvasSize()
    StdDraw.setScale(-scale, scale)
}

private fun updateLocation(direction: MutableList<String>, location: Point): Point {
    return when (direction.first()) {
        "N" -> Point(location.x,     location.y + 1)
        "E" -> Point(location.x + 1, location.y)
        "S" -> Point(location.x,     location.y - 1)
        "W" -> Point(location.x - 1, location.y)
        else -> throw Exception("unknown direction")
    }
}

private fun render(grid: MutableMap<Point, Long>, location: Point = Point(0, 0), scale: Double = 64.0, step: Int = -1) {
    StdDraw.clear(Color.BLACK)
    grid.forEach {
        val color = if (it.value == 0L) Color.BLACK else Color.LIGHT_GRAY
        StdDraw.setPenColor(color)
        StdDraw.filledRectangle(it.key.x.toDouble(), it.key.y.toDouble(), .5, .5)

        if (it.key == location) {
            StdDraw.setPenColor(Color.WHITE)
            StdDraw.filledCircle(it.key.x.toDouble(), it.key.y.toDouble(), .4)
        }
    }
    StdDraw.setPenColor(Color.RED)
    StdDraw.textLeft(-scale * .98, scale * .95, "Step: $step")
    StdDraw.show()
}