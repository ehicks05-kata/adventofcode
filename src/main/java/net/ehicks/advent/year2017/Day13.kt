package net.ehicks.advent.year2017

import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2017", "13.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(input)
}

private fun solveInput(input: List<String>) {
    val firewalls = parseInput(input)
    val maxDepth = firewalls.maxBy { it.depth }!!.depth

    println("part 1: ${getTripSeverity(maxDepth, firewalls, false)}")

    var i = 0
    while (true) {
        val (tripSeverity, gotCaught) = getTripSeverity(maxDepth, firewalls, true)
        println("$i: $tripSeverity, $gotCaught")

        if (!gotCaught)
            break

        doTick(firewalls)
        i++
    }

    println("part 2: $i")
}

private fun parseInput(input: List<String>): List<Firewall> {
    return input.map { row ->
        val parts = row.split(": ")
        Firewall(parts[0].toInt(), parts[1].toInt())
    }
}

private fun getTripSeverity(maxDepth: Int, firewalls: List<Firewall>, returnWhenCaught: Boolean): Result {
    val workFirewalls = firewalls.map { Firewall(it.depth, it.range, it.position, it.ascending) }

    var tripSeverity = 0
    var gotCaught = false
    for (depth in 0..maxDepth) {
        val stepResult = getSeverity(workFirewalls, depth)
        tripSeverity += stepResult.severity
        if (stepResult.gotCaught)
        {
            gotCaught = true
            if (returnWhenCaught)
                return Result(tripSeverity, gotCaught)
        }

        doTick(workFirewalls)
    }
    return Result(tripSeverity, gotCaught)
}

private data class Result(val severity: Int, val gotCaught: Boolean)
private fun getSeverity(firewalls: List<Firewall>, depth: Int): Result {
    val firewall = firewalls.firstOrNull { it.depth == depth }
    if (firewall != null) {
        if (firewall.position == 0)
            return Result(firewall.depth * firewall.range, true)
    }
    return Result(0, false)
}

private fun doTick(firewalls: List<Firewall>) {
    firewalls.forEach {
        val atTop = it.ascending && it.position == it.range - 1
        val atBottom = !it.ascending && it.position == 0
        if (atTop || atBottom)
            it.ascending = !it.ascending

        it.position = if (it.ascending) it.position + 1 else it.position - 1
    }
}

private data class Firewall(val depth: Int, val range: Int, var position: Int = 0, var ascending: Boolean = true)