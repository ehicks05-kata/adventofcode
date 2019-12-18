package net.ehicks.advent.year2019

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

// definitely messy code
fun main() {
    val input = Files.readString(Paths.get("src/main/resources/year2019/07.txt"))
            .split(",").stream().mapToInt { Integer.parseInt(it) }.toList()

    println("Part 1: " + solvePart1(input))
    println("Part 2: " + solvePart2(input))
}

private fun solvePart1(inputProgram: List<Int>): Int {
    val phases = (0..4).toSet()
    val phasePermutations = getPhasePermutations(phases)

    return phasePermutations.stream().mapToInt { phasePermutationToFinalOutput1(inputProgram, it) }.max().orElse(-1)
}

private fun solvePart2(inputProgram: List<Int>): Int {
    val phases = (5..9).toSet()
    val phasePermutations = getPhasePermutations(phases)

    return phasePermutations.stream().mapToInt { phasePermutationToFinalOutput2(inputProgram, it) }.max().orElse(-1)
}

private fun phasePermutationToFinalOutput1(inputProgram: List<Int>, phasePermutation: List<Int>): Int {
    val output1 = runProgramUntilHalt(inputProgram, listOf(phasePermutation[0], 0)).output[0]
    val output2 = runProgramUntilHalt(inputProgram, listOf(phasePermutation[1], output1)).output[0]
    val output3 = runProgramUntilHalt(inputProgram, listOf(phasePermutation[2], output2)).output[0]
    val output4 = runProgramUntilHalt(inputProgram, listOf(phasePermutation[3], output3)).output[0]
    val output5 = runProgramUntilHalt(inputProgram, listOf(phasePermutation[4], output4)).output[0]
    return output5
}

private fun phasePermutationToFinalOutput2(inputProgram: List<Int>, phasePermutation: List<Int>): Int {
    var programState1 = runProgram2UntilOutputOrHalt("IC1", inputProgram, listOf(phasePermutation[0], 0))
    var programState2 = runProgram2UntilOutputOrHalt("IC2", inputProgram, listOf(phasePermutation[1], programState1.output.last()))
    var programState3 = runProgram2UntilOutputOrHalt("IC3", inputProgram, listOf(phasePermutation[2], programState2.output.last()))
    var programState4 = runProgram2UntilOutputOrHalt("IC4", inputProgram, listOf(phasePermutation[3], programState3.output.last()))
    var programState5 = runProgram2UntilOutputOrHalt("IC5", inputProgram, listOf(phasePermutation[4], programState4.output.last()))

    while (true) {
        programState1 = runProgram2UntilOutputOrHalt("IC1", programState1, listOf(programState5.output.last()))
        programState2 = runProgram2UntilOutputOrHalt("IC2", programState2, listOf(programState1.output.last()))
        programState3 = runProgram2UntilOutputOrHalt("IC3", programState3, listOf(programState2.output.last()))
        programState4 = runProgram2UntilOutputOrHalt("IC4", programState4, listOf(programState3.output.last()))
        programState5 = runProgram2UntilOutputOrHalt("IC5", programState5, listOf(programState4.output.last()))

        val halted = programState1.halted
                || programState2.halted
                || programState3.halted
                || programState4.halted
                || programState5.halted

        if (halted)
            break
    }

    return programState5.output.last()
}

fun getPhasePermutations(phases: Set<Int>): MutableSet<List<Int>> {
    val completePermutations = mutableSetOf<List<Int>>()

    for (phase in phases)
    {
        val inProgressPermutation = mutableSetOf<Int>()
        inProgressPermutation.add(phase)

        val availablePhases = phases.toMutableSet()
        availablePhases.remove(phase)

        getPhasePermutationsRecur(completePermutations, inProgressPermutation, availablePhases)
    }

    return completePermutations
}

fun getPhasePermutationsRecur(completePermutations: MutableSet<List<Int>>, inProgressPermutation: MutableSet<Int>, phases: MutableSet<Int>) {
    if (inProgressPermutation.size == 5)
    {
        completePermutations.add(inProgressPermutation.toList())
        return
    }

    for (phase in phases)
    {
        val updatedInProgressPermutation = inProgressPermutation.toMutableSet()
        updatedInProgressPermutation.add(phase)
        
        val availablePhases = phases.toMutableSet()
        availablePhases.remove(phase)

        getPhasePermutationsRecur(completePermutations, updatedInProgressPermutation, availablePhases)
    }
}

private fun runProgramUntilHalt(inputProgram: List<Int>, input: List<Int>): ProgramState {
    val program = inputProgram.toMutableList()

    var programState = ProgramState(program, 0, input, 0, mutableListOf())
    while (programState.program[programState.programIndex] != 99)
        programState = processOpCode(programState)

    return programState
}

private fun runProgram2UntilOutputOrHalt(id: String, inputProgram: List<Int>, input: List<Int>): ProgramState {
    println("$id input: $input")
    var programState = ProgramState(inputProgram.toMutableList(), 0, input, 0, mutableListOf())
    while (programState.program[programState.programIndex] != 99)
    {
        val prevOutputSize = programState.output.size
        programState = processOpCode(programState)
        val outputSize = programState.output.size

        if (prevOutputSize != outputSize)
            break
    }

    println("$id output: ${programState.output}")
    return programState
}

private fun runProgram2UntilOutputOrHalt(id: String, inputProgramState: ProgramState, input: List<Int>): ProgramState {
    println("$id input: $input")
    var programState = ProgramState(inputProgramState.program, inputProgramState.programIndex, input, 0, inputProgramState.output)
    while (true)
    {
        if (programState.program[programState.programIndex] == 99)
        {
            programState.halted = true
            break
        }

        val prevOutputSize = programState.output.size
        programState = processOpCode(programState)
        val outputSize = programState.output.size

        if (prevOutputSize != outputSize)
            break
    }

    println("$id output: ${programState.output}")
    return programState
}

data class ProgramState(val program: MutableList<Int>, var programIndex: Int, val input: List<Int>, var inputIndex: Int, val output: MutableList<Int>, var halted: Boolean = false)

private fun processOpCode(programState: ProgramState): ProgramState {
    val program = programState.program
    val index = programState.programIndex

    val instruction = program[index].toString().reversed()
    val opCode = Integer.parseInt(instruction.substring(0, 1))
    val param1Mode = if (instruction.length > 2) Integer.parseInt(instruction.substring(2, 3)) else 0
    val param2Mode = if (instruction.length > 3) Integer.parseInt(instruction.substring(3, 4)) else 0

    logOperation(program, index, opCode, param1Mode, param2Mode)
    
    val updatedProgramIndex = when (opCode) {
        1 -> add(program, index, param1Mode, param2Mode)
        2 -> mult(program, index, param1Mode, param2Mode)
        3 -> input(program, index, param1Mode, programState.input[programState.inputIndex])
        4 -> {
            val (updatedProgramIndex, output) = output(program, index, param1Mode)
            programState.output.add(output)
            updatedProgramIndex
        }
        5 -> jumpIfTrue(program, index, param1Mode, param2Mode)
        6 -> jumpIfFalse(program, index, param1Mode, param2Mode)
        7 -> lessThan(program, index, param1Mode, param2Mode)
        8 -> equals(program, index, param1Mode, param2Mode)
        else -> 0
    }

    val updatedInputIndex = if (opCode == 3) programState.inputIndex + 1 else programState.inputIndex

    return ProgramState(program, updatedProgramIndex, programState.input, updatedInputIndex, programState.output)
}

private fun logOperation(data: MutableList<Int>, index: Int, opCode: Int, param1Mode: Int, param2Mode: Int) {
    var param1 = -1
    try { param1 = if (param1Mode == 0) data[data[index + 1]]
        else data[index + 1]
    } catch (e: Exception) {    }

    var param2 = -1
    try { param2 = if (param2Mode == 0) data[data[index + 2]]
        else data[index + 2]
    } catch (e: Exception) {    }

    var param3 = -1
    try { param3 = data[index + 3] } catch (e: Exception) {    }

    val formattedIndex = String.format("%4d", index)
    when (opCode) {
        1 -> println("    add@$formattedIndex: $param1, $param2, $param3")
        2 -> println("    mul@$formattedIndex: $param1, $param2, $param3")
        3 -> println("     in@$formattedIndex: $param1")
        4 -> println("    out@$formattedIndex: $param1")
        5 -> println("    jit@$formattedIndex: $param1, $param2")
        6 -> println("    jif@$formattedIndex: $param1, $param2")
        7 -> println("     lt@$formattedIndex: $param1, $param2, $param3")
        8 -> println("     eq@$formattedIndex: $param1, $param2, $param3")
        9 -> println("    rbo@$formattedIndex: $param1, $param2, $param3")
        else -> println("    UNKNOWN@$formattedIndex")
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

private fun output(data: MutableList<Int>, index: Int, param1Mode: Int): Pair<Int, Int> {
    val output = if (param1Mode == 0) data[data[index + 1]] else data[index + 1]

    return Pair(index + 2, output)
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