package net.ehicks.advent.year2018

import java.nio.file.Files
import java.nio.file.Path
import java.util.*

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "01.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(Arrays.asList("+3", "+3", "+4", "-2", "-4"))
    solveInput(input)
}

private fun solveInput(input: List<String>) {
    val frequencies: MutableSet<Int> = HashSet()
    var sum = 0

    while (true)
        input.map { it.toInt() }.forEach {
            sum += it

            if (frequencies.contains(sum)) {
                println("freq reached twice: $sum")
                return
            }

            frequencies.add(sum)
        }
}
