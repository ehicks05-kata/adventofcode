package net.ehicks.advent.year2019

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/year2019/03.txt"))

    val wire1 = input[0].split(",").toList()
    val wire2 = input[1].split(",").toList()

    println("Part 1: " + solvePart1(wire1, wire2))
    println("Part 2: " + solvePart2(wire1, wire2))
}

data class Coordinate(val x: Int, val y: Int, val steps: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinate

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

data class OriginAndCoordinates(val origin: Coordinate, val coordinates: Set<Coordinate>)

private fun solvePart1(wire1: List<String>, wire2: List<String>): Int {
    val wire1Coordinates = getWireCoordinates(wire1)
    val wire2Coordinates = getWireCoordinates(wire2)
    val intersections = wire1Coordinates intersect wire2Coordinates

    return intersections.map { coordinateToManhattan(it) }.min()!!
}

private fun solvePart2(wire1: List<String>, wire2: List<String>): Int {
    val wire1Coordinates = getWireCoordinates(wire1)
    val wire2Coordinates = getWireCoordinates(wire2)
    val intersections = wire1Coordinates intersect wire2Coordinates

    return intersections.stream().mapToInt { intersection ->
        val wire1Steps = wire1Coordinates.find { it.x == intersection.x && it.y == intersection.y }!!.steps
        val wire2Steps = wire2Coordinates.find { it.x == intersection.x && it.y == intersection.y }!!.steps

        wire1Steps + wire2Steps
    }.min().asInt
}

private fun coordinateToManhattan(input: Coordinate): Int {
    return Math.abs(input.x) + Math.abs(input.y)
}

private fun getWireCoordinates(wire: List<String>): Set<Coordinate> {
    val coordinates = mutableSetOf<Coordinate>()
    var origin = Coordinate(0, 0, 0)

    for (instruction in wire)
    {
        val result = instructionToCoordinates(origin, instruction)
        origin = result.origin

        val newCoordinates = result.coordinates subtract coordinates
        coordinates.addAll(newCoordinates)
    }

    return coordinates
}

private fun instructionToCoordinates(origin: Coordinate, instruction: String): OriginAndCoordinates {
    val direction = instruction[0]
    val length = instruction.substring(1).toInt()

    var updatedOrigin = Coordinate(0, 0, 0)
    val coordinates = mutableSetOf<Coordinate>()

    if (direction == 'U')
    {
        for (i in 1 .. length)
            coordinates.add(Coordinate(origin.x, origin.y + i, origin.steps + i))
        updatedOrigin = Coordinate(origin.x, origin.y + length, origin.steps + length)
    }
    if (direction == 'D')
    {
        for (i in 1 .. length)
            coordinates.add(Coordinate(origin.x, origin.y - i, origin.steps + i))
        updatedOrigin = Coordinate(origin.x, origin.y - length, origin.steps + length)
    }
    if (direction == 'L')
    {
        for (i in 1 .. length)
            coordinates.add(Coordinate(origin.x - i, origin.y, origin.steps + i))
        updatedOrigin = Coordinate(origin.x - length, origin.y, origin.steps + length)
    }
    if (direction == 'R')
    {
        for (i in 1 .. length)
            coordinates.add(Coordinate(origin.x + i, origin.y, origin.steps + i))
        updatedOrigin = Coordinate(origin.x + length, origin.y, origin.steps + length)
    }

    return OriginAndCoordinates(updatedOrigin, coordinates)
}