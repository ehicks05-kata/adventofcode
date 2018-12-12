package net.ehicks.advent.year2018

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.IntStream
import kotlin.streams.toList

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "12.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(input)
}

private data class Pot(var plant: Boolean = false, var nextPlant: Boolean = false)

private fun solveInput(input: List<String>) {
    val pots = mutableMapOf<Int, Pot>()
    input[0].split(": ")[1].mapIndexed { index, c ->
        pots[index] = Pot(c == '#')
    }

    val rules = getRules(input)

    var min = pots.keys.min()!!
    var max = pots.keys.max()!!

    var previousSum = 0
    var previousDifference = -1
    for (gen in 0 until 50_000_000_000) {

        // ensure 3 empty pots on each side
        while (pots[min]!!.plant || pots[min + 1]!!.plant || pots[min + 2]!!.plant)
            pots[--min] = Pot(false)
        while (pots[max]!!.plant || pots[max - 1]!!.plant || pots[max - 2]!!.plant)
            pots[++max] = Pot(false)

        for (i in min + 2..max - 2) {
            val subList = IntStream.range(i - 2, i + 3).mapToObj { pots[it]!!.plant }.toList().toMutableList()
            var ruleResult = rules[subList]
            if (ruleResult == null) ruleResult = false
            pots[i]!!.nextPlant = ruleResult
        }

        pots.values.forEach { it.plant = it.nextPlant }

        if (gen == 19L) {
            println("part 1: ${pots.filterValues { it.plant }.entries.sumBy { it.key }}")
        }

        val sum = pots.filterValues { it.plant }.entries.sumBy { it.key }
        val difference = sum - previousSum
        println("gen: $gen, sum: $sum, difference: $difference")

        if (difference == previousDifference) {
            val generationsToSimulate = 50_000_000_000 - gen
            println("part 2: ${previousSum + (generationsToSimulate * difference)}")
            break
        }

        previousSum = sum
        previousDifference = difference
    }
}

private fun getRules(input: List<String>): Map<List<Boolean>, Boolean> {
    return input.subList(2, input.size).associate { line ->
        val parts = line.split(" => ")
        Pair(parts[0].map { it == '#' }, parts[1] == "#")
    }
}