package advent2020

import java.io.File

object DayTwo {
    private val pattern = "(\\d+)-(\\d+)\\s+(\\w+):\\s+(\\w+)".toRegex()

    fun loadData(): List<Policy> {
        val text = File("src/main/resources/advent2020/Day2.txt").readText()
        return text.split("\n")
            .mapNotNull { pattern.find(it) }
            .map(::Policy)
    }


    data class Policy(
        val range: IntRange,
        val part: String,
        val password: String
    ) {
        constructor(match: MatchResult) : this(
            range = match.groupValues[1].toInt()..match.groupValues[2].toInt(),
            part = match.groupValues[3],
            password = match.groupValues[4]
        )

        fun isValidPart1(): Boolean =
            password.indices.asSequence()
                .filter { i -> password.substring(i).startsWith(part) }
                .count() in range

        fun isValidPart2(): Boolean {
            val first = password.substring(range.first - 1).startsWith(part)
            val second = password.substring(range.last - 1).startsWith(part)
            return first xor second
        }
    }
    
    
}

fun main(vararg args: String) {
    val data = DayTwo.loadData()
    println("RESULT 1: ${data.filter { it.isValidPart1() }.size}")
    println("RESULT 2: ${data.filter { it.isValidPart2() }.size}")
}