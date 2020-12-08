package advent2019

import advent2019.intcode.Command
import advent2019.intcode.OpCode
import advent2019.intcode.Program
import advent2019.intcode.Runtime

object DayEleven {
    private val inputProgram: Program = longArrayOf(3,8,1005,8,324,1106,0,11,0,0,0,104,1,104,0,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,1,10,4,10,1001,8,0,29,1,1107,14,10,1006,0,63,1006,0,71,3,8,1002,8,-1,10,101,1,10,10,4,10,1008,8,1,10,4,10,1002,8,1,61,1,103,18,10,1006,0,14,1,105,7,10,3,8,1002,8,-1,10,101,1,10,10,4,10,1008,8,1,10,4,10,101,0,8,94,1006,0,37,1006,0,55,2,1101,15,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,0,10,4,10,101,0,8,126,2,1006,12,10,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,1,10,4,10,1001,8,0,152,3,8,102,-1,8,10,1001,10,1,10,4,10,108,0,8,10,4,10,101,0,8,173,1006,0,51,1006,0,26,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,0,10,4,10,1001,8,0,202,2,8,18,10,1,103,19,10,1,1102,1,10,1006,0,85,3,8,102,-1,8,10,1001,10,1,10,4,10,108,0,8,10,4,10,1001,8,0,238,2,1002,8,10,1006,0,41,3,8,102,-1,8,10,1001,10,1,10,4,10,108,0,8,10,4,10,101,0,8,267,2,1108,17,10,2,105,11,10,1006,0,59,1006,0,90,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,1,10,4,10,1001,8,0,304,101,1,9,9,1007,9,993,10,1005,10,15,99,109,646,104,0,104,1,21102,936735777688,1,1,21101,341,0,0,1105,1,445,21101,0,937264173716,1,21101,352,0,0,1106,0,445,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,21101,3245513819,0,1,21102,1,399,0,1105,1,445,21102,1,29086470235,1,21102,410,1,0,1105,1,445,3,10,104,0,104,0,3,10,104,0,104,0,21101,825544712960,0,1,21102,1,433,0,1106,0,445,21102,825460826472,1,1,21101,0,444,0,1106,0,445,99,109,2,22102,1,-1,1,21101,0,40,2,21101,0,476,3,21102,466,1,0,1105,1,509,109,-2,2105,1,0,0,1,0,0,1,109,2,3,10,204,-1,1001,471,472,487,4,0,1001,471,1,471,108,4,471,10,1006,10,503,1101,0,0,471,109,-2,2106,0,0,0,109,4,2101,0,-1,508,1207,-3,0,10,1006,10,526,21101,0,0,-3,21202,-3,1,1,21201,-2,0,2,21101,0,1,3,21101,0,545,0,1105,1,550,109,-4,2105,1,0,109,5,1207,-3,1,10,1006,10,573,2207,-4,-2,10,1006,10,573,21202,-4,1,-4,1106,0,641,21202,-4,1,1,21201,-3,-1,2,21202,-2,2,3,21101,0,592,0,1105,1,550,22101,0,1,-4,21101,1,0,-1,2207,-4,-2,10,1006,10,611,21102,1,0,-1,22202,-2,-1,-2,2107,0,-3,10,1006,10,633,22101,0,-1,1,21102,633,1,0,105,1,508,21202,-2,-1,-2,22201,-4,-2,-4,109,-5,2105,1,0)

    private val printImm = Command.PRINT.toOpCode(OpCode.MODE_IMM)
    val testProgram: Program = longArrayOf(
        printImm, 1, printImm, 0,
        printImm, 0, printImm, 0,
        printImm, 1, printImm, 0,
        printImm, 1, printImm, 0,
        printImm, 0, printImm, 1,
        printImm, 1, printImm, 0,
        printImm, 1, printImm, 0,
        Command.EXIT.toOpCode()
    )

    data class Coordinates(
        val x: Int,
        val y: Int
    ) {
        operator fun plus(d: Direction): Coordinates {
            return when(d) {
                Direction.UP -> copy(y = y - 1)
                Direction.DOWN -> copy(y = y + 1)
                Direction.LEFT -> copy(x = x - 1)
                Direction.RIGHT -> copy(x = x + 1)
            }
        }
    }

    enum class Direction {
        UP, DOWN, LEFT, RIGHT;

        fun turnLeft(): Direction = when(this) {
            UP -> LEFT
            DOWN -> RIGHT
            LEFT -> DOWN
            RIGHT -> UP
        }

        fun turnRight(): Direction = when(this) {
            UP -> RIGHT
            DOWN -> LEFT
            LEFT -> UP
            RIGHT -> DOWN
        }
    }

    enum class PaintOrMove {
        PAINT, MOVE;
    }

    class PaintingRuntime(
        _program: Program,
        var position: Coordinates = Coordinates(0, 0),
        var direction: Direction = Direction.UP,
        startingWhites: List<Coordinates> = emptyList()
    ) : Runtime {
        override val program: Program = LongArray(_program.size + 200_000) { i ->
            if (i < _program.size) _program[i]
            else 0
        }
        override var relativeBase: Int = 0

        val whiteFields = startingWhites.toMutableSet()
        val blackFields = mutableSetOf<Coordinates>()
        var paintMove: PaintOrMove = PaintOrMove.PAINT

        // 1 => White; 0 => Black
        override fun readInput(): Long =
            if (position in whiteFields) 1
            else 0

        override fun writeOutput(value: Long) {
            if (paintMove == PaintOrMove.PAINT) {
                when(value) {
                    0L -> {
                        whiteFields.remove(position)
                        blackFields.add(position)
                    }
                    1L -> {
                        whiteFields.add(position)
                        blackFields.remove(position)
                    }
                }
                paintMove = PaintOrMove.MOVE
            } else {
                when(value) {
                    0L -> {
                        direction = direction.turnLeft()
                    }
                    1L -> {
                        direction = direction.turnRight()
                    }
                }
                position += direction
                paintMove = PaintOrMove.PAINT
            }
        }
    }

    fun Collection<Coordinates>.dimensions(): Pair<Coordinates, Coordinates> {
        val minX = minOf(Coordinates::x)
        val maxX = maxOf(Coordinates::x)
        val minY = minOf(Coordinates::y)
        val maxY = maxOf(Coordinates::y)

        val sizes = Coordinates(maxX - minX, maxY - minY)
        val offsets = Coordinates(minX, minY)
        return sizes to offsets
    }

    fun start() {
        val runtime = PaintingRuntime(inputProgram)
        runtime.execute()
        val paintedOnce = runtime.blackFields.union(runtime.whiteFields)
        println("RESULT: ${paintedOnce.size}")

        val secondRuntime = PaintingRuntime(inputProgram, startingWhites = listOf(Coordinates(0, 0)))
        secondRuntime.execute()
        val (dimensions, offsets) = secondRuntime.blackFields.union(secondRuntime.whiteFields).dimensions()
        val canvas = Array(dimensions.y + 1) {
            Array(dimensions.x + 1) { ' ' }
        }
        secondRuntime.whiteFields.forEach { c ->
            canvas[c.y + offsets.y][c.x + offsets.x] = '#'
        }
        canvas.forEach { row ->
            println(row.toCharArray().concatToString())
        }
    }
}

fun main(vararg args: String) {
    DayEleven.start()
}