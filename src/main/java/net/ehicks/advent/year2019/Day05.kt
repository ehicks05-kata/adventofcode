package net.ehicks.advent.year2019

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

fun main() {
    val input = Files.readString(Paths.get("src/main/resources/year2019/05.txt"))
            .split(",").stream().mapToInt { Integer.parseInt(it) }.toList()

    print("Part 1: ")
    solvePart1(input, 1)
    print("Part 2: ")
    solvePart2(input, 5)
}

private fun solvePart1(inputProgram: List<Int>, inputValue: Int): Int {
    val data = inputProgram.toMutableList()

    var index = 0
    while (data[index] != 99)
        index = processOpCode(data, index, inputValue)

    return data[0]
}

private fun solvePart2(input: List<Int>, inputValue: Int): Int {
    val data = input.toMutableList()

    var index = 0
    while (data[index] != 99)
        index = processOpCode(data, index, inputValue)

    return 0
}

private fun processOpCode(data: MutableList<Int>, index: Int, inputValue: Int): Int {
    val originalInstruction = data[index]
    val instruction = data[index].toString().reversed()
    val opCode = Integer.parseInt(instruction.substring(0, 1))
    val param1Mode = if (instruction.length > 2) Integer.parseInt(instruction.substring(2, 3)) else 0
    val param2Mode = if (instruction.length > 3) Integer.parseInt(instruction.substring(3, 4)) else 0
    val param3Mode = if (instruction.length > 4) Integer.parseInt(instruction.substring(4, 5)) else 0

    return when (opCode) {
        1 -> add(data, index, param1Mode, param2Mode)
        2 -> mult(data, index, param1Mode, param2Mode)
        3 -> input(data, index, param1Mode, inputValue)
        4 -> output(data, index, param1Mode)
        5 -> jumpIfTrue(data, index, param1Mode, param2Mode)
        6 -> jumpIfFalse(data, index, param1Mode, param2Mode)
        7 -> lessThan(data, index, param1Mode, param2Mode)
        8 -> equals(data, index, param1Mode, param2Mode)
        else -> 0
    }
}

private fun mult(data: MutableList<Int>, index: Int, param1Mode: Int, param2Mode: Int): Int {
    val param1 = if (param1Mode == 0) data[data[index + 1]] else data[index + 1]
    val param2 = if (param2Mode == 0) data[data[index + 2]] else data[index + 2]
    data[data[index + 3]] = param1 * param2

    return index + 4
}

private fun add(data: MutableList<Int>, index: Int, param1Mode: Int, param2Mode: Int): Int {
    val param1 = if (param1Mode == 0) data[data[index + 1]] else data[index + 1]
    val param2 = if (param2Mode == 0) data[data[index + 2]] else data[index + 2]
    data[data[index + 3]] = param1 + param2

    return index + 4
}

private fun input(data: MutableList<Int>, index: Int, param1Mode: Int, input: Int): Int {
    if (param1Mode == 0) data[data[index + 1]] = input else data[index + 1] = input

    return index + 2
}

private fun output(data: MutableList<Int>, index: Int, param1Mode: Int): Int {
    if (param1Mode == 0) println(data[data[index + 1]]) else println(data[index + 1])

    return index + 2
}

private fun jumpIfTrue(data: MutableList<Int>, index: Int, param1Mode: Int, param2Mode: Int): Int {
    val param1 = if (param1Mode == 0) data[data[index + 1]] else data[index + 1]
    val param2 = if (param2Mode == 0) data[data[index + 2]] else data[index + 2]

    return if (param1 != 0) param2 else index + 3
}

private fun jumpIfFalse(data: MutableList<Int>, index: Int, param1Mode: Int, param2Mode: Int): Int {
    val param1 = if (param1Mode == 0) data[data[index + 1]] else data[index + 1]
    val param2 = if (param2Mode == 0) data[data[index + 2]] else data[index + 2]

    return if (param1 == 0) param2 else index + 3
}

private fun lessThan(data: MutableList<Int>, index: Int, param1Mode: Int, param2Mode: Int): Int {
    val param1 = if (param1Mode == 0) data[data[index + 1]] else data[index + 1]
    val param2 = if (param2Mode == 0) data[data[index + 2]] else data[index + 2]

    data[data[index + 3]] = if (param1 < param2) 1 else 0

    return index + 4
}

private fun equals(data: MutableList<Int>, index: Int, param1Mode: Int, param2Mode: Int): Int {
    val param1 = if (param1Mode == 0) data[data[index + 1]] else data[index + 1]
    val param2 = if (param2Mode == 0) data[data[index + 2]] else data[index + 2]

    data[data[index + 3]] = if (param1 == param2) 1 else 0

    return index + 4
}