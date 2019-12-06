package net.ehicks.advent.year2019

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

fun main() {
    val input = Files.readString(Paths.get("src/main/resources/year2019/05.txt"))
            .split(",").stream().mapToInt { Integer.parseInt(it) }.toList()

    println("Part 1: " + solvePart1(input))
//    println("Part 2: " + solvePart2(input))
}

private fun solvePart1(input: List<Int>): Int {
    val data = input.toMutableList()

    var index = 0
    while (data[index] != 99)
        index = processOpCode(data, index)

    return data[0]
}

private fun solvePart2(input: List<Int>): Int {

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

private fun processOpCode(data: MutableList<Int>, index: Int): Int {
    val originalInstruction = data[index]
    val instruction = data[index].toString().reversed()
    val opCode = Integer.parseInt(instruction.substring(0, 1))
    val param1Mode = if (instruction.length > 2) Integer.parseInt(instruction.substring(2, 3)) else 0
    val param2Mode = if (instruction.length > 3) Integer.parseInt(instruction.substring(3, 4)) else 0
    val param3Mode = if (instruction.length > 4) Integer.parseInt(instruction.substring(4, 5)) else 0

    val param1 = if (index < data.size - 1) {
        if (param1Mode == 0) data[data[index + 1]] else data[index + 1]
    } else 0


    val input = 1

    when (opCode) {
        1 -> add(data, index, param1Mode, param2Mode)
        2 -> mult(data, index, param1Mode, param2Mode)
        3 -> if (param1Mode == 0) data[data[index + 1]] = input else data[index + 1] = input
        4 -> if (param1Mode == 0) println(data[data[index + 1]]) else println(data[index + 1])
        else -> 0
    }

    val jump =
            if (opCode == 1 || opCode == 2) 4
            else if (opCode == 3 || opCode == 4) 2
            else 2

    return index + jump
}

private fun mult(data: MutableList<Int>, index: Int, param1Mode: Int, param2Mode: Int) {
    val param1 = if (param1Mode == 0) data[data[index + 1]] else data[index + 1]
    val param2 = if (param2Mode == 0) data[data[index + 2]] else data[index + 2]
    data[data[index + 3]] = param1 * param2
}

private fun add(data: MutableList<Int>, index: Int, param1Mode: Int, param2Mode: Int) {
    val param1 = if (param1Mode == 0) data[data[index + 1]] else data[index + 1]
    val param2 = if (param2Mode == 0) data[data[index + 2]] else data[index + 2]
    data[data[index + 3]] = param1 + param2
}