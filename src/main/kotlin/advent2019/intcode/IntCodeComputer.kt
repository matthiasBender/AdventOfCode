package advent2019.intcode

enum class Command(
    val code: Byte
) {
    ADD(1),
    MUL(2),
    READ(3),
    PRINT(4),
    JUMP_TRUE(5),
    JUMP_FALSE(6),
    LESS_THAN(7),
    EQUALS(8),
    ADJUST_RELATIVE_BASE(9),
    EXIT(99),
    UNKNOWN(Byte.MAX_VALUE);

    fun toOpCode(argMode1: Byte = 0, argMode2: Byte = 0, argMode3: Byte = 0): Long {
        check(argMode1 in 0..2) { "argMode1 has to be in between 0 and 2 but was $argMode1!" }
        check(argMode2 in 0..2) { "argMode2 has to be in between 0 and 2 but was $argMode2!" }
        check(argMode3 in 0..2) { "argMode3 has to be in between 0 and 2 but was $argMode3!" }
        return argMode3 * 10_000L + argMode2 * 1000L + argMode1 * 100L + code
    }

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
    constructor(code: Long) : this(
        code = Command.fromCode((code % 100).toByte()),
        modeArg1 = (code % 1000 / 100).toByte(),
        modeArg2 = (code % 10_000 / 1000).toByte(),
        modeArg3 = (code % 100_000 / 10_000).toByte()
    )
    companion object {
        const val MODE_POS: Byte = 0
        const val MODE_IMM: Byte = 1
        const val MODE_REL: Byte = 2
    }
}

typealias Program = LongArray

fun Runtime.executeInstruction(
    instructionPointer: Int
): Int {
    val opCode = OpCode(program[instructionPointer])
    logOpcode(instructionPointer, opCode)
    return when (opCode.code) {
        Command.ADD -> {
            combineArgs(instructionPointer, opCode, relativeBase) { arg1, arg2 ->
                arg1 + arg2
            }
        }
        Command.MUL -> {
            combineArgs(instructionPointer, opCode, relativeBase) { arg1, arg2 ->
                arg1 * arg2
            }
        }
        Command.READ -> {
            writeValue(
                address = instructionPointer + 1,
                value = readInput(),
                mode = opCode.modeArg1,
                relBase = relativeBase
            )
            instructionPointer + 2
        }
        Command.PRINT -> {
            writeOutput(
                readValue(
                    address = instructionPointer + 1,
                    mode = opCode.modeArg1,
                    relBase = relativeBase
                )
            )
            instructionPointer + 2
        }
        Command.JUMP_TRUE -> {
            val value = readValue(
                address = instructionPointer + 1,
                mode = opCode.modeArg1,
                relBase = relativeBase
            )
            if (value != 0L) {
                readValue(
                    address = instructionPointer + 2,
                    mode = opCode.modeArg2,
                    relBase = relativeBase
                ).toInt()
            } else instructionPointer + 3
        }
        Command.JUMP_FALSE -> {
            val value = readValue(
                address = instructionPointer + 1,
                mode = opCode.modeArg1,
                relBase = relativeBase
            )
            if (value == 0L) {
                readValue(
                    address = instructionPointer + 2,
                    mode = opCode.modeArg2,
                    relBase = relativeBase
                ).toInt()
            } else instructionPointer + 3
        }
        Command.LESS_THAN -> {
            combineArgs(instructionPointer, opCode, relativeBase) { arg1, arg2 ->
                if (arg1 < arg2) 1 else 0
            }
        }
        Command.EQUALS -> {
            combineArgs(instructionPointer, opCode, relativeBase) { arg1, arg2 ->
                if (arg1 == arg2) 1 else 0
            }
        }
        Command.ADJUST_RELATIVE_BASE -> {
            relativeBase += readValue(
                address = instructionPointer + 1,
                mode = opCode.modeArg1,
                relBase = relativeBase
            ).toInt()
            instructionPointer + 2
        }
        Command.EXIT -> program.size
        Command.UNKNOWN -> throw IllegalStateException("OpCode $opCode cannot be executed! ${program[instructionPointer]} at address $instructionPointer")
    }.also { logEndLine() }
}


private fun Runtime.readFirstArgs(address: Int, opCode: OpCode, relBase: Int): Pair<Long, Long> = Pair(
    readValue(address + 1, opCode.modeArg1, relBase),
    readValue(address + 2, opCode.modeArg2, relBase)
)

private fun Runtime.combineArgs(address: Int, opCode: OpCode, relBase: Int, transform: (Long, Long) -> Long): Int {
    val (arg1, arg2) = readFirstArgs(address, opCode, relBase)
    writeValue(address + 3, transform(arg1, arg2), opCode.modeArg3, relBase)
    return address + 4
}

private fun Runtime.readValue(address: Int, mode: Byte, relBase: Int): Long {
    check(address >= 0 && address < program.size) { "Position $address is not available on the program!" }
    val targetAddress = when (mode) {
        OpCode.MODE_IMM -> address
        OpCode.MODE_POS -> program[address].toInt()
        OpCode.MODE_REL -> relBase + program[address].toInt()
        else -> throw IllegalArgumentException("Mode $mode is not supported!")
    }
    check(targetAddress >= 0 && targetAddress < program.size) { "Position $targetAddress is not available on the program!" }
    return program[targetAddress].also{ logRead(address, program[address], mode, it) }
}

private fun Runtime.writeValue(address: Int, value: Long, mode: Byte, relBase: Int) {
    check(address >= 0 && address < program.size) { "Position $address is not available on the program!" }
    val targetAddress = when (mode) {
        OpCode.MODE_IMM -> address
        OpCode.MODE_POS -> program[address].toInt()
        OpCode.MODE_REL -> relBase + program[address].toInt()
        else -> throw IllegalArgumentException("Mode $mode is not supported!")
    }
    logWrite(address, program[address], mode, value)
    check(targetAddress >= 0 && targetAddress < program.size) { "Position $targetAddress is not available on the program!" }
    program[targetAddress] = value
}
