package advent2020

import java.io.File

/**
 * 128 Rows (first 7 characters)
 * F => 0
 * B => 1
 * 8 Cols (last 3 characters)
 * L => 0
 * R => 1
 * seatId = row * 8 + column
 */
object Day5 {
    private fun loadData() = File("src/main/resources/advent2020/Day5.txt").readText()
        .split("\n")

    private fun String.calculateSeatId(): Int =
        this.reversed().foldIndexed(0) { index, acc, c ->
            if (c == 'F' || c == 'L') acc
            else acc + (1 shl index)
        }

    fun start() {
        val seatIds = loadData().map { it.calculateSeatId() }.sorted()
        println("RESULT: ${seatIds.maxOrNull()}")
        val index = seatIds.toSet()
        val results = (seatIds.first()..seatIds.last())
            .filter {
                it !in index && (it - 1) in index && (it + 1) in index
            }

        println("RESULT: $results")

    }
}

fun main(vararg args: String) {
    Day5.start()
}