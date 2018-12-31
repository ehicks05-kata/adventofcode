package net.ehicks.advent.year2018

import edu.princeton.cs.algs4.StdDraw
import java.awt.Color
import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "17.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(input)
}

private fun solveInput(input: List<String>) {
    val map = initMap(input)
    val spring = Point2D(500, 0, false)
    val water = mutableListOf<Point2D>()

    draw(map, water)

    var loop = 0
    while (true) {
        water.sortWith(BottomSort())
        water.forEach { it.deferred = false }
//        water.forEach { it.blocked = false }
        draw(map, water)

        // move each unsettled water
        water.filter { !it.settled }.forEach { droplet ->

            when {
                isClearBelow(water, droplet, map) -> droplet.y += 1
                isClearLeft(map, droplet, water) -> droplet.x -= 1
                isClearRight(map, droplet, water) -> droplet.x += 1

                else -> {
                    // can any contiguous block of water, on the same row, move?
                    // if so, lets defer our turn, and revisit later
                    val neighbors = getNeighborsInRow(water, droplet)

                    val neighborCanMove = neighbors.any { canMove(water, it, map) }
                    if (neighborCanMove)
                        droplet.deferred = true
                    else
                        droplet.settled = true
                }
            }
        }

        // move deferred water
        water.filter { it.deferred }.forEach { droplet ->

            when {
                isClearBelow(water, droplet, map) -> droplet.y += 1
                isClearLeft(map, droplet, water) -> droplet.x -= 1
                isClearRight(map, droplet, water) -> droplet.x += 1
                else -> droplet.blocked = true
            }
        }

        // add new water from the spring
        water.add(Point2D(spring.x, spring.y + 1, false))
        loop++
    }

    println("part 1: ${water.size}")
//    println("part 2: ")
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

private fun canMove(water: List<Point2D>, droplet: Point2D, map: List<List<Boolean>>): Boolean {
    return isClearBelow(water, droplet, map)
            || isClearLeft(map, droplet, water)
            || isClearRight(map, droplet, water)
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
    StdDraw.setCanvasSize(1100, 1100)
    StdDraw.setXscale(lowestX.toDouble(), map[0].size.toDouble())
//    StdDraw.setYscale(0.0, map.size.toDouble())
    StdDraw.setYscale(map.size.toDouble() * .95, map.size.toDouble())
    StdDraw.setPenRadius(.0002)
    StdDraw.enableDoubleBuffering()
}

private fun draw(map: List<List<Boolean>>, water: List<Point2D>) {
    StdDraw.clear()
    drawMap(map)
    drawWater(map, water)
    StdDraw.show()
    Thread.sleep(5)
}

private fun drawMap(map: List<List<Boolean>>) {
    for (row in 0 until map.size / 20) // todo remove optimization "/ 20"
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
            water.blocked -> StdDraw.BOOK_BLUE
            else -> StdDraw.BOOK_LIGHT_BLUE
        }
        StdDraw.setPenColor(color)
        StdDraw.filledRectangle(x + .5, y - .5, .5, .5)
        StdDraw.setPenColor()
    }
}

class BottomSort : Comparator<Point2D> {
    override fun compare(o1: Point2D?, o2: Point2D?): Int {
        if (o1 == null && o2 == null) return 0
        if (o1 == null) return -1
        if (o2 == null) return 1

        return if (o1.y == o2.y)
            o1.x.compareTo(o2.x)
        else
            o1.y.compareTo(o2.y) * -1
    }
}

data class Point2D(var x: Int, var y: Int,
                   var settled: Boolean = false,
                   var deferred: Boolean = false,
                   var blocked: Boolean = false)