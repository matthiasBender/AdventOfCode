package advent2020

import java.io.File

object Day11 {
    enum class Field(
        val code: Char,
        val isSeat: Boolean
    ) {
        FLOOR('.', isSeat = false),
        EMPTY('L', isSeat = true),
        OCCUPIED('#', isSeat = true);

        override fun toString() = code.toString()
    }

    data class Board(
        val fields: List<List<Field>>
    ) {
        val height = fields.size
        val width = fields[0].size

        fun next(tolerated: Int, nextOnes: Board.(Int, Int) -> Sequence<Field>) = Board(
            fields.mapIndexed { y, row ->
                row.mapIndexed { x, _ -> findNext(x, y, tolerated, nextOnes) }
            }
        )

        fun countSeats(): Int = fields.asSequence().flatten().count { it == Field.OCCUPIED }

        override fun toString(): String =
            fields.joinToString("\n") { row ->
                row.joinToString("")
            }
    }

    private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> =
        first + other.first to second + other.second

    private val fieldMap = Field.values().associateBy {  it.code }

    val example1 = """
        L.LL.LL.LL
        LLLLLLL.LL
        L.L.L..L..
        LLLL.LL.LL
        L.LL.LL.LL
        L.LLLLL.LL
        ..L.L.....
        LLLLLLLLLL
        L.LLLLLL.L
        L.LLLLL.LL
    """.trimIndent()
        .parseInput()

    private fun loadData() = File("src/main/resources/advent2020/Day11.txt").readText().parseInput()

    private fun String.parseInput() = split('\n')
        .filter { it.isNotBlank() }
        .map { row ->
            row.map { fieldMap[it] ?: throw IllegalArgumentException("'$it' cannot be mapped!") }
        }
        .let(::Board)

    private fun Board.getAdjacent(x: Int, y: Int): Sequence<Field> =
        sequenceOf(
            x - 1 to y - 1,
            x to y - 1,
            x + 1 to y - 1,
            x - 1 to y,
            x + 1 to y,
            x - 1 to y + 1,
            x to y + 1,
            x + 1 to y + 1
        )
            .filter { (dx, dy) ->
                dx >= 0 && dy >= 0 && dx < width && dy < height
            }
            .map { (dx, dy) -> fields[dy][dx] }

    private fun Board.findNextSeat(x: Int, y: Int, directionX: Int, directionY: Int): Field? {
        require(directionX != 0 || directionY != 0) { "Either directionX($directionX) or directionY($directionY) has to be nonzero!" }
        val xRange = 0 until width
        val yRange = 0 until height
        var posX = x + directionX
        var posY = y + directionY
        while (posX in xRange && posY in yRange) {
            val element = fields[posY][posX]
            if (element.isSeat) return element
            posX += directionX
            posY += directionY
        }
        return null
    }

    private fun Board.findNextSeats(x: Int, y: Int): Sequence<Field> {
        return sequenceOf(
            -1 to -1,
            -1 to 0,
            -1 to 1,
            0 to -1,
            0 to 1,
            1 to -1,
            1 to 0,
            1 to 1
        ).mapNotNull { (dirX, dirY) ->
            findNextSeat(x, y, dirX, dirY)
        }
    }

    private fun Board.findNext(
        x: Int,
        y: Int,
        tolerated: Int,
        nextOnes: Board.(Int, Int) -> Sequence<Field>
    ): Field = when (fields[y][x]) {
        Field.FLOOR -> Field.FLOOR
        Field.EMPTY -> {
            val hasNoNeighbor = nextOnes(x, y).none { it == Field.OCCUPIED }
            if (hasNoNeighbor) Field.OCCUPIED else Field.EMPTY
        }
        Field.OCCUPIED -> {
            val numberOfNeighbors = nextOnes(x, y).count { it == Field.OCCUPIED }
            if (numberOfNeighbors >= tolerated) Field.EMPTY else Field.OCCUPIED
        }
    }

    private fun Board.findStable(tolerated: Int, nextOnes: Board.(Int, Int) -> Sequence<Field>): Board {
        var previous = this
        var next = this.next(tolerated, nextOnes)
        while (previous != next) {
            previous = next
            next = previous.next(tolerated, nextOnes)
        }
        return previous
    }

    fun start() {
        val board = loadData()
        println(board)
        println()
        val stable = board.findStable(4) { x, y ->
            getAdjacent(x, y)
        }
        println(stable)
        println("RESULT: ${stable.countSeats()}")

        println()
        val stable2 = board.findStable(5) { x, y ->
            findNextSeats(x, y)
        }
        println(stable2)
        println("RESULT: ${stable2.countSeats()}")
    }
}

fun main() { Day11.start() }

