package net.ehicks.advent.year2017

import java.nio.file.Files
import java.nio.file.Path
import java.util.*

fun main() {
    val path: Path = Path.of("aoc-kotlin", "src", "main", "kotlin", "net", "ehicks", "advent", "year2017", "input.txt")
    val input: String = Files.readString(path)

    solveInput(input)
}

private fun solveInput(input: String) {
    val stack: Deque<String> = ArrayDeque<String>()
    var score = 0
    var inGarbage = false
    var ignoreNextChar = false
    var nonCancelledGarbage = 0

    input.split("").forEach {
        if (ignoreNextChar) {
            ignoreNextChar = false
            return@forEach
        }

        if (inGarbage) {
            when (it) {
                "!" -> ignoreNextChar = true
                ">" -> inGarbage = false
                else -> nonCancelledGarbage++
            }
        } else {
            when (it) {
                "<" -> inGarbage = true
                "{" -> stack.push(it)
                "}" -> {
                    score += stack.size
                    stack.pop()
                }
            }
        }
    }
    println("score: $score\nnon-cancelled garbage: $nonCancelledGarbage")
}
