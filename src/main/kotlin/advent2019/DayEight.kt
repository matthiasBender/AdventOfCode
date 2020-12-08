package advent2019

import java.io.File

object DayEight {
    fun loadData(): List<Int> =
        File("src/main/resources/advent2019/DayEight.txt")
            .readText()
            .map { it.toString().toInt() }

    fun List<Int>.splitIntoLayers(width: Int, height: Int): List<List<Int>> = chunked(width * height)

    fun List<List<Int>>.findWithViewest(): List<Int> =
        this.minByOrNull { layer ->
            layer.filter { it == 0 }.size
        }!!

    fun List<List<Int>>.mergeLayers(): List<Int> {
        return reduce { top, layer ->
            top.zip(layer).map { (up, lower) ->
                if (up == 2) lower
                else up
            }
        }
    }
}

fun main(vararg args: String) {
    with (DayEight) {
        val data = loadData()

        val layers = data.splitIntoLayers(25, 6)
        println("found ${layers.size} layers, last has length: ${layers.last().size} = ${25 * 6}")
        val minLayer = layers.findWithViewest()
        val result1 = minLayer.filter{ it == 1 }.size * minLayer.filter{ it == 2 }.size
        println("RESULT 1: $result1")

        val picture = layers.mergeLayers()
        picture.chunked(25).map { line ->
            line.map {
                if (it == 0) ' '
                else '#'
            }.joinToString("")
        }.forEach(::println)
    }
}