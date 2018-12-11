package net.ehicks.advent.year2017

import java.nio.file.Files
import java.nio.file.Path

fun main() {

    val path: Path = Path.of("src", "main", "resources", "year2017", "10.txt")
    val input: String = Files.readString(path).replace("\n", "")

    solveInput("1,2,4")
    solveInput(input)
}

private fun solveInput(input: String) {
    val hashState = HashState(MutableList(256) { index -> index }, 0, 0, 0)

    input.split(",")
            .map { it.toInt() }
            .forEach { inputLength ->
                hashState.inputLength = inputLength
                hashRound(hashState)
            }

    println("part 1: " + hashState.list[0] * hashState.list[1])

    val knotHash = knotHash(input)

    println("part 2: $knotHash")
}

private fun stringToAsciiList(input: String): List<Int> {
    return input.split("")
            .filter { it.isNotEmpty() }
            .map { it.single().toInt() }
            .plus(listOf(17, 31, 73, 47, 23))
}

public fun knotHash(input: String): String {
    val sequence = stringToAsciiList(input)
    val hashState = HashState(MutableList(256) { index -> index }, 0, 0, 0)

    for (i in 0 until 64) {
        sequence.forEach { inputLength ->
            hashState.inputLength = inputLength
            hashRound(hashState)
        }
    }

    val denseHash = mutableListOf<Int>()
    var temp = 0
    hashState.list.forEachIndexed { index, i ->
        temp = temp.xor(i)
        if ((index + 1) % 16 == 0) {
            denseHash.add(temp)
            temp = 0
        }
    }

    val knotHash = denseHash.joinToString(separator = "") {
        val hex = it.toString(16)
        if (hex.length == 1)
            "0$hex"
        else
            hex
    }
    return knotHash
}

private fun hashRound(hashState: HashState): HashState {
    hashState.apply {
        var reverseFrom = position
        var reverseTo = position + inputLength - 1

        while (reverseFrom < reverseTo) {
            val from = reverseFrom % list.size
            val to = reverseTo % list.size

            val temp = list[from]
            list[from] = list[to]
            list[to] = temp

            reverseFrom++
            reverseTo--
        }

        position += (inputLength + skipSize) % list.size
        skipSize++
        return hashState
    }
}

private data class HashState(var list: MutableList<Int>, var inputLength: Int, var position: Int, var skipSize: Int)