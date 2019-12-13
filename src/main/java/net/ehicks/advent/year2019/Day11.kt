package net.ehicks.advent.year2019

import java.awt.Point
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

fun main() {
    val input = Files.readString(Paths.get("src/main/resources/year2019/11.txt"))
    val ic = IC(input, true)

    println("Part 1: " + solvePart1(ic))
    println("Part 2: " + solvePart2())
}

private fun solvePart1(ic: IC): Int{
    val grid = mutableMapOf<Point, Long>()
    val paintedPanels = mutableSetOf<Point>()
    
    var location = Point(0, 0)
    val direction = mutableListOf(0, 1, 2, 3)

    while (true) {
        if (ic.machineState.halted)
            break

        val colorAtLocation = grid.getOrDefault(location, 0)

        ic.feedInput(colorAtLocation)
        ic.run()
        val output = ic.getOutput()
        val paintColor = output[0]
        grid[location] = paintColor

        if (paintColor == 1L)
            paintedPanels.add(location)

        val turn = if (output[1] == 1L) -1 else 1
        Collections.rotate(direction, turn)
        if (direction.first() == 0) location = Point(location.x, location.y + 1)
        if (direction.first() == 1) location = Point(location.x + 1, location.y)
        if (direction.first() == 2) location = Point(location.x, location.y - 1)
        if (direction.first() == 3) location = Point(location.x - 1, location.y)
    }

    return paintedPanels.size
}

private fun solvePart2(): Int{
    return 0
}

