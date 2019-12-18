package net.ehicks.advent.year2019

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.streams.toList

fun main() {
    val ic = IC(Files.readString(Paths.get("src/main/resources/year2019/09.txt")), true)
    ic.feedInput(1L)
    ic.run()
    println(ic.getOutput())
}

class IC(program: String,
         private val log: Boolean = false,
         private val id: String = "Unnamed IC",
         var machineState: MachineState = MachineState()) {

    init {
        val mem = programStringToMemoryMap(program)
        machineState = MachineState(mem)
    }

    private fun programStringToMemoryMap(input: String): MutableMap<Long, Long> {
        return input.split(",").stream().mapToLong { it.toLong() }.toList()
                .mapIndexed { index, i -> index.toLong() to i }.toMap().toMutableMap()
    }

    fun run(): MachineState {
        if (this.machineState.waitingForInput && this.machineState.input.isNotEmpty())
            this.machineState.waitingForInput = false

        while (!this.machineState.halted && !this.machineState.waitingForInput)
            this.machineState = processOpCode(this.machineState)

        return this.machineState
    }

    fun feedInput(input: Long) {
        this.machineState.input.add(input)
    }

    fun getOutput(): List<Long> {
        return this.machineState.output.toList()
    }

    fun getAndClearOutput(): List<Long> {
        val output = this.machineState.output.toList()
        this.machineState.output.clear()
        return output
    }

    data class MachineState(val mem: MutableMap<Long, Long> = mutableMapOf(), val input: Queue<Long> = ArrayDeque(),
                            var programIndex: Long = 0, val output: MutableList<Long> = mutableListOf(),
                            var halted: Boolean = false, var relativeBase: Long = 0, var waitingForInput: Boolean = false)

    private fun processOpCode(machineState: MachineState): MachineState {
        val memory = machineState.mem
        val index = machineState.programIndex

        val instruction = memory[index].toString().reversed()
        val opCode = getOpCode(instruction)

        if (opCode == 99) machineState.halted = true
        if (opCode == 3 && machineState.input.isEmpty()) machineState.waitingForInput = true
        if (machineState.halted || machineState.waitingForInput) return machineState

        val (param1, param2, param3) = getParams(memory, index, machineState.relativeBase, instruction, opCode)

        if (log)
            logOperation(index, opCode, param1, param2, param3, machineState)

        val updatedProgramIndex = when (opCode) {
            1 -> add(memory, index, param1, param2, param3)
            2 -> mult(memory, index, param1, param2, param3)
            3 -> input(memory, index, param1, machineState.input.remove())
            4 -> output(index, param1, machineState.output)
            5 -> jumpIfTrue(index, param1, param2)
            6 -> jumpIfFalse(index, param1, param2)
            7 -> lessThan(memory, index, param1, param2, param3)
            8 -> equals(memory, index, param1, param2, param3)
            9 -> relativeBaseOffset(index, param1, machineState)
            else -> throw Exception("Invalid opcode: $opCode")
        }

        return MachineState(memory, machineState.input, updatedProgramIndex, machineState.output, machineState.halted, machineState.relativeBase)
    }

    private fun logOperation(index: Long, opCode: Int, param1: Long, param2: Long, param3: Long, machineState: MachineState) {
        val formattedIndex = String.format("%4d", index)
        when (opCode) {
            1 -> println("$id add@$formattedIndex: $param1, $param2, $param3")
            2 -> println("$id mul@$formattedIndex: $param1, $param2, $param3")
            3 -> println("$id  in@$formattedIndex: $param1")
            4 -> println("$id out@$formattedIndex: $param1")
            5 -> println("$id jit@$formattedIndex: $param1, $param2")
            6 -> println("$id jif@$formattedIndex: $param1, $param2")
            7 -> println("$id  lt@$formattedIndex: $param1, $param2, $param3")
            8 -> println("$id  eq@$formattedIndex: $param1, $param2, $param3")
            9 -> println("$id rbo@$formattedIndex: ${machineState.relativeBase} + $param1 -> ${machineState.relativeBase + param1}")
            else -> println("$id UNKNOWN@$formattedIndex: $param1, $param2, $param3")
        }
    }

    private fun getOpCode(instruction: String): Int {
        return if (instruction.length == 1)
            Integer.parseInt(instruction.substring(0, 1))
        else
            Integer.parseInt(instruction.substring(0, 2).reversed())
    }

    private fun getParamModes(instruction: String): Triple<Int, Int, Int> {
        val param1Mode = if (instruction.length > 2) Integer.parseInt(instruction.substring(2, 3)) else 0
        val param2Mode = if (instruction.length > 3) Integer.parseInt(instruction.substring(3, 4)) else 0
        val param3Mode = if (instruction.length > 4) Integer.parseInt(instruction.substring(4, 5)) else 0
        return Triple(param1Mode, param2Mode, param3Mode)
    }

    private fun getParams(memory: MutableMap<Long, Long>, index: Long, relativeBase: Long, instruction: String, opCode: Int): Triple<Long, Long, Long> {
        val isWrite = opCode == 3

        val paramModes = getParamModes(instruction)
        val param1 = getParam(memory, index, relativeBase, isWrite, 1L, paramModes.first)
        val param2 = getParam(memory, index, relativeBase, isWrite, 2L, paramModes.second)
        val param3 = getParam(memory, index, relativeBase, true, 3L, paramModes.third)
        return Triple(param1, param2, param3)
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
}