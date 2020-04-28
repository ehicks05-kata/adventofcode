package net.ehicks.advent.year2019

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.ceil
import kotlin.math.roundToInt

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/year2019/14.txt"))
    val reactions = linesToReactions(input)
    println("Part 1: " + solvePart1(reactions))
    println("Part 2: " + solvePart2())
}

private fun solvePart1(reactions: List<Reaction>): Int {
    val requirements = mutableListOf(ChemAmount("FUEL", 1))
    val leftovers = mutableMapOf<String, Int>()

    var allRequirementsAreMadeWithOre = false
    while (!allRequirementsAreMadeWithOre) {
        // map chem amounts to their inputs until we're left with ore
        val updatedRequirements = requirements.flatMap { getInputsForChemAmount(reactions, leftovers, it, false) }.toMutableList()
        val consolidatedRequirements = consolidateChemAmounts(updatedRequirements)

        requirements.clear()
        requirements.addAll(consolidatedRequirements)

        allRequirementsAreMadeWithOre = requirements.all { isMadeWithOre(reactions, it) }
    }

    val oreRequirements = requirements.flatMap { getInputsForChemAmount(reactions, leftovers, it, true) }.toMutableList()

    // 321504 is too low
    // 368024 is too high
    // 387756 is too high
    return oreRequirements.map { it.amount }.sum()
}

private fun solvePart2() {

}

private fun consolidateChemAmounts(chemAmounts: List<ChemAmount>): List<ChemAmount> {
    return chemAmounts.groupBy { it.name }
            .map { ChemAmount(it.key, it.value.sumBy { chemAmount -> chemAmount.amount }, it.value[0].indent) }
}

private fun isMadeWithOre(reactions: List<Reaction>, chemAmount: ChemAmount): Boolean {
    val reaction = reactions.find { it.output.name == chemAmount.name } ?: return false

    return reaction.inputs.size == 1 && reaction.inputs[0].name == "ORE"
}

private fun getInputsForChemAmount(reactions: List<Reaction>, leftovers: MutableMap<String, Int>,
                                   chemAmount: ChemAmount, convertToOre: Boolean): List<ChemAmount> {
    val reaction = reactions.find { it.output.name == chemAmount.name } ?: return listOf(chemAmount)

    val previousLeftover = leftovers[chemAmount.name] ?: 0
    val leftoversToUse = if (previousLeftover > chemAmount.amount) chemAmount.amount else previousLeftover
    if (leftoversToUse > 0)
        leftovers[chemAmount.name] = leftovers[chemAmount.name]!! - leftoversToUse
    val amountToProduce = chemAmount.amount - leftoversToUse

    if (!convertToOre && reaction.inputs.size == 1 && reaction.inputs[0].name == "ORE")
        return listOf(ChemAmount(chemAmount.name, chemAmount.amount - leftoversToUse))

    println(" ".repeat(chemAmount.indent * 2) + "Need ${reaction.inputs} to make $chemAmount")

    if (amountToProduce != 0) {
        val reactionServings = ceil(amountToProduce.toDouble() / reaction.output.amount).roundToInt().coerceAtLeast(1)
        val leftover = reaction.output.amount * reactionServings % amountToProduce
        leftovers.merge(chemAmount.name, leftover) {v1, v2 -> v1 + v2}

        return reaction.inputs.map { ChemAmount(it.name, it.amount * reactionServings, chemAmount.indent + 1) }
    }

    return reaction.inputs.map { ChemAmount(it.name, 0, chemAmount.indent + 1) }
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

data class ChemAmount(val name: String, val amount: Int, val indent: Int = 0) {
    override fun toString(): String {
        return "$amount $name"
    }
}
data class Reaction(val inputs: List<ChemAmount>, val output: ChemAmount)