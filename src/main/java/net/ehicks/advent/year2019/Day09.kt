package net.ehicks.advent.year2019

import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

fun main() {
    val input = Files.readString(Paths.get("src/main/resources/year2019/09.txt"))
            .split(",").stream().mapToLong { it.toLong() }.toList()

    val mem = input.mapIndexed { index, i -> index.toLong() to i }.toMap()

    val test = listOf<Long>(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99)
    val testMem = test.mapIndexed { index, i -> index.toLong() to i }.toMap()

    println("Test 1: " + solvePart1(testMem))
    println("Part 1: " + solvePart1(mem))
    println("Part 2: " + solvePart2(input))
}

private fun solvePart1(inputProgram: Map<Long, Long>): Long {
    val programState = runProgramUntilHalt(inputProgram, listOf(1))
    return programState.output.first()
}

private fun solvePart2(inputProgram: List<Long>): Long {
    return 0
}

private fun runProgramUntilHalt(inputProgram: Map<Long, Long>, input: List<Long>): ProgramState {
    val program = inputProgram.toMutableMap()

    var programState = ProgramState(program, 0, input, 0, mutableListOf())
    while (programState.mem[programState.programIndex] != 99L)
        programState = processOpCode(programState)

    return programState
}

data class ProgramState(val mem: MutableMap<Long, Long>, var programIndex: Long, val input: List<Long>, var inputIndex: Int,
                        val output: MutableList<Long>, var halted: Boolean = false, var relativeBase: Long = 0)

private fun processOpCode(programState: ProgramState): ProgramState {
    val program = programState.mem
    val index = programState.programIndex

    val instruction = program[index].toString().reversed()
    val opCode = Integer.parseInt(instruction.substring(0, 1))
    val paramModes = getParamModes(instruction)

    val param1 = getParam(program, index, paramModes.first, 1L, programState.relativeBase)
    val param2 = getParam(program, index, paramModes.second, 2L, programState.relativeBase)
    val param3 = getParam(program, index, paramModes.third, 3L, programState.relativeBase)

    val updatedProgramIndex = when (opCode) {
        1 -> add(program, index, param1, param2, param3)
        2 -> mult(program, index, param1, param2, param3)
        3 -> input(program, index, param1, programState.input[programState.inputIndex])
        4 -> {
            val (updatedProgramIndex, output) = output(program, index, param1)
            programState.output.add(output)
            updatedProgramIndex
        }
        5 -> jumpIfTrue(index, param1, param2)
        6 -> jumpIfFalse(index, param1, param2)
        7 -> lessThan(program, index, param1, param2, param3)
        8 -> equals(program, index, param1, param2, param3)
        9 -> {
            programState.relativeBase += param1
            index + 2
        }
        else -> 0
    }

    val updatedInputIndex = if (opCode == 3) programState.inputIndex + 1 else programState.inputIndex

    return ProgramState(program, updatedProgramIndex, programState.input, updatedInputIndex, programState.output, programState.halted, programState.relativeBase)
}

private fun getParamModes(instruction: String): Triple<Int, Int, Int> {
    val param1Mode = if (instruction.length > 2) Integer.parseInt(instruction.substring(2, 3)) else 0
    val param2Mode = if (instruction.length > 3) Integer.parseInt(instruction.substring(3, 4)) else 0
    val param3Mode = if (instruction.length > 4) Integer.parseInt(instruction.substring(4, 5)) else 0
    return Triple(param1Mode, param2Mode, param3Mode)
}

private fun getParam(data: MutableMap<Long, Long>, index: Long, paramMode: Int, offset: Long, relativeBase: Long): Long {
    val rawAddress = data.getOrDefault(index + offset, 0)
    return when (paramMode) {
        0 -> data.getOrDefault(rawAddress, 0)
        1 -> rawAddress
        2 -> data.getOrDefault(relativeBase + rawAddress, 0)
        else -> throw Exception("Invalid Param Mode")
    }
}

private fun mult(data: MutableMap<Long, Long>, index: Long, param1: Long, param2: Long, param3Address: Long): Long {
    data[param3Address] = param1 * param2
    return index + 4
}

private fun add(data: MutableMap<Long, Long>, index: Long, param1: Long, param2: Long, param3Address: Long): Long {
    data[param3Address] = param1 + param2
    return index + 4
}

private fun input(data: MutableMap<Long, Long>, index: Long, param1Address: Long, input: Long): Long {
    data[param1Address] = input
    return index + 2
}

private fun output(data: MutableMap<Long, Long>, index: Long, param1Address: Long): Pair<Long, Long> {
    val output = data.getOrDefault(param1Address, 0)
    return Pair(index + 2, output)
}

private fun jumpIfTrue(index: Long, param1: Long, param2: Long): Long {
    return if (param1 != 0L) param2 else index + 3
}

private fun jumpIfFalse(index: Long, param1: Long, param2: Long): Long {
    return if (param1 == 0L) param2 else index + 3
}

private fun lessThan(data: MutableMap<Long, Long>, index: Long, param1: Long, param2: Long, param3Address: Long): Long {
    data[param3Address] = if (param1 < param2) 1L else 0
    return index + 4
}

private fun equals(data: MutableMap<Long, Long>, index: Long, param1: Long, param2: Long, param3Address: Long): Long {
    data[param3Address] = if (param1 == param2) 1L else 0
    return index + 4
}