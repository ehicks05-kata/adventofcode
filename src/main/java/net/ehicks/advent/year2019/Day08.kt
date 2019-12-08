package net.ehicks.advent.year2019

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readString(Paths.get("src/main/resources/year2019/08.txt")).toCharArray().toList()
    val dimensions = Pair(25, 6)

    val layers = input.chunked(dimensions.run { first * second })
    println("Part 1: " + solvePart1(layers))
    println("Part 2: ")
    solvePart2(layers, dimensions)
}

private fun solvePart1(layers: List<List<Char>>): Int {
    val layerWithFewestZeroes = layers.minBy { layer -> layer.filter { pixel -> pixel == '0' }.count() }!!
            .groupingBy { it }.eachCount()
    val ones = layerWithFewestZeroes['1']!!
    val twos = layerWithFewestZeroes['2']!!

    return ones * twos
}

private fun solvePart2(layers: List<List<Char>>, dimensions: Pair<Int, Int>) {
    val w = dimensions.first
    val h = dimensions.second

    val composite = MutableList(w * h) { '2' } // init to transparent

    for (layer in layers) {
        for (i in layer.indices) {
            if (composite[i] != '2')
                continue

            composite[i] = layer[i]
        }
    }

    composite
            .map { if (it == '1') "#" else " " }
            .chunked(w)
            .forEach { println(it.joinToString(" ")) }
}