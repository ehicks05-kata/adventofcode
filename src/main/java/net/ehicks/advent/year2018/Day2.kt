package net.ehicks.advent.year2018

import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "02.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(input)
}

private fun solveInput(input: List<String>) {
    var idsWith2 = 0
    var idsWith3 = 0

    input.forEach { id ->
        val letterCounts = id.split("")
                .filter { it.isNotEmpty() }
                .groupingBy { letter -> letter }
                .eachCount()
                .filterValues { i -> i == 2 || i == 3 }

        if (letterCounts.containsValue(2))
            idsWith2++
        if (letterCounts.containsValue(3))
            idsWith3++
    }

    println("part 1: " + idsWith2 * idsWith3)

    input.forEach { id ->
        val letters = id.split("")
                .filter { it.isNotEmpty() }

        input.forEach { innerId ->
            if (innerId != id)
            {
                val innerLetters = innerId.split("")
                        .filter { it.isNotEmpty() }

                var difference = ""
                for (i in 0 until letters.size)
                {
                    if (letters[i] != innerLetters[i])
                        difference += innerLetters[i]
                }

                if (difference.length == 1)
                {
                    println("part 2: ")
                    println(id)
                    println(innerId)
                    println(innerId.replaceFirst(difference, ""))
                    return
                }
            }
        }
    }
}
