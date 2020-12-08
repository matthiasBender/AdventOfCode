package advent2019

/**
 * Opcode:
 * 99 -> end program
 * 1, x, y, z -> X[z] = X[x] + X[y]
 * 2, x, y, z -> X[z] = X[x] * X[y]
 */
object DayTwo {
    val testProgram = arrayOf(
        1,9,10,3,
        2,3,11,0,
        99,30,40,50
    )

    val task1 = arrayOf(
        1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,9,19,1,13,19,23,2,23,9,27,1,6,27,31,2,10,31,35,1,6,35,39,2,9,39,43,1,5,43,47,2,47,13,51,2,51,10,55,1,55,5,59,1,59,9,63,1,63,9,67,2,6,67,71,1,5,71,75,1,75,6,79,1,6,79,83,1,83,9,87,2,87,10,91,2,91,10,95,1,95,5,99,1,99,13,103,2,103,9,107,1,6,107,111,1,111,5,115,1,115,2,119,1,5,119,0,99,2,0,14,0
    )

    fun executeInstruction(
        program: Array<Int>,
        instructionPointer: Int
    ): Int {
        when (program[instructionPointer]) {
            1 -> {
                program.setValue(
                     instructionPointer + 3,
                    program.readValue(instructionPointer + 1) + program.readValue(instructionPointer + 2)
                )
                return instructionPointer + 4
            }
            2 -> {
                program.setValue(
                    instructionPointer + 3,
                    program.readValue(instructionPointer + 1) * program.readValue(instructionPointer + 2)
                )
                return instructionPointer + 4
            }
            99 -> return program.size
            else -> return instructionPointer + 1
        }
    }

    fun Array<Int>.readValue(pointerAddress: Int): Int {
        check(pointerAddress >= 0 && pointerAddress < this.size) { "Position $pointerAddress is not available on the program!" }
        val targetAddress = this[pointerAddress]
        check(targetAddress >= 0 && targetAddress < this.size) { "Position $targetAddress is not available on the program!" }
        return this[targetAddress]
    }

    fun Array<Int>.setValue(pointerAddress: Int, value: Int) {
        check(pointerAddress >= 0 && pointerAddress < this.size) { "Position $pointerAddress is not available on the program!" }
        val targetAddress = this[pointerAddress]
        check(targetAddress >= 0 && targetAddress < this.size) { "Position $targetAddress is not available on the program!" }
        this[targetAddress] = value
    }

    fun execute(program: Array<Int>) {
        var instructionPointer = 0
        while(instructionPointer < program.size) {
            instructionPointer = executeInstruction(program, instructionPointer)
        }
    }

    fun runProgram(input1: Int, input2: Int): Int {
        val program = task1.copyOf()
        program[1] = input1
        program[2] = input2
        execute(program)
        return program[0]
    }

    fun searchTargetOutput(target: Int): Pair<Int, Int>? {
        for (first in 0..100) {
            for (second in 0..100) {
                val result = runProgram(first, second)
//                println("$first, $second -> $result")
                if (result == target) return first to second
                if (result > target) break
            }
        }
        return null
    }
}

fun main(vararg args: String) {
    println("Result: ${DayTwo.runProgram(12, 2)}")
    println(DayTwo.searchTargetOutput(19690720))
}
