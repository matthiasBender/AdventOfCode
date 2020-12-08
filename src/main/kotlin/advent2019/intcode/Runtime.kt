package advent2019.intcode


interface Runtime {
    val program: Program
    var relativeBase: Int

    fun readInput(): Long
    fun writeOutput(value: Long)

    fun logOpcode(address: Int, opCode: OpCode) {}
    fun logRead(address: Int, addressValue: Long, mode: Byte, result: Long) {}
    fun logWrite(address: Int, addressValue: Long, mode: Byte, value: Long) {}
    fun logEndLine() {}

    fun execute() {
        var instructionPointer = 0
        while(instructionPointer < program.size) {
            instructionPointer = executeInstruction(instructionPointer)
        }
    }
}


open class InteractiveRuntime(
    _program: Program,
    memorySize: Int = _program.size,
    val debug: Boolean = false
) : Runtime {
    override val program: Program = LongArray(memorySize) { i ->
        if (i < _program.size) _program[i]
        else 0
    }

    override var relativeBase: Int = 0

    override fun readInput(): Long {
        print("INPUT << ")
        return readLine()!!.toLong()
    }

    override fun writeOutput(value: Long) {
        if (debug) {
            print("PRINT($value)")
        } else {
            println("OUTPUT >> $value")
        }
    }

    override fun logOpcode(address: Int, opCode: OpCode) {
        if (debug) print("L$address: ${opCode.code} ")
    }

    override fun logRead(address: Int, addressValue: Long, mode: Byte, result: Long) {
        if (debug) print("${mode.toModeChar()}$address/$addressValue=>$result ")
    }

    override fun logWrite(address: Int, addressValue: Long, mode: Byte, value: Long) {
        if (debug) print("${mode.toModeChar()}$address/$addressValue:=$value ")
    }

    override fun logEndLine() { if (debug) println() }

    private fun Byte.toModeChar(): String = when(this) {
        OpCode.MODE_POS -> "#"
        OpCode.MODE_IMM -> "!"
        OpCode.MODE_REL -> "%"
        else -> "?"
    }
}
