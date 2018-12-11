package net.ehicks.advent.year2017

import edu.princeton.cs.algs4.StdDraw
import java.awt.Color
import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2017", "14.txt")
    val input: String = Files.readString(path)

//    solveInput("flqrgnkx")
    solveInput(input)
}

private fun solveInput(input: String) {
    val grid = IntRange(0, 127).map { row ->
        val hashInput = "$input-$row"
        val knotHash = knotHash(hashInput)

        knotHash.fold(listOf<Int>()) { acc, c ->
            var binaryString = Integer.parseInt(c.toString(), 16).toString(2)
            for (x in 0 until 4 - binaryString.length)
                binaryString = "0$binaryString"

            val bits = binaryString.map { it.toString().toInt() }

            acc + bits
        }
    }

    StdDraw.enableDoubleBuffering()
    StdDraw.setCanvasSize(1000,1000)
    StdDraw.setScale(0.0, 128.0)
    StdDraw.setPenColor(Color.BLUE)

    for (row in 0 until grid.size)
    {
        for (col in 0 until grid[row].size)
        {
            if (grid[row][col] == 1)
                StdDraw.filledRectangle(row.toDouble() + .5, col.toDouble() + .5, .5, .5)
        }
    }
    StdDraw.show()

    val used = grid.sumBy { list -> list.sum() }

    println("part 1: $used")
    println("part 2: ")
}