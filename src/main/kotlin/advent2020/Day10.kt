package advent2020

import kotlinx.coroutines.*
import java.io.File
import kotlin.math.absoluteValue
import kotlin.system.measureTimeMillis

object Day10 {
    val example1 = """
        16
        10
        15
        5
        1
        11
        7
        19
        6
        12
        4
    """.trimIndent().processInput()
    val example2 = """
        28
        33
        18
        42
        31
        14
        46
        20
        48
        47
        24
        23
        49
        45
        19
        38
        39
        11
        1
        32
        25
        35
        8
        17
        7
        9
        4
        2
        34
        10
        3
    """.trimIndent().processInput()

    private fun loadData() = File("src/main/resources/advent2020/Day10.txt").readText().processInput()

    private fun String.processInput() =
        split('\n')
        .filter { it.isNotBlank() }
        .map { it.toInt() }

    private fun List<Int>.countDifferences(): Pair<Int, Int> {
        val pairs = subList(0, size - 1)
            .zip(subList(1, size))
            .map { (first, second) -> (first - second).absoluteValue }
        val ones = pairs.count { it == 1 }
        val threes = pairs.count { it == 3 }
        return ones to threes
    }

    private fun List<Int>.isValid(): Boolean =
        subList(0, size - 1)
            .asSequence()
            .zip(subList(1, size).asSequence())
            .map { (first, second) -> (first - second).absoluteValue }
            .all { it <= 3 }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun List<Int>.isValidAt(removed: Int): Boolean = (this[removed + 1] - this[removed - 1]) <= 3

    private fun List<Int>.findValidCombinations(): Long {
        var result = 1L
        for (i in 1 until size - 1) {
            if (isValidAt(i)) {
                val newList = subList(i-1, i) + subList(i + 1, size)
                result += newList.findValidCombinations()
            }
        }
        return result
    }

    private suspend fun List<Int>.findValidCombinationsAsync(): Long {
        return coroutineScope {
            (1 until size - 1).asSequence()
                .filter { i -> isValidAt(i) }
                .map { i ->
                    async(Dispatchers.Default) {
                        val newList = subList(i-1, i) + subList(i + 1, size)
                        if (newList.size <= 20)
                            return@async newList.findValidCombinations()
                        else return@async newList.findValidCombinationsAsync()
                    }
                }
                .toList()
                .awaitAll().sum() + 1
        }
    }


    private fun List<Int>.findValidCombinationsTransitioning(): Long {
        var result = 1L
        val checked = mutableListOf(this[0])
        val tocheck = this.toMutableList()
        tocheck.removeFirst()

        for (i in 1 until size - 1) {
            val entry = tocheck.removeFirst()
            val last = checked.last()
            if (tocheck.first() - last <= 3) {
                result += (listOf(last) + tocheck).findValidCombinationsTransitioning()
            }
            checked.add(entry)
        }
        return result
    }


    fun start() {
        val jolts = loadData()
        val adapter = jolts.maxOrNull()!! + 3
        val outlet = 0
        val l = (listOf(adapter, outlet) + jolts).sorted().subList(30, jolts.size + 2)
        val (ones, threes) = l.countDifferences()
        println("RESULT: $ones, $threes => ${ones * threes}")


        val m1 = measureTimeMillis {
            println(runBlocking {
                l.findValidCombinationsAsync()
            })
//            println("RESULT: ${l.findValidCombinations()}")
        }
        println("Calculation took ${m1.toDouble() / 1000 }s")
    }
}

fun main() {
    Day10.start()
}