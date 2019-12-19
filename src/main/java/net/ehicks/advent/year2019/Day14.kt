package net.ehicks.advent.year2019

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.ceil
import kotlin.math.roundToInt

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/year2019/14c.txt"))
    val reactions = linesToReactions(input)
    println("Part 1: " + solvePart1(reactions))
    println("Part 2: " + solvePart2())
}

private fun solvePart1(reactions: List<Reaction>): Int {
    val requirements = mutableListOf(ChemAmount("FUEL", 1))

    var allRequirementsAreMadeWithOre = false
    while (!allRequirementsAreMadeWithOre) {
        // map chem amounts to their inputs until we're left with ore
        val updatedRequirements = requirements.flatMap { getInputsForChemAmount(reactions, it, false) }.toMutableList()
        val consolidatedRequirements = consolidateChemAmounts(updatedRequirements)

        requirements.clear()
        requirements.addAll(consolidatedRequirements)

        allRequirementsAreMadeWithOre = requirements.all { isMadeWithOre(reactions, it) }
    }

    val oreRequirements = requirements.flatMap { getInputsForChemAmount(reactions, it, true) }.toMutableList()


    // 321504 is too low
    // 387756 is too high
    return oreRequirements.map { it.amount }.sum()
}

private fun solvePart2() {

}

private fun consolidateChemAmounts(chemAmounts: List<ChemAmount>): List<ChemAmount> {
    return chemAmounts.groupBy { it.name }
            .map { ChemAmount(it.key, it.value.sumBy { chemAmount -> chemAmount.amount }) }
}

private fun isMadeWithOre(reactions: List<Reaction>, chemAmount: ChemAmount): Boolean {
    val reaction = reactions.find { it.output.name == chemAmount.name } ?: return false

    return reaction.inputs.size == 1 && reaction.inputs[0].name == "ORE"
}

private fun getInputsForChemAmount(reactions: List<Reaction>, chemAmount: ChemAmount, convertToOre: Boolean): List<ChemAmount> {
    val reaction = reactions.find { it.output.name == chemAmount.name } ?: return listOf(chemAmount)

    if (!convertToOre)
        if (reaction.inputs.size == 1 && reaction.inputs[0].name == "ORE") return listOf(chemAmount)

    val reactionServings = ceil(chemAmount.amount.toDouble() / reaction.output.amount).roundToInt().coerceAtLeast(1)

    return reaction.inputs.map { ChemAmount(it.name, it.amount * reactionServings) }
}

private fun linesToReactions(lines: List<String>): List<Reaction> {
    return lines.map { line ->
        val parts = line.split(" => ")
        val (inString, outString) = parts
        val inputs = inString.split(", ").map { stringToChemAmount(it)}
        val output = stringToChemAmount(outString)

        Reaction(inputs, output)
    }
}

private fun stringToChemAmount(input: String): ChemAmount {
    return ChemAmount(input.split(" ").last(), input.split(" ").first().toInt())
}

data class ChemAmount(val name: String, val amount: Int)
data class Reaction(val inputs: List<ChemAmount>, val output: ChemAmount)