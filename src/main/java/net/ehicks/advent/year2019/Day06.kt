package net.ehicks.advent.year2019

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

const val CENTER_OF_MASS = "COM"

fun main() {
    val input = Files.readAllLines(Paths.get("src/main/resources/year2019/06.txt"))
            .stream().map {
                val parts = it.split(")")
                Pair(parts[0], parts[1])
            }.toList()

    val bodies = processOrbits(input)
    println("Part 1: " + solvePart1(bodies))
    println("Part 2: " + solvePart2(bodies))
}

private fun solvePart1(bodies: Map<String, Body>): Int {
    return bodies.values.stream().mapToInt { bodyToOrbitChain(bodies, it, CENTER_OF_MASS).size }.sum()
}

private fun solvePart2(bodies: Map<String, Body>): Int {
    return orbitalTransfers(bodies, "YOU", "SAN")
}

private fun processOrbits(input: List<Pair<String, String>>): Map<String, Body> {
    val bodies = mutableMapOf<String, Body>()

    input.forEach {
        val parent: Body = bodies.getOrDefault(it.first, Body(it.first, null, mutableSetOf()))
        val child = bodies.getOrDefault(it.second, Body(it.second, parent.id, mutableSetOf()))

        child.parent = parent.id
        parent.children.add(child.id)

        bodies[parent.id] = parent
        bodies[child.id] = child
    }

    return bodies
}

data class Body(val id: String, var parent: String?, val children: MutableSet<String>, var visited: Boolean = false)

private fun orbitalTransfers(bodies: Map<String, Body>, source: String, destination: String): Int {
    val sourceChain = bodyToOrbitChain(bodies, bodies[source]!!, CENTER_OF_MASS).toMutableList()
    val destinationChain = bodyToOrbitChain(bodies, bodies[destination]!!, CENTER_OF_MASS)

    var branchingPoint = ""
    for (i in sourceChain.indices)
        if (destinationChain.contains(sourceChain[i]))
        {
            branchingPoint = sourceChain[i]
            break
        }

    val sourceToBranchingPointDistance = bodyToOrbitChain(bodies, bodies[source]!!, branchingPoint).size
    val destinationToBranchingPointDistance = bodyToOrbitChain(bodies, bodies[destination]!!, branchingPoint).size

    return (sourceToBranchingPointDistance - 1) + (destinationToBranchingPointDistance - 1)  // subtract 1 since we only need to get within the same orbit
}

private fun bodyToOrbitChain(bodies: Map<String, Body>, body: Body, endPoint: String): List<String> {
    if (body.id == endPoint)
        return listOf()

    val chain = mutableListOf(body.id)
    var parent = body.parent!!
    while (parent != endPoint) {
        chain.add(parent)
        parent = bodies[parent]!!.parent!!
    }

    return chain
}