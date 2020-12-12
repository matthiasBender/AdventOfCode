package advent2020

import java.io.File

object Day09 {
    private fun loadData() = File("src/main/resources/advent2020/Day09.txt").readText()
        .split("\n")
        .filter { it.isNotBlank() }
        .map { it.toLong() }


    private fun List<Long>.determineValidNumbers(): Set<Long> {
        val preamble = this.slice(0 until 25)
        return preamble.asSequence()
            .map { first ->
                preamble.asSequence()
                    .filter { it != first }
                    .map { second -> first + second }
            }
            .flatten()
            .toSet()
    }

    private fun List<Long>.findFirstInvalid(): Long {
        return subList(25, size).asSequence()
            .filterIndexed { i, value ->
                val previous = this.slice(0 + i until 25 + i).toSet()
                previous.none { value - it in previous }
            }
            .first()
    }

    private fun List<Long>.findSumFor(number: Long): IntRange {
        for (start in 0 until size) {
            for (end in start + 1 until size) {
                val r = start..end
                if (slice(r).sum() == number)
                    return r
            }
        }
        throw IllegalStateException("Could not find any result!")
    }

    fun start() {
        val values = loadData()
        val result = values.findFirstInvalid()
        println("RESULT: ${result}")
        val range = values.slice(values.findSumFor(result))
        val min = range.minOrNull()!!
        val max = range.maxOrNull()!!
        println("RESULT in range [min, max]: ${min + max}")

    }
}

fun main() {
    Day09.start()
}