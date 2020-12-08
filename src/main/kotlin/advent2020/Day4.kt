package advent2020

import java.io.File

object Day4 {
    private fun loadData(): List<Passport> = File("src/main/resources/advent2020/Day4.txt").readText()
        .split("\n\n")
        .asSequence()
        .filter { it.isNotBlank() }
        .map { it.trim().parsePassport() }
        .toList()

    data class KeyValue(val key: String, val value: String)

    data class Passport(
        val values: List<KeyValue>
    ) {
        val indexed = values.associate { it.key to it.value }

        val isValidSimple get() = requiredFields.all { it in indexed }

        val isValid: Boolean get() {
            val byr = (indexed["byr"]?.toInt() ?: 0) in 1920..2002
            val iyr = (indexed["iyr"]?.toInt() ?: 0) in 2010..2020
            val eyr = (indexed["eyr"]?.toInt() ?: 0) in 2020..2030
            val hgt = indexed["hgt"]?.let { hgt ->
                val value = hgt.substring(0, hgt.length - 2).toIntOrNull() ?: -1
                val metric = hgt.substring(hgt.length - 2)
                (metric == "cm" && value in 150..193) || (metric == "in" && value in 59..76)
            } ?: false
            val hcl = colorPattern.matches(indexed["hcl"] ?: "")
            val ecl = indexed["ecl"] in setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
            val pid = pidPattern.matches(indexed["pid"] ?: "")

            return byr && iyr && eyr && hgt && hcl && ecl && pid
        }
    }

    private val requiredFields = listOf(
        "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"//, "cid"
    )
    private val colorPattern = "#[0-9a-f]{6}".toRegex()
    private val pidPattern = "[0-9]{9}".toRegex()
    
    private fun String.parsePassport(): Passport =
        Passport(
            split("(\\n|\\s)+".toRegex()).map { str ->
                val key = str.substring(0, 3)
                val value = str.substring(4)
                KeyValue(key, value)
            }
        )

    fun start() {
        val records = loadData()
        val validRecords = records.filter { it.isValidSimple }.size
        println("RESULT: $validRecords")

        val trueValids = records.filter { it.isValid }.size
        println("RESULT: $trueValids")

    }
}

fun main(vararg args: String) {
    Day4.start()
}