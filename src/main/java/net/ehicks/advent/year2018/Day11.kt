package net.ehicks.advent.year2018

import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "11.txt")
    val input: String = Files.readString(path)

    solveInput(input)
}

private fun solveInput(input: String) {
    val gridSerialNumber = input.toInt()

    val grid = MutableList(300) { MutableList(300) { 0 } }

    for (x in 0 until 300)
        for (y in 0 until 300) {
            val nominalX = x + 1
            val nominalY = y + 1

            val rackId = nominalX + 10
            var powerLevel = rackId * nominalY
            powerLevel += gridSerialNumber
            powerLevel *= rackId

            val powerLevelString = powerLevel.toString()
            powerLevel =
                    if (powerLevelString.length >= 3)
                        powerLevelString[powerLevelString.length - 3].toString().toInt()
                    else
                        0

            powerLevel -= 5

            grid[x][y] = powerLevel
        }

    var part1MaxSubGridPower = 0
    var part1Coords = Pair(0, 0)
    var part2MaxSubGridPower = 0
    var part2Coords = Triple(0, 0, 0)

    for (size in 0..300) {
        println("on size $size")
        for (x in 0 until 300 - (size - 1))
            for (y in 0 until 300 - (size - 1)) {
                val subGrid = grid.subList(x, x + size).map { it.subList(y, y + size) }
                val subGridPower = subGrid.sumBy { it.sum() }

                if (size == 3 && subGridPower > part1MaxSubGridPower) {
                    part1MaxSubGridPower = subGridPower
                    part1Coords = Pair(x + 1, y + 1)
                    println("part1: coords: $part1Coords, size: $size, power: $part1MaxSubGridPower")
                }

                if (subGridPower > part2MaxSubGridPower) {
                    part2MaxSubGridPower = subGridPower
                    part2Coords = Triple(x + 1, y + 1, size)
                    println("part2: coords: $part2Coords, size: $size, power: $part2MaxSubGridPower")
                }
            }
    }

    println("part 1: $part1Coords")
    println("part 2: $part2Coords")
}