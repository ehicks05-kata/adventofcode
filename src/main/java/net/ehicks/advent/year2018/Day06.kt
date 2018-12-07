package net.ehicks.advent.year2018

import edu.princeton.cs.algs4.StdDraw
import java.awt.Color
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.abs

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "06.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(input)
}

private fun solveInput(input: List<String>) {
    val points = mutableListOf<Point>()
    input.forEachIndexed { index, s ->
        val parts = s.split(", ")
        points.add(Point(parts[0].toInt(), parts[1].toInt(), index + 1))
    }

    val xSize = 1 + (points.maxBy { it.x }?.x ?: 0)
    val ySize = 1 + (points.maxBy { it.y }?.y ?: 0)

    initDrawing(xSize, ySize)

    val grid = MutableList(xSize) { MutableList(ySize) { 0 } }
    points.forEach {
        grid[it.x][it.y] = it.code
    }

    while (true) {
        val changeSet = mutableSetOf<Point>()
        for (x in 0 until xSize)
            for (y in 0 until ySize) {
                if (grid[x][y] != 0)
                    continue

                val adjacentCodes = mutableSetOf<Int>()
                if (x > 0) adjacentCodes.add(grid[x - 1][y])
                if (y > 0) adjacentCodes.add(grid[x][y - 1])
                if (x < xSize - 1) adjacentCodes.add(grid[x + 1][y])
                if (y < ySize - 1) adjacentCodes.add(grid[x][y + 1])
                adjacentCodes.removeIf { it == 0 }

                when {
                    adjacentCodes.size > 1 -> changeSet.add(Point(x, y, -1))
                    adjacentCodes.size == 1 -> changeSet.add(Point(x, y, adjacentCodes.first()))
                }
            }

        if (changeSet.isEmpty()) break
        changeSet.forEach { grid[it.x][it.y] = it.code }

        drawGrid(grid)
    }

    val regionSizes = getRegionSizes(grid)

    removeInfiniteRegions(regionSizes, grid)

    drawLabels(points)

    println("part 1: ${regionSizes.maxBy { it.value }}")

    var regionsUnder10k = 0
    for (x in 0 until xSize)
        for (y in 0 until ySize) {
            val totalDistance = points.fold(0) { acc, point -> acc + abs(point.x - x) + abs(point.y - y) }
            if (totalDistance < 10000)
                regionsUnder10k++
        }

    println("part 2: $regionsUnder10k")
}

private fun getRegionSizes(grid: MutableList<MutableList<Int>>): MutableMap<Int, Int> {
    val regionSizes = mutableMapOf<Int, Int>()
    for (x in 0 until grid.size)
        for (y in 0 until grid[0].size) {
            val total = regionSizes.getOrDefault(grid[x][y], 0)
            regionSizes[grid[x][y]] = total + 1
        }
    return regionSizes
}

private fun removeInfiniteRegions(totals: MutableMap<Int, Int>, grid: MutableList<MutableList<Int>>) {
    for (x in 0 until grid.size)
        for (y in 0 until grid[0].size) {
            if (x != 0 && x != grid.size - 1 && y != 0 && y != grid[0].size - 1)
                continue
            totals.remove(grid[x][y])
        }
}

private fun drawLabels(points: MutableList<Point>) {
    StdDraw.setPenColor()
    points.forEach {
        StdDraw.text(it.x.toDouble(), it.y.toDouble(), it.code.toString())
    }
    StdDraw.show()
}

private fun initDrawing(xSize: Int, ySize: Int) {
    StdDraw.setCanvasSize(xSize, ySize)
    StdDraw.setXscale(0.0, xSize.toDouble())
    StdDraw.setYscale(0.0, ySize.toDouble())
    StdDraw.enableDoubleBuffering()
    StdDraw.setPenRadius(0.0)
}

private fun drawGrid(grid: MutableList<MutableList<Int>>) {
    val baseColors = listOf<Color>(Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN,
            Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW)
    val colors = baseColors + baseColors.map { it.darker() } + baseColors.map { it.darker().darker() }

    for (x in 0 until grid.size)
        for (y in 0 until grid[0].size) {
            if (grid[x][y] == 0) continue

            val color = when {
                grid[x][y] == 0 -> Color.WHITE
                grid[x][y] == -1 -> Color.BLACK
                else -> colors[grid[x][y] % colors.size]
            }
            StdDraw.setPenColor(color)
            StdDraw.point(x.toDouble(), y.toDouble())
        }
    StdDraw.show()
}

data class Point(val x: Int, val y: Int, val code: Int)