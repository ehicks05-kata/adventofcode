package net.ehicks.advent.year2018

import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "05.txt")
    val input: String = Files.readString(path)

    solveInput(input)
}

private fun solveInput(input: String) {
    println("original length: ${input.length}")

    println("part 1: ${fullyReact(input.split("").subList(1, input.length).toMutableList()).size}")

    var shortestResult = input.length
    var shortestResultChar = ""
    print("trying...")
    for (c in 'A'..'Z') {
        print(c)
        val chars = input.split("").subList(1, input.length).toMutableList()
        chars.removeAll { it.toUpperCase() == c.toString() }

        val result = fullyReact(chars)
        if (result.size < shortestResult) {
            shortestResult = result.size
            shortestResultChar = c.toString()
        }
    }

    println("\npart 2: ${shortestResult} (by removing ${shortestResultChar.toLowerCase()}/${shortestResultChar})")
}

private fun fullyReact(input: MutableList<String>): MutableList<String> {
    var c = 0
    var n = 1

    while (n < input.size) {
        val current = input[c]
        val next = input[n]
        val reactive = current != next && current.toUpperCase() == next.toUpperCase()
        if (reactive) {
            input.removeAt(c)
            input.removeAt(c) // char that was at 'n' is now at 'c' after previous removal
            c--
            n--

            if (c < 0) c = 0
            if (n < 1) n = 1
        } else {
            c++
            n++
        }
    }
    return input
}