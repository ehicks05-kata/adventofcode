// note to self: part 2 required outside inspiration
package net.ehicks.advent.year2018

import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "09.txt")
    val input: String = Files.readString(path)

    solveInput(input)
}

private fun solveInput(input: String) {
    val parts = input.split(" ")
    val players = MutableList(parts[0].toInt()) { 0L }
    val marbles = parts[6].toInt() * 100

    val circle = CircleList(0)
    for (marble in 1..marbles) {
        if (marble % 23 == 0) {
            circle.move(-7)
            players[marble % players.size] += marble.toLong() + circle.remove().toLong()
        } else {
            circle.move(1)
            circle.insert(marble)
        }

        if (marble == marbles / 100)
            println("part 1: ${players.max()}")
    }

    println("part 2: ${players.max()}")
}

private class CircleList<T>(data: T) {
    var current: CLNode<T> = CLNode(data)

    fun move(n: Int) {
        if (n < 0) repeat(-n) { current = current.left }
        else repeat(n) { current = current.right }
    }

    fun insert(data: T) {
        current = CLNode(data, current, current.right)
    }

    fun remove(): T {
        val data = current.data

        current.left.right = current.right
        current.right.left = current.left
        current = current.right

        return data
    }
}

private class CLNode<T> {
    val data: T
    var left: CLNode<T>
    var right: CLNode<T>

    constructor(data: T) {
        this.data = data
        this.left = this
        this.right = this
    }

    constructor(data: T, left: CLNode<T>, right: CLNode<T>) {
        this.data = data
        this.left = left
        left.right = this
        this.right = right
        right.left = this
    }
}