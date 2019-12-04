package net.ehicks.advent.year2019

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val lines = Files.readAllLines(Paths.get("src/main/resources/year2019/01.txt"));
    println("part 1: " + lines.stream().mapToInt { massToFuel(it.toInt()) }.sum())
    println("part 2: " + lines.stream().mapToInt { massToFuel2(it.toInt()) }.sum())
}

private fun massToFuel(mass: Int): Int {
    return mass / 3 - 2
}

private fun massToFuel2(mass: Int): Int {
    var sum = massToFuel(mass)
    var totalSum = 0
    while (sum > 0) {
        totalSum += sum
        sum = massToFuel(sum)
    }
    return totalSum
}