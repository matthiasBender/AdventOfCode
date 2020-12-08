package advent2020

import java.io.File

object Day06 {
    private fun loadData() = File("src/main/resources/advent2020/Day06.txt").readText()
        .split("\n\n")

    private val questions = setOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')

    private fun String.extractChars(): Set<Char> = this.filter {
        it in questions
    }.toSet()

    private fun List<String>.countQuestions(): Int = map { it.extractChars().size }.sum()

    private fun String.extractCharsByPerson(): List<Set<Char>> =
        split('\n').map{ it.extractChars() }

    private fun String.countCommonsQuestions(): Int =
        extractCharsByPerson()
            .reduce { acc, c -> acc.intersect(c) }
            .size

    fun start() {
        val groups = loadData()
        println("RESULT: ${groups.countQuestions()}")
        println("RESULT: ${groups.map { it.countCommonsQuestions() }.sum()}")
    }
}

fun main(vararg args: String) {
    Day06.start()
}