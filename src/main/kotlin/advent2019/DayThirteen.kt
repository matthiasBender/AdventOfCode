package advent2019

import advent2019.intcode.InteractiveRuntime
import advent2019.intcode.Program

object DayThirteen {
    val inputProgram = longArrayOf(1,380,379,385,1008,2119,168858,381,1005,381,12,99,109,2120,1102,1,0,383,1102,0,1,382,21001,382,0,1,21002,383,1,2,21101,37,0,0,1105,1,578,4,382,4,383,204,1,1001,382,1,382,1007,382,37,381,1005,381,22,1001,383,1,383,1007,383,20,381,1005,381,18,1006,385,69,99,104,-1,104,0,4,386,3,384,1007,384,0,381,1005,381,94,107,0,384,381,1005,381,108,1106,0,161,107,1,392,381,1006,381,161,1101,0,-1,384,1106,0,119,1007,392,35,381,1006,381,161,1102,1,1,384,21001,392,0,1,21101,0,18,2,21101,0,0,3,21102,138,1,0,1105,1,549,1,392,384,392,21001,392,0,1,21102,18,1,2,21102,1,3,3,21102,1,161,0,1106,0,549,1101,0,0,384,20001,388,390,1,21001,389,0,2,21102,180,1,0,1105,1,578,1206,1,213,1208,1,2,381,1006,381,205,20001,388,390,1,20102,1,389,2,21102,1,205,0,1105,1,393,1002,390,-1,390,1102,1,1,384,21002,388,1,1,20001,389,391,2,21101,228,0,0,1105,1,578,1206,1,261,1208,1,2,381,1006,381,253,21002,388,1,1,20001,389,391,2,21101,253,0,0,1106,0,393,1002,391,-1,391,1102,1,1,384,1005,384,161,20001,388,390,1,20001,389,391,2,21101,279,0,0,1106,0,578,1206,1,316,1208,1,2,381,1006,381,304,20001,388,390,1,20001,389,391,2,21101,0,304,0,1106,0,393,1002,390,-1,390,1002,391,-1,391,1101,1,0,384,1005,384,161,21001,388,0,1,20102,1,389,2,21101,0,0,3,21102,338,1,0,1105,1,549,1,388,390,388,1,389,391,389,20102,1,388,1,21002,389,1,2,21102,4,1,3,21101,0,365,0,1105,1,549,1007,389,19,381,1005,381,75,104,-1,104,0,104,0,99,0,1,0,0,0,0,0,0,270,16,15,1,1,18,109,3,22102,1,-2,1,22102,1,-1,2,21101,0,0,3,21101,0,414,0,1105,1,549,21202,-2,1,1,22101,0,-1,2,21101,429,0,0,1106,0,601,2102,1,1,435,1,386,0,386,104,-1,104,0,4,386,1001,387,-1,387,1005,387,451,99,109,-3,2105,1,0,109,8,22202,-7,-6,-3,22201,-3,-5,-3,21202,-4,64,-2,2207,-3,-2,381,1005,381,492,21202,-2,-1,-1,22201,-3,-1,-3,2207,-3,-2,381,1006,381,481,21202,-4,8,-2,2207,-3,-2,381,1005,381,518,21202,-2,-1,-1,22201,-3,-1,-3,2207,-3,-2,381,1006,381,507,2207,-3,-4,381,1005,381,540,21202,-4,-1,-1,22201,-3,-1,-3,2207,-3,-4,381,1006,381,529,22102,1,-3,-7,109,-8,2105,1,0,109,4,1202,-2,37,566,201,-3,566,566,101,639,566,566,1202,-1,1,0,204,-3,204,-2,204,-1,109,-4,2105,1,0,109,3,1202,-1,37,593,201,-2,593,593,101,639,593,593,21002,0,1,-2,109,-3,2106,0,0,109,3,22102,20,-2,1,22201,1,-1,1,21102,1,373,2,21102,642,1,3,21101,0,740,4,21102,1,630,0,1106,0,456,21201,1,1379,-2,109,-3,2106,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,2,2,2,0,2,2,2,2,2,2,0,0,2,2,2,2,2,2,2,2,2,2,2,0,0,2,2,0,0,0,2,0,1,1,0,2,2,0,0,2,0,2,0,0,0,2,2,2,2,2,2,0,2,2,2,2,2,2,0,2,2,0,2,0,2,0,2,2,0,1,1,0,2,2,0,2,0,2,2,2,2,2,0,0,0,0,2,0,2,2,0,2,0,2,2,2,0,0,2,2,2,2,2,0,2,0,1,1,0,2,0,0,0,2,2,0,2,2,2,2,0,2,2,2,2,2,2,0,2,2,0,2,2,2,2,2,2,2,0,2,2,0,0,1,1,0,0,0,0,2,0,2,2,2,0,0,2,2,2,2,2,2,0,0,2,0,0,2,2,2,2,2,2,2,2,2,2,2,2,0,1,1,0,0,0,2,2,2,0,2,2,2,0,2,2,2,0,2,2,2,0,2,0,2,0,2,0,2,2,2,2,0,2,0,2,2,0,1,1,0,2,0,2,2,2,2,0,2,0,0,0,0,2,2,2,2,2,0,0,0,2,2,2,2,0,2,2,2,0,0,0,2,2,0,1,1,0,2,0,0,2,0,0,2,2,0,2,2,2,2,2,2,0,2,0,2,2,2,2,2,0,2,2,0,2,2,2,2,2,2,0,1,1,0,2,2,2,2,2,0,2,2,2,2,2,0,2,2,2,2,2,2,2,2,2,2,2,2,0,2,2,0,0,2,0,0,0,0,1,1,0,2,2,0,2,2,2,2,2,2,2,2,2,2,0,2,0,2,2,0,0,2,0,2,2,2,2,2,2,2,2,2,2,2,0,1,1,0,0,0,0,2,0,2,2,2,0,2,0,0,2,2,2,0,2,2,0,2,2,2,2,2,0,2,2,2,2,2,0,2,0,0,1,1,0,2,2,0,0,2,0,2,0,0,0,0,2,0,2,2,2,2,0,0,0,2,2,0,0,2,2,2,0,2,2,2,2,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,27,7,12,9,29,89,22,25,56,68,11,72,19,45,14,13,78,3,69,94,27,9,49,28,71,46,36,48,68,13,97,58,28,79,14,17,54,51,27,52,90,90,3,20,46,50,15,13,15,12,56,49,24,85,68,84,48,19,74,5,70,9,64,50,53,5,63,54,90,67,74,95,40,39,25,91,48,87,66,36,17,18,48,24,81,86,24,63,36,29,48,23,19,43,34,97,78,94,51,49,41,47,33,65,76,97,34,78,90,54,20,38,15,78,75,40,60,31,89,98,57,52,39,89,27,51,67,92,75,24,14,26,40,84,22,49,36,8,80,6,69,46,83,80,70,34,29,39,82,26,72,29,47,33,48,92,8,89,49,96,39,13,33,75,48,63,62,71,49,17,57,95,96,34,35,73,53,48,55,95,47,93,80,90,67,32,69,52,15,12,42,62,48,37,80,72,79,37,24,24,77,12,94,62,29,36,32,98,55,82,17,73,26,29,88,21,94,73,30,41,46,65,25,9,67,85,40,92,20,33,55,75,78,44,61,64,41,84,44,21,25,41,95,26,6,43,29,7,31,79,1,93,23,89,25,47,24,57,44,66,83,1,7,11,44,73,96,24,71,66,47,17,42,71,82,5,65,52,18,20,90,85,57,32,80,10,60,65,13,32,51,68,29,67,84,28,13,53,44,41,69,84,76,31,31,57,74,51,44,16,49,80,71,29,78,53,94,60,24,57,9,76,12,54,65,32,30,72,2,91,98,29,28,91,7,84,24,18,12,79,11,34,51,18,98,3,68,38,15,82,53,56,57,18,50,61,95,15,63,3,17,66,80,29,56,4,42,57,82,84,35,8,15,47,4,20,5,50,51,50,48,20,67,77,51,91,81,83,3,79,44,71,82,48,45,43,27,28,42,15,89,21,6,8,80,14,7,90,46,15,90,54,14,1,40,42,78,82,53,82,11,54,95,57,81,29,52,35,86,72,26,54,24,40,22,50,31,33,6,23,45,57,77,43,21,40,84,57,12,67,3,31,90,16,10,64,38,97,59,15,80,44,36,61,33,89,38,67,14,91,34,16,37,77,69,60,58,53,19,79,90,79,4,68,60,4,39,33,8,50,61,5,29,39,65,72,70,34,56,74,21,58,73,20,95,63,97,73,74,91,80,67,38,25,54,90,97,81,52,43,55,12,85,78,71,42,76,50,16,61,81,82,61,30,48,67,15,38,93,1,12,20,18,82,15,17,78,60,94,48,18,7,10,26,33,70,46,79,8,93,29,53,32,15,79,83,1,84,23,30,95,55,36,47,20,93,56,41,5,73,42,68,8,14,41,61,43,34,40,17,52,23,61,27,51,27,77,34,14,3,42,20,97,13,33,16,96,43,42,11,67,9,94,50,45,19,48,59,2,16,38,3,97,59,70,21,86,95,24,34,49,60,43,4,94,44,6,42,21,51,6,39,1,76,17,15,75,43,39,14,61,93,49,45,38,92,60,58,49,17,8,57,77,31,48,43,17,8,89,37,17,19,23,9,17,28,44,2,83,61,84,83,43,8,80,71,56,15,16,17,46,14,85,92,75,58,71,83,7,13,92,27,39,56,21,24,20,31,65,34,4,37,9,95,21,53,93,19,78,88,12,46,76,77,37,16,5,43,13,68,1,67,98,13,55,70,57,77,13,92,168858)

    enum class TileId(
        val code: Long,
        val display: Char
    ) {
        EMPTY(0, ' '),
        WALL(1, '#'),
        BLOCK(2, '='),
        PADDLE(3, '_'),
        BALL(4, 'o')
    }

    data class Tile(
        val x: Int,
        val y: Int,
        val tileId: TileId
    )

    class TileBuilder() {
        private val idIndex = TileId.values().associateBy(TileId::code)
        private var step = 0

        private var x = 0
        private var y = 0
        private var tileId = TileId.EMPTY

        fun addData(value: Long): Tile? {
            when(step++) {
                0 -> x = value.toInt()
                1 -> y = value.toInt()
                2 -> {
                    if (x == -1 && y == 0) {
                        step = 0
                        throw NewScoreException(value)
                    }
                    tileId = idIndex[value] ?: throw IllegalArgumentException("Tile Id $value does not exist! [x=$x, y=$y]")
                }
            }
            return if (step >= 3) {
                step = 0
                Tile(x, y, tileId)
            } else null
        }
    }

    class NewScoreException(val score: Long): Exception()

    class ArcadeRuntime(
        program: Program
    ) : InteractiveRuntime(program, memorySize = program.size + 20_000) {
        private val tileBuilder = TileBuilder()

        val canvas = Array(20) { Array(40) { TileId.EMPTY } }

        val tilesToDraw = mutableListOf<Tile>()
        var score = 0L

        fun printGame(tiles: List<Tile>, score: Long) {
            println("Score: $score")
            canvas.printOut()
        }

        override fun readInput(): Long {
            printGame(tilesToDraw, score)
            val xBall = canvas.findXOf(TileId.BALL)
            val xPaddle = canvas.findXOf(TileId.PADDLE)
            return xBall.compareTo(xPaddle).toLong()
        }

        override fun writeOutput(value: Long) {
            try {
                tileBuilder.addData(value)?.let {
                    canvas[it.y][it.x] = it.tileId
                }
            } catch (e: NewScoreException) {
                score = e.score
            }
        }

        fun insertCoin() {
            program[0] = 2
        }
    }

    fun Array<Array<TileId>>.printOut() {
        forEach { row ->
            println(
                row.map { it.display }.joinToString(" ")
            )
        }
    }
    fun Array<Array<TileId>>.reset() {
        for (i in this.indices) {
            this[i] = Array(40) { TileId.EMPTY }
        }
    }

    fun Array<Array<TileId>>.findXOf(it: TileId): Int {
        forEach { row ->
            row.forEachIndexed { i, value ->
                if (value == it) return i
            }
        }
        throw IllegalStateException("Could not find any item of $it!")
    }

    fun start() {
        val rt1 = ArcadeRuntime(inputProgram)
        rt1.execute()
        val result = rt1.tilesToDraw.filter { it.tileId == TileId.BLOCK }.size
        println("RESULT: $result")

        val rt2 = ArcadeRuntime(inputProgram)
        rt2.insertCoin()
        rt2.execute()
        println("RESULT: ${rt2.score}")
    }
}

fun main() {
    DayThirteen.start()
}