package net.ehicks.advent.year2017

import com.google.common.graph.Graph
import com.google.common.graph.GraphBuilder
import com.google.common.graph.Graphs
import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2017", "12.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(input)
}

private fun solveInput(input: List<String>) {
    val graph = GraphBuilder.undirected().allowsSelfLoops(true).build<Int>()

    input.forEach { row ->
        val parts = row.split(" <-> ")
        val from = parts[0].toInt()
        val to = parts[1].split(", ").map { it.toInt() }

        to.forEach { graph.putEdge(from, it) }
    }

    println("part 1: ${listConnections(graph, mutableSetOf(), 0).size}")
    println("part 2: ${countGroups(graph)}")
}

private fun listConnections(graph: Graph<Int>, connections: MutableSet<Int>, node: Int): MutableSet<Int>
{
    connections.add(node)
    graph.successors(node)
            .filter { !connections.contains(it) }
            .forEach { connections.addAll(listConnections(graph, connections, it)) }
    return connections
}

private fun countGroups(g: Graph<Int>): Int
{
    val graph = Graphs.copyOf(g)
    var groups = 0
    while (graph.nodes().isNotEmpty())
    {
        groups++
        val node = graph.nodes().first()
        val connections = listConnections(graph, mutableSetOf(), node)
        connections.forEach { graph.removeNode(it) }
    }

    return groups
}