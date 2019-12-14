package net.ehicks.advent.year2019

import edu.princeton.cs.algs4.StdDraw
import java.awt.Color
import java.awt.Point
import java.lang.Thread.sleep
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs
import kotlin.math.max
import kotlin.system.exitProcess

fun main() {
    val input = Files.readString(Paths.get("src/main/resources/year2019/13.txt"))
    solve(IC(input))
    exitProcess(0)
}

private fun solve(ic: IC) {
    StdDraw.enableDoubleBuffering()
    StdDraw.setCanvasSize()
    var scale = 16.0
    StdDraw.setScale(0.0, scale)

    val grid = mutableMapOf<Point, Int>()
    var step = 0
    var score = 0
    ic.machineState.mem[0] = 2 // add 2 quarters

    while (true) {
        if (ic.machineState.halted)
            break

        ic.run()
        val output = ic.getOutput()

        output.chunked(3).forEach { chunk ->
            val x = chunk[0].toInt()
            val y = chunk[1].toInt()
            val tileId = chunk[2].toInt()

            grid[Point(x, y)] = tileId

            if (x == -1 && y == 0)
                score = tileId

            if (max(abs(x), abs(y)) > scale) {
                scale = max(abs(x), abs(y)).toDouble()
                StdDraw.setScale(0.0, scale)
            }
        }

        val ball = grid.entries.find { it.value == 4 }!!.key.x
        val paddle = grid.entries.find { it.value == 3 }!!.key.x
        val move = when {
            paddle < ball -> 1
            paddle > ball -> -1
            else -> 0
        }
        ic.feedInput(move.toLong())

        if (step == 0)
            println("Part 1: " + grid.filter { it.value == 2 }.count())

        if (grid.filter { it.value == 2 }.count() == 0) {
            println("Part 2: $score")
            break
        }

        step++
        render(grid, scale, step, score)
        sleep(1)
    }
}

private fun render(grid: MutableMap<Point, Int>, scale: Double, step: Int, score: Int) {
    StdDraw.clear(Color.BLACK)
    val maxY = grid.map { it.key.y }.max()!! // used to flip the y axis
    grid.forEach {
        val y = it.key.y * -1 + maxY // flip the y coordinate and then push it back up to a positive value

        if (it.key.x == -1 && it.key.y == 0) {
            StdDraw.setPenColor(Color.RED)
            StdDraw.textLeft(scale * .03, scale * .95, "Step: $step")
            StdDraw.textLeft(scale * .03, scale * .90, "Score: $score")
        } else
            when (it.value) {
                0 -> {
                }
                1 -> {
                    StdDraw.setPenColor(Color.DARK_GRAY)
                    StdDraw.filledRectangle(it.key.x.toDouble(), y.toDouble(), .5, .5)
                }
                2 -> {
                    StdDraw.setPenColor(Color.LIGHT_GRAY)
                    StdDraw.filledRectangle(it.key.x.toDouble(), y.toDouble(), .5, .5)
                }
                3 -> {
                    StdDraw.setPenColor(Color.YELLOW)
                    StdDraw.filledRectangle(it.key.x.toDouble(), y.toDouble(), .5, .25)
                }
                4 -> {
                    StdDraw.setPenColor(Color.GREEN)
                    StdDraw.filledCircle(it.key.x.toDouble(), y.toDouble(), .4)
                }
            }
    }

    StdDraw.show()
}