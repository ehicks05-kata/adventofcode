package net.ehicks.advent.year2018

import edu.princeton.cs.algs4.StdDraw
import java.awt.Color
import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "15.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(input)

    for (elfDamage in 4..Int.MAX_VALUE) {
        val deadElves = solveInput(input, elfDamage, part2 = true)
        if (deadElves == 0)
            break
    }
}

private fun solveInput(input: List<String>, elfDamage: Int = 3, part2: Boolean = false): Int {
    val map = mutableListOf<MutableList<Boolean>>()
    val critters = mutableListOf<Critter>()

    input.forEachIndexed { rowIndex, line ->
        val row = mutableListOf<Boolean>()
        line.forEachIndexed { colIndex, c ->
            row.add(c == '#')
            if (c == 'E' || c == 'G') {
                val attack = if (c == 'E') elfDamage else 3
                critters.add(Critter(c.toString(), colIndex, rowIndex, attack = attack))
            }
        }
        map.add(row)
    }

    var deadElves = 0
    initDrawing(map)
    draw(map, critters, null, 0, elfDamage)

    var round = 0
    roundLoop@
    for (i in 0..Int.MAX_VALUE) {
        critters.sort()

        val dead = critters.filter { it.hp <= 0 }
        dead.forEach {
            if (it.type == "E")
                deadElves++
            critters.remove(it)
        }

        val workCritters = critters.toMutableList()
        draw(map, critters, null, round, elfDamage)
        for (critter in critters) {
//            draw(map, critters, critter, round, elfDamage)

            if (critter.hp <= 0)
                continue

            // attempt to attack before doing anything else
            val attacked = attack(map, workCritters, critter)
            if (attacked)
                continue

            // identify targets
            val targets = workCritters.filter { it.hp > 0 && it.type != critter.type }
            if (targets.isEmpty()) {
                break@roundLoop
            }

            // identify open squares 'in range' of targets
            val openSquaresInRangeOfTargets = mutableListOf<Square>()
            targets.forEach { target ->
                openSquaresInRangeOfTargets.addAll(getOpenAdjacentSquares(map, workCritters, target.x, target.y))
            }

            if (openSquaresInRangeOfTargets.isEmpty())
                continue

            // determine the distance to each open square
            val squareToDistance = mutableMapOf<Square, Int>()
            openSquaresInRangeOfTargets.forEach {
                val distance = getDistanceIter(map, workCritters, Square(critter.x, critter.y), it)
                if (distance != -1)
                    squareToDistance[it] = distance
            }

            val minDistance = squareToDistance.minBy { it.value }?.value ?: -1
            val squaresAtMinDistance = squareToDistance.filter { it.value == minDistance }.keys

            if (squaresAtMinDistance.isEmpty())
                continue

            val squareAtMinDistance = squaresAtMinDistance.sorted().first()

            when (minDistance) {
                0 -> attack(map, workCritters, critter)
                1 -> {
                    move(map, workCritters, critter, squareAtMinDistance)
                    attack(map, workCritters, critter)
                }
                else -> move(map, workCritters, critter, squareAtMinDistance)
            }
        }

        round++
    }

    val dead = critters.filter { it.hp <= 0 }
    dead.forEach {
        if (it.type == "E")
            deadElves++
        critters.remove(it)
    }

    val hpLeft = critters.map { it.hp }.sum()

    if (!part2)
        println("part 1: round: $round, hp left: $hpLeft, answer: ${round * hpLeft}")

    if (part2)
        println("part 2: round: $round, hp left: $hpLeft, answer: ${round * hpLeft}, elf damage: $elfDamage, dead elves: $deadElves")

    return deadElves
}

private fun initDrawing(map: MutableList<MutableList<Boolean>>) {
    StdDraw.setCanvasSize(700, 700)
    StdDraw.setXscale(0.0, map[0].size.toDouble())
    StdDraw.setYscale(0.0, map.size.toDouble())
    StdDraw.enableDoubleBuffering()
}

private fun draw(map: MutableList<MutableList<Boolean>>, critters: MutableList<Critter>, critter: Critter? = null,
                 round: Int, elfDamage: Int) {
    StdDraw.clear()
    drawMap(map, round, elfDamage)
    drawCritters(map, critters)
    if (critter != null)
        drawCritter(map, critter, active = true)
    StdDraw.show()
    Thread.sleep(5)
}

private fun drawMap(map: List<List<Boolean>>, round: Int, elfDamage: Int) {
    for (row in 0 until map.size)
        for (cell in 0 until map[row].size) {
            val x = cell.toDouble()
            val y = (map.size - row).toDouble()

            if (map[row][cell])
                StdDraw.filledRectangle(x + .5, y - .5, .5, .5)
            else
                StdDraw.rectangle(x + .5, y - .5, .5, .5)
        }
    StdDraw.setPenColor(Color.ORANGE)
    StdDraw.text(map[0].size / 2.0, map.size - .5, "round: $round, elfDamage: $elfDamage")
    StdDraw.setPenColor()
}

private fun drawCritters(map: List<List<Boolean>>, critters: List<Critter>) {
    for (critter in critters)
        drawCritter(map, critter)
}

private fun drawCritter(map: List<List<Boolean>>, critter: Critter, active: Boolean = false) {
    val x = critter.x.toDouble()
    val y = (map.size - critter.y).toDouble()

    val color = when {
        active -> Color.BLUE
        critter.type == "E" -> Color.GREEN
        else -> Color.RED
    }

    StdDraw.setPenColor(color)
    StdDraw.filledRectangle(x + .5, y - .5, .5, .5)

    StdDraw.setPenColor()
    StdDraw.text(x + .5, y - .5, critter.hp.toString())
}

private fun move(map: List<List<Boolean>>, critters: List<Critter>, critter: Critter, squareNextToTargetAtMinDistance: Square) {
    val squaresToMoveTo = getOpenAdjacentSquares(map, critters, critter.x, critter.y)

    val squareToDistance = mutableMapOf<Square, Int>()
    squaresToMoveTo.forEach {
        val distance = getDistanceIter(map, critters, it, squareNextToTargetAtMinDistance)
        if (distance != -1)
            squareToDistance[it] = distance
    }

    val minDistance = squareToDistance.minBy { it.value }?.value ?: -1
    val squaresAtMinDistance = squareToDistance.filter { it.value == minDistance }.keys
    val squareAtMinDistance = squaresAtMinDistance.sorted().first()

    critter.x = squareAtMinDistance.x
    critter.y = squareAtMinDistance.y
}

private fun attack(map: List<List<Boolean>>, critters: MutableList<Critter>, critter: Critter): Boolean {
    val targets = critters.filter { it.hp > 0 && it.type != critter.type }
    val squaresToAttack = getAdjacentSquares(map, critter.x, critter.y).toList().sorted()
    val enemiesToAttack = mutableListOf<Critter>()
    squaresToAttack.forEach { square ->
        val enemyInSquare = targets.firstOrNull { it.type != critter.type && it.x == square.x && it.y == square.y }
        if (enemyInSquare != null) {
            enemiesToAttack.add(enemyInSquare)
        }
    }
    if (enemiesToAttack.isNotEmpty()) {
        enemiesToAttack.sortBy { it.hp }
        val enemyToAttack = enemiesToAttack.first()
        if (enemyToAttack.hp <= 0) {
            println("uh oh")
        }
        enemyToAttack.hp -= critter.attack
        if (enemyToAttack.hp <= 0)
            critters.remove(enemyToAttack)
        return true
    }

    return false
}

fun getDistanceIter(map: List<List<Boolean>>, critters: List<Critter>, start: Square, end: Square): Int {
    var distance = 0
    if (start == end)
        return distance

    val visited: MutableSet<Square> = mutableSetOf(start)
    val toVisit: MutableSet<Square> = getOpenAdjacentSquares(map, critters, start.x, start.y).subtract(visited).toMutableSet()

    while (true) {
        distance++

        if (toVisit.isEmpty())
            return -1

        toVisit.forEach {
            if (it == end)
                return distance

            visited.add(it)
        }

        val justVisited = toVisit.toSet()
        toVisit.clear()

        justVisited.forEach {
            toVisit.addAll(getOpenAdjacentSquares(map, critters, it.x, it.y).subtract(visited))
        }
    }
}

fun isTraversable(map: List<List<Boolean>>, critters: List<Critter>, square: Square): Boolean {
    val isInMap = square.x >= 0 && square.x < map[0].size && square.y >= 0 && square.y < map[0].size
    return isInMap && !map[square.y][square.x] && critters.none { it.x == square.x && it.y == square.y }
}

fun getAdjacentSquares(map: List<List<Boolean>>, x: Int, y: Int): Set<Square> {
    return setOf(
            Square(x - 1, y),
            Square(x, y - 1),
            Square(x + 1, y),
            Square(x, y + 1))
            .filter {
                isTraversable(map, listOf(), it)
            }.toSet()
}

fun getOpenAdjacentSquares(map: List<List<Boolean>>, critters: List<Critter>, x: Int, y: Int): Set<Square> {
    return setOf(
            Square(x - 1, y),
            Square(x, y - 1),
            Square(x + 1, y),
            Square(x, y + 1))
            .filter {
                isTraversable(map, critters, it)
            }.toSet()
}

data class Square(val x: Int, val y: Int) : Comparable<Square> {
    override fun compareTo(other: Square): Int {
        return if (this.y == other.y)
            this.x.compareTo(other.x)
        else
            this.y.compareTo(other.y)
    }
}

data class Critter(val type: String, var x: Int, var y: Int, var hp: Int = 200, val attack: Int = 3) : Comparable<Critter> {
    override fun compareTo(other: Critter): Int {
        return if (this.y == other.y)
            this.x.compareTo(other.x)
        else
            this.y.compareTo(other.y)
    }
}