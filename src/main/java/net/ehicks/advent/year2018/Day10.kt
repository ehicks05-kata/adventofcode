package net.ehicks.advent.year2018

import edu.princeton.cs.algs4.StdDraw
import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "10.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(input)
}

private fun solveInput(input: List<String>) {
    val pattern = """position=<\s*(-?\d+),\s*(-?\d+)> velocity=<\s*(-?\d+),\s*(-?\d+)>""".toRegex()
    var points = mutableListOf<MovingPoint>()
    input.forEach { line ->
        val (x, y, vx, vy) = requireNotNull(pattern.matchEntire(line))
                .destructured.toList().map { it.toInt() }

        points.add(MovingPoint(x, y, vx, vy))
    }

    var xMin = points.minBy { it.x }?.x ?: 0
    var xMax = points.maxBy { it.x }?.x ?: 0
    var yMin = points.minBy { it.y }?.y ?: 0
    var yMax = points.maxBy { it.y }?.y ?: 0

    initDrawing(xMin, xMax, yMin, yMax)

    var draw = false
    var i = 0
    while (true) {
        i++
        points = points.map {
            MovingPoint(it.x + it.vx, it.y + it.vy, it.vx, it.vy)
        }.toMutableList()

        xMin = points.minBy { it.x }?.x ?: 0
        xMax = points.maxBy { it.x }?.x ?: 0
        yMin = points.minBy { it.y }?.y ?: 0
        yMax = points.maxBy { it.y }?.y ?: 0
        StdDraw.setXscale(xMin.toDouble() - (xMin.toDouble() * .2), xMax.toDouble() + (xMax.toDouble() * .2))
        StdDraw.setYscale(yMin.toDouble() - (yMin.toDouble() * .2), yMax.toDouble() + (yMax.toDouble() * .2))

        val newSize = (xMax - xMin) + (yMax - yMin)

        if (draw)
        {
            drawPoints(points)
            println(i)
            Thread.sleep(1000)
        }

        if (newSize < 200)
            draw = true
    }

    println("part 1: (determined by visual inspection)")
    println("part 2: (determined by visual inspection)")
}

private fun initDrawing(xMin: Int, xMax: Int, yMin: Int, yMax: Int) {
    StdDraw.setCanvasSize(1000, 1000)
    StdDraw.setXscale(xMin.toDouble(), xMax.toDouble())
    StdDraw.setYscale(yMin.toDouble(), yMax.toDouble())
    StdDraw.enableDoubleBuffering()
    StdDraw.setPenRadius(.02)
}

private fun drawPoints(points: MutableList<MovingPoint>) {
    StdDraw.clear()

    points.forEach {
        StdDraw.point(it.x.toDouble(), it.y.toDouble())
    }

    StdDraw.show()
}

private data class MovingPoint(val x: Int, val y: Int, val vx: Int, val vy: Int)