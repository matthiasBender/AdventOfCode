package advent2019

import java.io.File

object DayOne {
    fun loadData(): List<Long> {
        val text = File("src/main/resources/advent2019/dayone.txt").readText()
        return text.split("\n").filter(String::isNotBlank).map(String::toLong)
    }

    fun fuelForWeight(mass: Long) = maxOf(mass / 3 - 2, 0)

    fun calculateMass(mass: Long): Long {
        if (mass <= 0) return 0L
        val fuel = fuelForWeight(mass)
        return fuel + calculateMass(fuel)
    }
}


fun main(vararg args: String) {
    val data = DayOne.loadData()
    val result1 = data.map(DayOne::fuelForWeight).sum()
    println("Result1: $result1")
    val result2 = data.map(DayOne::calculateMass).sum()
    println("Result2: $result2")
}
