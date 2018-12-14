package net.ehicks.advent.year2018

import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "14.txt")
    val input: String = Files.readString(path)

    solveInput(input)
}

private fun solveInput(input: String) {
    val inputAsInt = input.toInt()
    val scoreboard = mutableListOf(3, 7)
    var elf1 = 0
    var elf2 = 1

    while (true) {
        val sum = scoreboard[elf1] + scoreboard[elf2]
        val sumString = sum.toString()

        sumString.forEach {
            scoreboard.add(it.toString().toInt())

            if (scoreboard.size == inputAsInt + 10)
                println("part 1: ${scoreboard.subList(inputAsInt, inputAsInt + 10).joinToString(separator = "")}")

            if (scoreboard.size >= input.length)
                if (scoreboard.subList(scoreboard.size - input.length, scoreboard.size).joinToString(separator = "") == input)
                    println("part 2: $input first appears after ${scoreboard.size - input.length} recipes.")
        }

        elf1 = (elf1 + 1 + scoreboard[elf1]) % scoreboard.size
        elf2 = (elf2 + 1 + scoreboard[elf2]) % scoreboard.size
    }
}