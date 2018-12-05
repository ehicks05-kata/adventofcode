package net.ehicks.advent.year2018

import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "03.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(input)
}

private fun solveInput(input: List<String>) {
    var overlappingInches = 0
    val length = 2000
    val grid = Array(length) { Array(length) { MutableList<Int>(0) { 0 } } }
    val claimIds: MutableSet<Int> = HashSet<Int>()

    input.forEach { claim ->
        val claimId = claim.substring(claim.indexOf("#") + 1, claim.indexOf("@") - 1).toInt()
        claimIds.add(claimId)

        val starts = claim.substring(claim.indexOf("@") + 2, claim.indexOf(":"))
        val lengths = claim.substring(claim.indexOf(":") + 2)
        val x1 = starts.substring(0, starts.indexOf(",")).toInt()
        val y1 = starts.substring(starts.indexOf(",") + 1).toInt()

        val x2 = x1 + lengths.substring(0, lengths.indexOf("x")).toInt()
        val y2 = y1 + lengths.substring(lengths.indexOf("x") + 1).toInt()

        for (x in x1 until x2)
            for (y in y1 until y2)
                grid[x][y].add(claimId)
    }

    for (x in 0 until length)
        for (y in 0 until length) {
            if (grid[x][y].size > 1)
            {
                overlappingInches++
                grid[x][y].forEach { claimIds.remove(it) }
            }
        }

    println("part 1: " + overlappingInches)
    println("part 2: " + claimIds)
}
