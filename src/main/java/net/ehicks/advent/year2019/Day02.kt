package net.ehicks.advent.year2019

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

fun main() {
    val input = Files.readString(Paths.get("src/main/resources/year2019/02.txt"))
            .split(",").stream().mapToInt { Integer.parseInt(it) }.toList()

    println("Part 1: " + solvePart1(input))
    println("Part 2: " + solvePart2(input))
}

fun solvePart1(input: List<Int>): Int {
    val data = input.toMutableList()

    // restore the "1202 program alarm" state
    data[1] = 12
    data[2] = 2

    var index = 0
    while (data[index] != 99)
        index = processOpCode(data, index)

    return data[0]
}

fun solvePart2(input: List<Int>): Int {

    for (noun in 1..99)
        for (verb in 1..99) {
            val data = input.toMutableList()
            data[1] = noun
            data[2] = verb

            var index = 0
            while (data[index] != 99)
                index = processOpCode(data, index)

            if (data[0] == 19690720)
                return (100 * noun) + verb
        }

    return 0
}

fun processOpCode(data: MutableList<Int>, index: Int): Int {
    val value = when (data[index]) {
        1 -> data[data[index + 1]] + data[data[index + 2]]
        2 -> data[data[index + 1]] * data[data[index + 2]]
        else -> 0
    }

    data[data[index + 3]] = value

    return index + 4
}