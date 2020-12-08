package advent2020

import java.io.File

object Day3 {
    const val tree = '#'
    const val empty = '.'

    fun loadData(): List<String> {
        return File("src/main/resources/advent2020/Day3.txt").readText().split("\n")
    }

    data class Coordinates(
        val x: Int, val y: Int
    )

    data class Plain(
        private val fields: List<String>,
        val position: Coordinates = Coordinates(0, 0),
        val encounteredTrees: Int = 0
    ) {
        val height = fields.size
        private val width = fields[0].length

        fun walk(right: Int = 3, down: Int = 1): Plain {
            val next = Coordinates(
                x = (position.x + right) % width,
                y = position.y + down
            )
            return copy(
                position = next,
                encounteredTrees = if (next.y < height && get(next) == tree) encounteredTrees + 1 else encounteredTrees
            )
        }

        operator fun get(c: Coordinates): Char = fields[c.y][c.x]
    }

    tailrec fun walkDown(plain: Plain, right: Int = 3, down: Int = 1): Plain =
        if (plain.position.y >= plain.height) plain
        else walkDown(plain.walk(right, down), right, down)


    fun solveFirst() {
        val plain = Plain(loadData())
        println("RESULT = ${walkDown(plain).encounteredTrees}")

        val resSlope1 = walkDown(plain, 1, 1).encounteredTrees.toLong()
        val resSlope2 = walkDown(plain).encounteredTrees
        val resSlope3 = walkDown(plain, 5, 1).encounteredTrees
        val resSlope4 = walkDown(plain, 7, 1).encounteredTrees
        val resSlope5 = walkDown(plain, 1, 2).encounteredTrees

        val result = resSlope1 * resSlope2 * resSlope3 * resSlope4 * resSlope5
        println("RESULT = $result")
    }
}

fun main(vararg args: String) {
    Day3.solveFirst()
}
