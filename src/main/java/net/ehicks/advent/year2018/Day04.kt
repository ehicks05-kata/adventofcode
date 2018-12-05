package net.ehicks.advent.year2018

import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() {
    val path: Path = Path.of("src", "main", "resources", "year2018", "04.txt")
    val input: List<String> = Files.readAllLines(path)

    solveInput(input)
}

private fun solveInput(input: List<String>) {
    val records: MutableList<Record> = ArrayList()
    input.forEach {
        val rawTime = it.substring(it.indexOf("[") + 1, it.indexOf("]"))
        val time = LocalDateTime.parse(rawTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val event = it.substring(it.indexOf("]") + 2)
        val guard =
                if (event.startsWith("Guard")) {
                    event.split(" ")[1].substring(1).toInt()
                } else {
                    0
                }
        records.add(Record(time, guard, event))
    }

    records.sortBy { record -> record.dateTime }

    val napTimes: MutableMap<Int, MutableList<Int>> = HashMap()
    var currentGuard = 0
    var fellAsleepAtMinute = 0

    records.forEach {
        if (it.guard != 0)
        {
            currentGuard = it.guard
            napTimes.putIfAbsent(currentGuard, ArrayList())
        }
        if (it.event.contains("falls asleep"))
        {
            fellAsleepAtMinute = it.dateTime.minute
        }
        if (it.event.contains("wakes up"))
        {
            for (i in fellAsleepAtMinute until it.dateTime.minute)
                napTimes[currentGuard]?.add(i)
        }
    }

    val sleepyGuard = napTimes.entries.maxBy { it.value.size }!!.key
    val commonMinute = napTimes[sleepyGuard]!!.groupingBy { it }?.eachCount()?.maxBy { entry: Map.Entry<Int, Int> -> entry.value }!!.key

    println("part 1: " + sleepyGuard * commonMinute)

    var maxMinute = 0
    var maxMinuteCount = 0
    var maxMinuteGuard = 0
    napTimes.entries.forEach {
        if (it.value.size == 0)
            return@forEach

        val maxMin = napTimes[it.key]!!.groupingBy { i -> i }.eachCount().maxBy { entry: Map.Entry<Int, Int> -> entry.value }
        if (maxMin!!.value > maxMinuteCount)
        {
            maxMinute = maxMin.key
            maxMinuteCount = maxMin.value
            maxMinuteGuard = it.key
        }
    }

    println("part 2: " + maxMinute * maxMinuteGuard)

}

private data class Record(val dateTime: LocalDateTime, val guard: Int, val event: String)