package net.ehicks.advent.year2019

import java.util.stream.IntStream

fun main() {
    val input = "137683-596253"
    val fromTo = input.split("-").map { it.toInt() }
    val range = fromTo[0] .. fromTo[1]

    println("Part 1: " + solvePart1(range))
    println("Part 2: " + solvePart2(range))
}

private fun isValidPassword(input: Int, part2Filter: Boolean): Boolean {
    val testNum = input.toString()
    for (i in 0 .. testNum.length - 2)
        if (testNum[i] > testNum[i + 1])
            return false

    var hasAdjacentDuplicate = false
    for (i in 0 .. testNum.length - 2)
    {
        if (testNum[i] == testNum[i + 1])
        {
            hasAdjacentDuplicate = true
            break
        }
    }

    if (!hasAdjacentDuplicate)
        return false

    if (part2Filter)
    {
        var hasAdjacentDuplicateOfTwoDigits = false
        for (i in 0 .. testNum.length - 2)
        {
            if (i == 0)
            {
                if (testNum[i] == testNum[i + 1] && testNum[i] != testNum[i + 2])
                {
                    hasAdjacentDuplicateOfTwoDigits = true
                    break
                }
            }
            else if (i == testNum.length - 2)
            {
                if (testNum[i] == testNum[i + 1] && testNum[i] != testNum[i - 1])
                    hasAdjacentDuplicateOfTwoDigits = true
                break
            }
            else
            {
                if (testNum[i] == testNum[i + 1] && testNum[i] != testNum[i - 1] && testNum[i] != testNum[i + 2])
                {
                    hasAdjacentDuplicateOfTwoDigits = true
                    break
                }
            }
        }

        if (!hasAdjacentDuplicateOfTwoDigits)
            return false
    }

    return true
}

private fun solvePart1(input: IntRange): Int {
    return IntStream.rangeClosed(input.first, input.last)
            .filter { isValidPassword(it, false) }.count().toInt()
}

private fun solvePart2(input: IntRange): Int {
    return IntStream.rangeClosed(input.first, input.last)
            .filter { isValidPassword(it, true) }.count().toInt()
}