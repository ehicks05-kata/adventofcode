package net.ehicks.advent.year2019

import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

fun main() {
    val input = Files.readString(Paths.get("src/main/resources/year2019/09.txt"))
            .split(",").stream().mapToLong { it.toLong() }.toList()

    val mem = input.mapIndexed { index, i -> index.toLong() to i }.toMap()
    println("Part 1: " + solvePart1(mem))
    println("Part 2: " + solvePart2(mem))
}

private fun solvePart1(inputProgram: Map<Long, Long>): Long {
    val programState = runProgramUntilHalt(inputProgram, listOf(1L))
    return programState.output.first()
}

private fun solvePart2(inputProgram: Map<Long, Long>): Long {
    val programState = runProgramUntilHalt(inputProgram, listOf(2L))
    return programState.output.first()
}

private fun runProgramUntilHalt(inputProgram: Map<Long, Long>, input: List<Long>): MachineState {
    val program = inputProgram.toMutableMap()

    var programState = MachineState(program, 0, input, 0, mutableListOf())
    while (programState.mem[programState.programIndex] != 99L)
        programState = processOpCode(programState)

    return programState
}

data class MachineState(val mem: MutableMap<Long, Long>, var programIndex: Long, val input: List<Long>, var inputIndex: Int,
                        val output: MutableList<Long>, var halted: Boolean = false, var relativeBase: Long = 0)

private fun processOpCode(machineState: MachineState): MachineState {
    val memory = machineState.mem
    val index = machineState.programIndex

    val instruction = memory[index].toString().reversed()
    val opCode = Integer.parseInt(instruction.substring(0, 1))
    val paramModes = getParamModes(instruction)

    var isWrite = false
    if (opCode == 3) isWrite = true

    val param1 = getParam(memory, index, machineState.relativeBase, isWrite, 1L, paramModes.first)
    val param2 = getParam(memory, index, machineState.relativeBase, isWrite, 2L, paramModes.second)
    val param3 = getParam(memory, index, machineState.relativeBase, true, 3L, paramModes.third)

    val updatedProgramIndex = when (opCode) {
        1 -> add(memory, index, param1, param2, param3)
        2 -> mult(memory, index, param1, param2, param3)
        3 -> input(memory, index, param1, machineState.input[machineState.inputIndex])
        4 -> output(index, param1, machineState.output)
        5 -> jumpIfTrue(index, param1, param2)
        6 -> jumpIfFalse(index, param1, param2)
        7 -> lessThan(memory, index, param1, param2, param3)
        8 -> equals(memory, index, param1, param2, param3)
        9 -> relativeBaseOffset(index, param1, machineState)
        else -> 0
    }

    val updatedInputIndex = if (opCode == 3) machineState.inputIndex + 1 else machineState.inputIndex

    return MachineState(memory, updatedProgramIndex, machineState.input, updatedInputIndex, machineState.output, machineState.halted, machineState.relativeBase)
}

private fun getParamModes(instruction: String): Triple<Int, Int, Int> {
    val param1Mode = if (instruction.length > 2) Integer.parseInt(instruction.substring(2, 3)) else 0
    val param2Mode = if (instruction.length > 3) Integer.parseInt(instruction.substring(3, 4)) else 0
    val param3Mode = if (instruction.length > 4) Integer.parseInt(instruction.substring(4, 5)) else 0
    return Triple(param1Mode, param2Mode, param3Mode)
}

private fun getParam(data: MutableMap<Long, Long>, index: Long, relativeBase: Long, isWrite: Boolean, offset: Long, paramMode: Int): Long {
    val rawAddress = data.getOrDefault(index + offset, 0)

    return when (isWrite) {
        true -> {
            when (paramMode) {
                0 -> rawAddress
                1 -> throw Exception("Cannot use immediate mode when writing")
                2 -> relativeBase + rawAddress
                else -> throw Exception("Invalid Param Mode")
            }
        }
        false -> {
            when (paramMode) {
                0 -> data.getOrDefault(rawAddress, 0)
                1 -> rawAddress
                2 -> data.getOrDefault(relativeBase + rawAddress, 0)
                else -> throw Exception("Invalid Param Mode")
            }
        }
    }
}

private fun add(data: MutableMap<Long, Long>, index: Long, param1: Long, param2: Long, param3Address: Long): Long {
    data[param3Address] = param1 + param2
    return index + 4
}

private fun mult(data: MutableMap<Long, Long>, index: Long, param1: Long, param2: Long, param3Address: Long): Long {
    data[param3Address] = param1 * param2
    return index + 4
}

private fun input(data: MutableMap<Long, Long>, index: Long, param1Address: Long, input: Long): Long {
    data[param1Address] = input
    return index + 2
}

private fun output(index: Long, param1: Long, machineOutput: MutableList<Long>): Long {
    machineOutput.add(param1)
    return index + 2
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

private fun relativeBaseOffset(index: Long, param1: Long, machineState: MachineState): Long {
    machineState.relativeBase += param1
    return index + 2
}