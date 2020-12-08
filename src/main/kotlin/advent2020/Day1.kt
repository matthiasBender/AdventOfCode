package advent2020

import java.io.File

object Day1 {
    fun loadData(): List<Long> {
        val text = File("src/main/resources/advent2020/Day1.txt").readText()
        return text.split("\n").filter(String::isNotBlank).map(String::toLong)
    }

    fun findPair(data: List<Long>): Pair<Long, Long> {
        return data.asSequence()
            .map { first ->
                data.asSequence().map { first to it }
            }
            .flatten()
            .filter { (first, second) -> first + second == 2020L }
            .first()
    }

    fun findTriple(data: List<Long>): Triple<Long, Long, Long> {
        return data.asSequence()
            .map { first ->
                data.asSequence().map { first to it }
            }
            .flatten()
            .map { (first, sec) ->
                data.asSequence().map {  Triple(first, sec, it) }
            }
            .flatten()
            .filter { (first, second, third) -> first + second + third == 2020L }
            .first()
    }
}

fun main(vararg args: String) {
    val (first, second) = Day1.findPair(Day1.loadData())
    println("Result1: $first * $second = ${first * second}")
    val result2 = Day1.findTriple(Day1.loadData())
    println("Result2: $result2 => ${result2.first * result2.second * result2.third}")
}