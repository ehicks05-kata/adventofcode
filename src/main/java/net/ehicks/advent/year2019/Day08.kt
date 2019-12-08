package net.ehicks.advent.year2019

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

fun main() {
    val input = Files.readString(Paths.get("src/main/resources/year2019/08.txt"))
            .chars().mapToObj { it.toChar().toString().toInt() }.toList()

    val dimensions = Pair(25, 6)
    val layers = getLayers(input, dimensions)
    println("Part 1: " + solvePart1(layers))
    println("Part 2: ")
    solvePart2(layers, dimensions)
}

private fun solvePart1(layers: MutableList<MutableList<Int>>): Long {
    val layerWithFewestZeroes = layers.minBy { layer -> layer.filter { pixel -> pixel == 0 }.count() }!!
    val ones = layerWithFewestZeroes.stream().filter { it == 1 }.count()
    val twos = layerWithFewestZeroes.stream().filter { it == 2 }.count()

    return ones * twos
}

private fun solvePart2(layers: MutableList<MutableList<Int>>, dimensions: Pair<Int, Int>) {
    val w = dimensions.first
    val h = dimensions.second

    val composite = MutableList(w * h) { 2 }

    for (layer in layers) {
        for (row in 0 until h) {
            for (col in 0 until w) {
                val layerPixelIndex = row * w + col
                if (composite[layerPixelIndex] != 2)
                    continue

                composite[layerPixelIndex] = layer[layerPixelIndex]
            }
        }
    }

    composite
            .map { if (it == 1) "#" else " " }
            .chunked(w)
            .forEach { println(it.joinToString(" ")) }
}

private fun getLayers(input: List<Int>, dimensions: Pair<Int, Int>): MutableList<MutableList<Int>> {
    val layers = mutableListOf<MutableList<Int>>()
    val w = dimensions.first
    val h = dimensions.second
    val layerSize = w * h
    val layerCount = input.size / layerSize

    for (layerIndex in 0 until layerCount) {
        val layer = mutableListOf<Int>()
        for (row in 0 until h) {
            for (col in 0 until w) {
                val i = layerIndex * layerSize + row * w + col
                layer.add(input[i])
            }
        }
        layers.add(layer)
    }
    return layers
}