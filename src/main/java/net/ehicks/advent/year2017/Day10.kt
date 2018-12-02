package net.ehicks.advent.year2017

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.IntStream
import kotlin.streams.toList

fun main() {

    val path: Path = Path.of("src", "main", "resources", "year2017", "10.txt")
    val input: String = Files.readString(path)

    solveInput("3,4,1,5", 5)
    solveInput(input, 256)
}

private fun solveInput(input: String, listSize: Int) {
    val list = IntStream.range(0, listSize).toList().toMutableList()
    var position = 0
    var skipSize = 0

    input.split(",")
            .map { it.replace("\n", "").toInt() }
            .forEach {
                var reverseFrom = position
                var reverseTo = position + it - 1

                while (reverseFrom < reverseTo) {
                    val from = reverseFrom % list.size
                    val to = reverseTo % list.size

                    val temp = list[from]
                    list[from] = list[to]
                    list[to] = temp

                    reverseFrom++
                    reverseTo--
                }

                position += (it + skipSize) % list.size
                skipSize++
            }

    println(list[0] * list[1])
}