package net.ehicks.advent.year2018

import edu.princeton.cs.algs4.StdDraw
import java.awt.Color
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.random.Random

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "17.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(input)
}

private fun solveInput(input: List<String>) {
    val map = initMap(input)
    val spring = Point2D(500, 0, false)
    val water = Water()
    val springs = mutableSetOf<Point2D>()

    val recentRuns = ArrayDeque<Long>()
    for (loop in 0..Int.MAX_VALUE) {
        val start = System.currentTimeMillis()
        water.getAllActive().forEach { it.moved = false }

        draw(map, water.getAll(), springs)

        for (row in water.getAllActive().groupBy { it.y }.keys.sortedDescending()) {

            // identify settled water
            water.activeWaters.values.filter { it.y == row }.forEach { droplet ->
                val neighbors = getNeighborsInRow(water, droplet).toMutableList()
                neighbors.add(droplet)
                neighbors.sortBy { it.x }

                val leftMostNeighbor = neighbors[0]
                val rightMostNeighbor = neighbors[neighbors.size - 1]
                val clayLeft = map[leftMostNeighbor.y][leftMostNeighbor.x - 1]
                val clayRight = map[rightMostNeighbor.y][rightMostNeighbor.x + 1]
                if (clayLeft && clayRight) {
                    neighbors.forEach {
                        water.settle(it)
                    }
                }
            }

            var moved = 1
            while (moved > 0) {
                moved = 0

                water.getAllActive().filter { !it.moved && it.y == row }.sortedBy { it.x }.forEach { droplet ->
                    val waterAbove = water.get(droplet.x, droplet.y - 1)
                    val clearBelow = isClearBelow(map, water, droplet)
                    val clearLeft = isClearLeft(map, water, droplet)
                    val clearRight = isClearRight(map, water, droplet)
                    when {
                        clearBelow -> {
                            water.move(droplet, 0, 1); moved++
                        }
                        waterAbove == null && clearLeft && clearRight -> {
                        }
                        clearLeft && clearRight -> {
                            droplet.direction = if (Random.nextBoolean()) "left" else "right"
                            water.move(droplet, if (droplet.direction == "left") -1 else 1, 0)
                        }
                        clearLeft -> {
                            water.move(droplet, -1, 0); moved++
                        }
                        clearRight -> {
                            water.move(droplet, 1, 0); moved++
                        }
                    }

                    if ((clearLeft || clearRight) && waterAbove != null) water.move(waterAbove, 0, 1)
                }
            }

            val waterAtBottomOfMap = water.getAllActive().filter { it.y >= map.size }
            waterAtBottomOfMap.forEach { water.remove(it) }
        }

        // find new springs
        water.getAllActive().forEach { droplet ->
            val clearAbove = isClearAbove(map, water, droplet)
            val clearBelow = isClearBelow(map, water, droplet)
            val clearLeft = isClearLeft(map, water, droplet)
            val clearRight = isClearRight(map, water, droplet)
            val waterAbove = water.get(droplet.x, droplet.y - 1)
            val waterLeft = water.get(droplet.x - 1, droplet.y) != null
            val waterRight = water.get(droplet.x + 1, droplet.y) != null
            val clayBelowLeft = map[droplet.y + 1][droplet.x - 1]
            val clayBelowRight = map[droplet.y + 1][droplet.x + 1]

            var clayAboveEventually = false
            for (y in droplet.y - 1 downTo 0) {
                if (map[y][droplet.x] || y == 0) {
                    clayAboveEventually = true
                    break
                }
                if (water.get(droplet.x, y) != null)
                    break
                if (springs.firstOrNull { it.x == droplet.x && it.y == y } != null)
                    break
            }

            if (clayAboveEventually && clearBelow && (clearLeft || clearRight) && (clayBelowLeft || clayBelowRight))
                springs.add(Point2D(droplet.x, droplet.y))
        }

        val newWater = Point2D(spring.x, spring.y + 1, false)
        water.add(newWater)

        val duration = System.currentTimeMillis() - start
        recentRuns.add(duration)
        if (recentRuns.size > 100)
            recentRuns.pop()
        if (loop % 100 == 0)
            println("$loop: ${duration}ms (last 100: ${recentRuns.average()}ms), water: ${water.getAllActive().size} / ${water.getAll().size}")
    }

    println("part 1: ${water.getAll().size}")
}

private fun getNeighborsInRow(water: Water, droplet: Point2D): List<Point2D> {
    val neighbors = mutableListOf<Point2D>()

    for (distanceLeft in 1..Int.MAX_VALUE) {
        val neighbor = water.get(droplet.x - distanceLeft, droplet.y)
        if (neighbor != null) neighbors.add(neighbor)
        else break
    }
    for (distanceRight in 1..Int.MAX_VALUE) {
        val neighbor = water.get(droplet.x + distanceRight, droplet.y)
        if (neighbor != null) neighbors.add(neighbor)
        else break
    }
    return neighbors
}

private fun isClearRight(map: List<List<Boolean>>, water: Water, droplet: Point2D): Boolean {
    val clayRight = map[droplet.y][droplet.x + 1]
    val waterRight = water.get(droplet.x + 1, droplet.y) != null
    return !clayRight && !waterRight
}

private fun isClearLeft(map: List<List<Boolean>>, water: Water, droplet: Point2D): Boolean {
    val clayLeft = map[droplet.y][droplet.x - 1]
    val waterLeft = water.get(droplet.x - 1, droplet.y) != null
    return !clayLeft && !waterLeft
}

private fun isClearAbove(map: List<List<Boolean>>, water: Water, droplet: Point2D): Boolean {
    val clayAbove = map[droplet.y - 1][droplet.x]
    val waterAbove = water.get(droplet.x, droplet.y - 1) != null
    return !clayAbove && !waterAbove
}

private fun isClearBelow(map: List<List<Boolean>>, water: Water, droplet: Point2D): Boolean {
    if (droplet.y == map.size - 1)
        return true

    val clayBelow = map[droplet.y + 1][droplet.x]
    val waterBelow = water.get(droplet.x, droplet.y + 1) != null
    return !clayBelow && !waterBelow
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
    StdDraw.setXscale(lowestX.toDouble() - 1, map[0].size.toDouble())
//    StdDraw.setYscale(0.0, map.size.toDouble())
    StdDraw.setYscale(map.size.toDouble() * .90, map.size.toDouble())
    StdDraw.setPenRadius(.0002)
    StdDraw.enableDoubleBuffering()
}

private fun draw(map: List<List<Boolean>>, water: Collection<Point2D>, springs: Set<Point2D>) {
    StdDraw.clear()
    drawMap(map)
    drawWater(map, water)
    drawSpring(map, Point2D(500, 0))
    drawSprings(map, springs)
    StdDraw.show()
    Thread.sleep(1)
}

private fun drawSpring(map: List<List<Boolean>>, spring: Point2D) {
    StdDraw.setPenColor(Color.RED)
    StdDraw.filledRectangle(spring.x + .5, map.size - spring.y - .5, .5, .5)
    StdDraw.setPenColor()
}

private fun drawSprings(map: List<List<Boolean>>, springs: Set<Point2D>) {
    springs.forEach { drawSpring(map, it) }
}

private fun drawMap(map: List<List<Boolean>>) {
//    val denom = 1 // todo remove optimization
    val denom = 10 // todo remove optimization
    for (row in 0 until map.size / denom)
        for (col in 0 until map[row].size) {
            if (map[row][col]) {
                val x = col.toDouble()
                val y = (map.size - row).toDouble()
                StdDraw.filledRectangle(x + .5, y - .5, .5, .5)
            }
        }
    StdDraw.text(510.0, map.size.toDouble() - 2, "test")
}

private fun drawWater(map: List<List<Boolean>>, waters: Collection<Point2D>) {
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

data class Water(val waters: MutableMap<Pair<Int, Int>, Point2D> = mutableMapOf(),
                 val activeWaters: MutableMap<Pair<Int, Int>, Point2D> = mutableMapOf()) {
    fun get(x: Int, y: Int): Point2D? {
        return waters[Pair(x, y)]
    }

    fun getActive(x: Int, y: Int): Point2D? {
        return activeWaters[Pair(x, y)]
    }

    fun getAll(): List<Point2D> {
        return waters.values.toList()
    }

    fun getAllActive(): List<Point2D> {
        return activeWaters.values.toList()
    }

    fun add(water: Point2D) {
        waters[Pair(water.x, water.y)] = water
        activeWaters[Pair(water.x, water.y)] = water
    }

    fun move(water: Point2D, deltaX: Int, deltaY: Int) {
        remove(water)
        water.x += deltaX
        water.y += deltaY
        add(water)

        water.moved = true
        if (deltaX > 0) water.direction = "right"
        if (deltaX < 0) water.direction = "left"
    }

    fun remove(water: Point2D) {
        waters.remove(Pair(water.x, water.y))
        activeWaters.remove(Pair(water.x, water.y))
    }

    fun removeFromActive(water: Point2D) {
        activeWaters.remove(Pair(water.x, water.y))
    }

    fun settle(water: Point2D) {
        water.settled = true
        removeFromActive(water)
    }

    fun markSettledWater() {

    }
}

data class Point2D(var x: Int, var y: Int,
                   var settled: Boolean = false,
                   var moved: Boolean = false,
                   var direction: String = "left",
                   var width: Int = 1)