package net.ehicks.advent.year2018

import edu.princeton.cs.algs4.StdDraw
import java.awt.Color
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "13.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(input)
}

private fun solveInput(input: List<String>) {
    StdDraw.enableDoubleBuffering()
    StdDraw.setCanvasSize(1000, 1000)
    StdDraw.setXscale(0.0, 150.0)
    StdDraw.setYscale(0.0, 150.0)
    StdDraw.setPenRadius(.004)

    val carts = mutableListOf<Cart>()

    val tracks = input.mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            when {
                listOf('>', '<').contains(c) -> {
                    carts.add(Cart(carts.size, x, y, c))
                    '-'
                }
                listOf('^', 'v').contains(c) -> {
                    carts.add(Cart(carts.size, x, y, c))
                    '|'
                }
                else -> c
            }
        }
    }

    val directions = listOf('^', '>', 'v', '<')
    val cartDirections = mapOf(
            '<' to Pair(-1, 0)
            , '>' to Pair(1, 0)
            , '^' to Pair(0, -1)
            , 'v' to Pair(0, 1)
    )

    var part1Found = false

    val trails = mutableMapOf<Pair<Int, Int>, Int>()
    
    while (true) {
        carts.sort()

        StdDraw.clear()
        tracks.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, c ->
                val x = colIndex.toDouble() + .5
                val y = (150 - rowIndex.toDouble()) - .5
                StdDraw.setPenColor(Color.GRAY)

                val trailEntry = trails[Pair(rowIndex, colIndex)]
                if (trailEntry != null)
                {
                    StdDraw.setPenColor(Color(0 + (trailEntry * 6), 0 + (trailEntry * 6), 255 - (trailEntry * 5)))
                }

                when (tracks[rowIndex][colIndex]) {
                    '|' -> {
                        StdDraw.line(x + .0, y - .5, x + .0, y + .5)
                    }
                    '-' -> {
                        StdDraw.line(x - .5, y + .0, x + .5, y + .0)
                    }
                    '/' -> {
                        if (colIndex == 0 || colIndex > 0 && (tracks[rowIndex][colIndex - 1] == ' ' || tracks[rowIndex][colIndex - 1] == '|' || tracks[rowIndex][colIndex - 1] == '\\'))
                            StdDraw.line(x, y - .5, x + .5, y) // right-bottom
                        else
                            StdDraw.line(x - .5, y, x, y + .5) // left-top
                    }
                    '\\' -> {
                        if (colIndex == 0 || colIndex > 0 && (tracks[rowIndex][colIndex - 1] == ' ' || tracks[rowIndex][colIndex - 1] == '|'))
                            StdDraw.line(x, y + .5, x + .5, y) // right-top
                        else
                            StdDraw.line(x - .5, y, x, y - .5) // left-bottom
                    }
                    '+' -> {
                        StdDraw.line(x + .0, y - .5, x + .0, y + .5)
                        StdDraw.line(x - .5, y + .0, x + .5, y + .0)
                    }
                }
            }
        }

        StdDraw.setPenColor(Color.BLUE)
        carts.forEach {
            val x = it.x.toDouble() + .5
            val y = (150 - it.y.toDouble()) - .5
            StdDraw.filledCircle(x, y, 0.4)
        }
        StdDraw.show()
        StdDraw.setPenColor()

        val cartsToRemove = mutableSetOf<Cart>()
        
        trails.entries.forEach { mutableEntry -> mutableEntry.setValue(mutableEntry.value + 1) }
        trails.entries.removeIf { it.value > 20 }
        
        carts.forEach {
            trails[Pair(it.y, it.x)] = 1
            
            val newXy = Pair(it.x + cartDirections[it.direction]!!.first, it.y + cartDirections[it.direction]!!.second)

            // collision check
            if (carts.filter { it.x == newXy.first && it.y == newXy.second }.any())
            {
                if (!part1Found)
                {
                    println("part 1: $newXy")
                    part1Found = true
                }
                cartsToRemove.add(it)
                cartsToRemove.add(carts.first { it.x == newXy.first && it.y == newXy.second })
            }

            // turn check
            val track = tracks[newXy.second][newXy.first]
            when (track) {
                '|' -> null
                '-' -> null
                '/' ->
                    it.direction = mapOf(
                            '<' to 'v'
                            , '>' to '^'
                            , '^' to '>'
                            , 'v' to '<'
                    )[it.direction]!!
                '\\' ->
                    it.direction = mapOf(
                            '<' to '^'
                            , '>' to 'v'
                            , '^' to '<'
                            , 'v' to '>'
                    )[it.direction]!!
                '+' -> {
                    it.direction = directions[(directions.indexOf(it.direction) + it.nextIntersectionTurn + 4) % 4]
                    it.nextIntersectionTurn =
                            if (it.nextIntersectionTurn == 1)
                                -1
                            else
                                it.nextIntersectionTurn + 1
                }
                else -> {
                    StdDraw.setPenColor(Color.RED)
                    val x = it.x.toDouble() + .5
                    val y = (150 - it.y.toDouble()) - .5
                    StdDraw.filledCircle(x, y, 0.4)
                    StdDraw.show()
                    throw Exception("Whoa Nelly!")
                }
            }

            // move
            it.x = newXy.first
            it.y = newXy.second
        }

        if (cartsToRemove.size > 0)
        {
            cartsToRemove.forEach {
                carts.remove(it)
                println("Removing $it. Carts left: ${carts.size}")
            }
            println("Carts left: ${carts.size}")
            if (carts.size == 1)
            {
                println("part 2: ${carts.first()}")
                break
            }
        }
    }
}

private data class Cart(val id: Int, var x: Int, var y: Int, var direction: Char, var nextIntersectionTurn: Int = -1) : Comparable<Cart> {
    override fun compareTo(other: Cart): Int {
        return if (this.y == other.y)
            this.x.compareTo(other.x)
        else
            this.y.compareTo(other.y)
    }
}