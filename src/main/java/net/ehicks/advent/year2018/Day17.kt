package net.ehicks.advent.year2018

import edu.princeton.cs.algs4.StdDraw
import java.awt.Color
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.random.Random

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "17.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(input)
}

private fun solveInput(input: List<String>) {
    val map = initMap(input)
    val spring = Point2D(500, 0, false)
    val water = mutableListOf<Point2D>()

//    Files.delete(Paths.get("17-temp.png"))
//    draw(map, water)
//    Thread.sleep(1000)
//    StdDraw.save("17-temp.png")

    for (loop in 0..Int.MAX_VALUE) {
        water.forEach { it.moved = false }

//        if (loop % 1000 == 0)
            draw(map, water)

        for (row in water.groupBy { it.y }.keys.sortedDescending()) {

            // first, find settled water
            water.filter { !it.settled && it.y == row }.forEach { droplet ->
                val neighbors = getNeighborsInRow(water, droplet).toMutableList()
                neighbors.add(droplet)
                neighbors.sortBy { it.x }

                val leftMostNeighbor = neighbors[0]
                val rightMostNeighbor = neighbors[neighbors.size - 1]
                val clayLeft = map[leftMostNeighbor.y][leftMostNeighbor.x - 1]
                val clayRight = map[rightMostNeighbor.y][rightMostNeighbor.x + 1]
                if (clayLeft && clayRight)
                    neighbors.forEach { it.settled = true }
            }

            var moved = 1
            while (moved > 0) {
                moved = 0

                water.filter { !it.moved && !it.settled && it.y == row }.sortedBy { it.x }.forEach { droplet ->
                    val waterAbove = water.firstOrNull { it.x == droplet.x && it.y == droplet.y - 1 }
                    val clearBelow = isClearBelow(water, droplet, map)
                    val clearLeft = isClearLeft(map, droplet, water)
                    val clearRight = isClearRight(map, droplet, water)
                    when {
                        clearBelow -> move(droplet, 0, 1)
                        clearLeft && clearRight  -> move(droplet, if (Random.nextBoolean()) 1 else -1, 0)
                        clearLeft  -> move(droplet, -1, 0)
                        clearRight -> move(droplet, 1, 0)
                    }

                    if (clearBelow || clearLeft || clearRight) moved++
                    if ((clearLeft || clearRight) && waterAbove != null) move(waterAbove, 0, 1)
                }
            }
        }

        water.add(Point2D(spring.x, spring.y + 1, false))
    }

    println("part 1: ${water.size}")
}

private fun getNeighborsInRow(water: List<Point2D>, droplet: Point2D): List<Point2D> {
    val neighbors = mutableListOf<Point2D>()

    for (distanceLeft in 1..Int.MAX_VALUE) {
        val neighbor = water.firstOrNull { it.x == droplet.x - distanceLeft && it.y == droplet.y }
        if (neighbor != null) neighbors.add(neighbor)
        else break
    }
    for (distanceRight in 1..Int.MAX_VALUE) {
        val neighbor = water.firstOrNull { it.x == droplet.x + distanceRight && it.y == droplet.y }
        if (neighbor != null) neighbors.add(neighbor)
        else break
    }
    return neighbors
}

private fun move(droplet: Point2D, x: Int, y: Int) {
    droplet.x += x; droplet.y += y; droplet.moved = true
}

private fun isClearRight(map: List<List<Boolean>>, droplet: Point2D, water: List<Point2D>): Boolean {
    val clayRight = map[droplet.y][droplet.x + 1]
    val waterRight = water.any { it.x == droplet.x + 1 && it.y == droplet.y }
    return !clayRight && !waterRight
}

private fun isClearLeft(map: List<List<Boolean>>, droplet: Point2D, water: List<Point2D>): Boolean {
    val clayLeft = map[droplet.y][droplet.x - 1]
    val waterLeft = water.any { it.x == droplet.x - 1 && it.y == droplet.y }
    return !clayLeft && !waterLeft
}

private fun isClearBelow(water: List<Point2D>, droplet: Point2D, map: List<List<Boolean>>): Boolean {
    val waterBelow = water.any { it.x == droplet.x && it.y == droplet.y + 1 }
    val clayBelow = map[droplet.y + 1][droplet.x]
    return !waterBelow && !clayBelow
}

private fun initMap(input: List<String>): MutableList<MutableList<Boolean>> {
    val clayPoints = mutableListOf<Point2D>()

    input.forEach { line ->
        val parts = line.split(", ")
        val keyParts = parts[0].split("=")
        val valParts = parts[1].split("=")

        val keyDomain = keyParts[0]
        val keyValue = keyParts[1].toInt()
        val valValue = valParts[1].split("..")
        val valFrom = valValue[0].toInt()
        val valTo = valValue[1].toInt()

        for (i in valFrom..valTo) {
            val x = if (keyDomain == "x") keyValue else i
            val y = if (keyDomain == "y") keyValue else i
            clayPoints.add(Point2D(x, y, false))
        }
    }

    val lowestX = clayPoints.minBy { it.x }?.x ?: 0
    val highestX = clayPoints.maxBy { it.x }?.x ?: 0
    val highestY = clayPoints.maxBy { it.y }?.y ?: 0

    val map = MutableList(highestY + 1) { MutableList(highestX + 1) { false } }

    clayPoints.forEach { pair ->
        map[pair.y][pair.x] = true
    }

    initDrawing(lowestX, map)
    return map
}

private fun initDrawing(lowestX: Int, map: List<List<Boolean>>) {
    StdDraw.setCanvasSize(900, 900)
    StdDraw.setXscale(lowestX.toDouble(), map[0].size.toDouble())
//    StdDraw.setYscale(0.0, map.size.toDouble())
    StdDraw.setYscale(map.size.toDouble() * .90, map.size.toDouble())
    StdDraw.setPenRadius(.0002)
    StdDraw.enableDoubleBuffering()
}

private fun draw(map: List<List<Boolean>>, water: List<Point2D>) {
    StdDraw.clear()
    if (Paths.get("17-temp.png").toFile().exists())
    {
        StdDraw.picture(map[0].size / 2.0, map.size / 2.0,
                "17-temp.png", map[0].size.toDouble(), map.size.toDouble())
//        StdDraw.picture(200.0, 200.0, "17-temp.png", 900.0, 900.0)
    }
    else
        drawMap(map)
    drawWater(map, water)
    StdDraw.show()
    Thread.sleep(5)
}

private fun drawMap(map: List<List<Boolean>>) {
    for (row in 0 until map.size / 10) // todo remove optimization
//    for (row in 0 until map.size)
        for (col in 0 until map[row].size) {
            val x = col.toDouble()
            val y = (map.size - row).toDouble()

            if (col == 500 && row == 0) {
                StdDraw.setPenColor(Color.RED)
                StdDraw.filledRectangle(x + .5, y - .5, .5, .5)
                continue
            } else
                StdDraw.setPenColor(Color.BLACK)

            if (map[row][col])
                StdDraw.filledRectangle(x + .5, y - .5, .5, .5)
            else
                StdDraw.rectangle(x + .5, y - .5, .5, .5)
        }
    StdDraw.setPenColor(Color.ORANGE)
    StdDraw.text(map[0].size / 2.0, 5.5, "test")
    StdDraw.setPenColor()
}

private fun drawWater(map: List<List<Boolean>>, waters: List<Point2D>) {
    for (water in waters) {
        val x = water.x.toDouble()
        val y = (map.size - water.y).toDouble()

        val color = when {
            water.settled -> Color.BLUE
            else -> StdDraw.BOOK_LIGHT_BLUE
        }
        StdDraw.setPenColor(color)
        StdDraw.filledRectangle(x + .5, y - .5, .5, .5)
        StdDraw.setPenColor()
    }
}

data class Point2D(var x: Int, var y: Int,
                   var settled: Boolean = false,
                   var moved: Boolean = false)