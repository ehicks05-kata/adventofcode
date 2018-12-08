package net.ehicks.advent.year2018

import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "08.txt")
    val input: String = Files.readString(path)

    solveInput(input)
}

private fun solveInput(input: String) {
    val inputNumbers = input.replace("\n", "").split(" ").map { it.toInt() }

    val root = parseNode(inputNumbers)

    println("part 1: ${sumMetaData(root)}")
    println("part 2: ${getValue(root)}")
}

private fun sumMetaData(node: Node): Int {
    return node.metadata.sum() + node.childNodes.sumBy { sumMetaData(it) }
}

private fun parseNode(input: List<Int>, index: Int = 0): Node {
    val childCount = input[index + 0]
    val metadataSize = input[index + 1]
    val children: MutableList<Node> = mutableListOf()

    var childIndex = index + 2
    for (i in 0 until childCount) {
        val child = parseNode(input, childIndex)
        childIndex += child.length
        children.add(child)
    }

    val length = 2 + metadataSize + children.sumBy { it.length }
    val metadata: List<Int> = input.subList(index + length - metadataSize, index + length)

    return Node(children, metadata, length)
}

private fun getValue(node: Node): Int {
    return if (node.childNodes.isEmpty())
        node.metadata.sum()
    else {
        node.metadata.sumBy { nodeNumber ->
            if (nodeNumber == 0 || nodeNumber > node.childNodes.size)
                0
            else
                getValue(node.childNodes[nodeNumber - 1])
        }
    }
}

private data class Node(val childNodes: List<Node> = mutableListOf(), val metadata: List<Int> = mutableListOf(), val length: Int)