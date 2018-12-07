package net.ehicks.advent.year2018

import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "07.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(input)
}

private fun solveInput(input: List<String>) {
    var graph = buildGraph(input)

    val order = mutableListOf<String>()

    var head = getHeads(graph).firstOrNull()
    while (head != null) {
        order.add(head)
        graph.removeNode(head)
        head = getHeads(graph).firstOrNull()
    }

    println("part 1: ${order.joinToString(separator = "")}")

    graph = buildGraph(input)

    val workers = mutableListOf(Worker(1), Worker(2), Worker(3), Worker(4), Worker(5))
    var heads = getHeads(graph)
    var seconds = 0

    while (graph.nodes().isNotEmpty()) {
        workers.forEach {
            if (it.prevTimeRemaining != 0 && it.timeRemaining == 0) {
                it.prevTimeRemaining = 0
                graph.removeNode(it.job)
                it.job = ""
                heads = getHeads(graph)
                heads.removeAll(workers.map { it.job })
            }
        }

        while (getAvailableWorkers(workers) > 0 && heads.isNotEmpty()) {
            val worker = workers.first { it.timeRemaining == 0 }
            worker.job = heads.first()
            worker.prevTimeRemaining = worker.timeRemaining
            worker.timeRemaining = getSecondsForJob(heads.first())
            heads.removeAt(0)
        }

        val secondsToJump = workers.map { it.timeRemaining }.filter { it > 0 }.min() ?: 0
        seconds += secondsToJump
        workers.filter { it.timeRemaining > 0 }.forEach {
            it.prevTimeRemaining = it.timeRemaining
            it.timeRemaining -= secondsToJump
        }
    }

    println("part 2: $seconds")
}

private fun getAvailableWorkers(workers: List<Worker>): Int {
    return workers.filter { it.timeRemaining == 0 }.count()
}

private fun getSecondsForJob(job: String): Int {
    return 60 + (1 + job.single().toInt() - 'A'.toInt())
}

private fun buildGraph(input: List<String>): MutableGraph<String> {
    val graph = GraphBuilder.directed().build<String>()
    input.forEach { s ->
        val parts = s.split(" ")
        graph.putEdge(parts[1], parts[7])
    }
    return graph
}

private fun getHeads(graph: MutableGraph<String>): MutableList<String> {
    val heads = mutableListOf<String>()
    graph.nodes().forEach { if (graph.predecessors(it).size == 0) heads.add(it) }
    return heads.sorted().toMutableList()
}

private data class Worker(val id: Int, var job: String = "", var prevTimeRemaining: Int = 0, var timeRemaining: Int = 0)