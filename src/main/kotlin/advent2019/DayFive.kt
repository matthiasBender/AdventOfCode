package advent2019

/**
 * Opcode:
 * 99 -> end program
 * 1, x, y, z -> X[z] = X[x] + X[y]
 * 2, x, y, z -> X[z] = X[x] * X[y]
 */
object DayFive {
    val testProgram = arrayOf(
        1,9,10,3,
        2,3,11,0,
        99,30,40,50
    )

    val task1 = arrayOf(
        3,225,1,225,6,6,1100,1,238,225,104,0,1002,114,46,224,1001,224,-736,224,4,224,1002,223,8,223,1001,224,3,224,1,223,224,223,1,166,195,224,1001,224,-137,224,4,224,102,8,223,223,101,5,224,224,1,223,224,223,1001,169,83,224,1001,224,-90,224,4,224,102,8,223,223,1001,224,2,224,1,224,223,223,101,44,117,224,101,-131,224,224,4,224,1002,223,8,223,101,5,224,224,1,224,223,223,1101,80,17,225,1101,56,51,225,1101,78,89,225,1102,48,16,225,1101,87,78,225,1102,34,33,224,101,-1122,224,224,4,224,1002,223,8,223,101,7,224,224,1,223,224,223,1101,66,53,224,101,-119,224,224,4,224,102,8,223,223,1001,224,5,224,1,223,224,223,1102,51,49,225,1101,7,15,225,2,110,106,224,1001,224,-4539,224,4,224,102,8,223,223,101,3,224,224,1,223,224,223,1102,88,78,225,102,78,101,224,101,-6240,224,224,4,224,1002,223,8,223,101,5,224,224,1,224,223,223,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,1107,226,677,224,102,2,223,223,1006,224,329,101,1,223,223,1108,226,677,224,1002,223,2,223,1005,224,344,101,1,223,223,8,226,677,224,102,2,223,223,1006,224,359,1001,223,1,223,1007,226,677,224,1002,223,2,223,1005,224,374,101,1,223,223,1008,677,677,224,1002,223,2,223,1005,224,389,1001,223,1,223,1108,677,226,224,1002,223,2,223,1006,224,404,1001,223,1,223,1007,226,226,224,1002,223,2,223,1005,224,419,1001,223,1,223,1107,677,226,224,1002,223,2,223,1006,224,434,101,1,223,223,108,677,677,224,1002,223,2,223,1005,224,449,1001,223,1,223,1107,677,677,224,102,2,223,223,1005,224,464,1001,223,1,223,108,226,226,224,1002,223,2,223,1006,224,479,1001,223,1,223,1008,226,226,224,102,2,223,223,1005,224,494,101,1,223,223,108,677,226,224,102,2,223,223,1005,224,509,1001,223,1,223,8,677,226,224,1002,223,2,223,1006,224,524,101,1,223,223,7,226,677,224,1002,223,2,223,1006,224,539,101,1,223,223,7,677,226,224,102,2,223,223,1006,224,554,1001,223,1,223,7,226,226,224,1002,223,2,223,1006,224,569,101,1,223,223,107,677,677,224,102,2,223,223,1006,224,584,101,1,223,223,1108,677,677,224,102,2,223,223,1006,224,599,1001,223,1,223,1008,677,226,224,1002,223,2,223,1005,224,614,1001,223,1,223,8,677,677,224,1002,223,2,223,1006,224,629,1001,223,1,223,107,226,677,224,1002,223,2,223,1006,224,644,101,1,223,223,1007,677,677,224,102,2,223,223,1006,224,659,101,1,223,223,107,226,226,224,1002,223,2,223,1006,224,674,1001,223,1,223,4,223,99,226
    )

    enum class Command(
        val code: Byte
    ) {
        ADD(1),
        MUL(2),
        STORE(3),
        LOAD(4),
        JUMP_TRUE(5),
        JUMP_FALSE(6),
        LESS_THAN(7),
        EQUALS(8),
        EXIT(99),
        UNKNOWN(Byte.MAX_VALUE);

        companion object {
            private val commands = values().associateBy(Command::code)

            fun fromCode(code: Byte): Command = commands[code] ?: UNKNOWN
        }
    }

    data class OpCode(
        val code: Command,
        val modeArg3: Byte,
        val modeArg2: Byte,
        val modeArg1: Byte
    ) {
        constructor(code: Int) : this(
            code = Command.fromCode((code % 100).toByte()),
            modeArg1 = (code % 1000 / 100).toByte(),
            modeArg2 = (code % 10_000 / 1000).toByte(),
            modeArg3 = (code % 100_000 / 10_000).toByte()
        )
        companion object {
            const val MODE_POS: Byte = 0
            const val MODE_IMM: Byte = 1
        }
    }

    fun executeInstruction(
        program: Array<Int>,
        instructionPointer: Int
    ): Int {
        val opCode = OpCode(program[instructionPointer])
        return when (opCode.code) {
            Command.ADD -> {
                val (arg1, arg2) = program.readFirstArgs(instructionPointer, opCode)
                program.writeValue(
                    address = instructionPointer + 3,
                    value = arg1 + arg2,
                    mode = opCode.modeArg3
                )
                instructionPointer + 4
            }
            Command.MUL -> {
                val (arg1, arg2) = program.readFirstArgs(instructionPointer, opCode)
                program.writeValue(
                    address = instructionPointer + 3,
                    value = arg1 * arg2,
                    mode = opCode.modeArg3
                )
                instructionPointer + 4
            }
            Command.STORE -> {
                print(">> INPUT <<: ")
                program.writeValue(
                    address = instructionPointer + 1,
                    value = readLine()!!.toInt(),
                    mode = opCode.modeArg1
                )
                instructionPointer + 2
            }
            Command.LOAD -> {
                println(">> OUTPUT >>: " + program.readValue(
                    address = instructionPointer + 1,
                    mode = opCode.modeArg1
                ))
                instructionPointer + 2
            }
            Command.JUMP_TRUE -> {
                val value = program.readValue(
                    address = instructionPointer + 1,
                    mode = opCode.modeArg1
                )
                if (value != 0) {
                    program.readValue(
                        address = instructionPointer + 2,
                        mode = opCode.modeArg2
                    )
                } else instructionPointer + 3
            }
            Command.JUMP_FALSE -> {
                val value = program.readValue(
                    address = instructionPointer + 1,
                    mode = opCode.modeArg1
                )
                if (value == 0) {
                    program.readValue(
                        address = instructionPointer + 2,
                        mode = opCode.modeArg2
                    )
                } else instructionPointer + 3
            }
            Command.LESS_THAN -> {
                val (arg1, arg2) = program.readFirstArgs(instructionPointer, opCode)
                program.writeValue(
                    instructionPointer + 3,
                    if (arg1 < arg2) 1 else 0,
                    opCode.modeArg3
                )
                instructionPointer + 4
            }
            Command.EQUALS -> {
                val (arg1, arg2) = program.readFirstArgs(instructionPointer, opCode)
                program.writeValue(
                    instructionPointer + 3,
                    if (arg1 == arg2) 1 else 0,
                    opCode.modeArg3
                )
                instructionPointer + 4
            }
            Command.EXIT -> program.size
            else -> throw IllegalStateException("OpCode $opCode cannot be executed! ${program[instructionPointer]} at address $instructionPointer")
        }
    }

    fun Array<Int>.readValue(address: Int, mode: Byte): Int {
        check(address >= 0 && address < this.size) { "Position $address is not available on the program!" }
        val targetAddress = if (mode == OpCode.MODE_POS) this[address] else address
        check(targetAddress >= 0 && targetAddress < this.size) { "Position $targetAddress is not available on the program!" }
        return this[targetAddress]
    }

    fun Array<Int>.writeValue(address: Int, value: Int, mode: Byte) {
        check(address >= 0 && address < this.size) { "Position $address is not available on the program!" }
        val targetAddress = if (mode == OpCode.MODE_POS) this[address] else address
        check(targetAddress >= 0 && targetAddress < this.size) { "Position $targetAddress is not available on the program!" }
        this[targetAddress] = value
    }

    fun Array<Int>.readFirstArgs(address: Int, opCode: OpCode): Pair<Int, Int> = Pair(
        readValue(address + 1, opCode.modeArg1),
        readValue(address + 2, opCode.modeArg2)
    )

    fun execute(program: Array<Int>) {
        var instructionPointer = 0
        while(instructionPointer < program.size) {
            instructionPointer = executeInstruction(program, instructionPointer)
        }
    }
}

fun main(vararg args: String) {
    DayFive.execute(DayFive.task1)
}
