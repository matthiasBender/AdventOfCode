package advent2020

import java.io.File

object Day08 {
    private fun loadData() = File("src/main/resources/advent2020/Day08.txt").readText()

    private val testProgram = """
        nop +0
        acc +1
        jmp +4
        acc +3
        jmp -3
        acc -99
        acc +1
        jmp -4
        acc +6
    """.trimIndent()

    enum class Command(
        val code: String
    ) {
        NOP("nop"),
        ACC("acc"),
        JMP("jmp");
    }

    data class Instruction(
        val command: Command,
        val argument: Long
    )

    private val pattern = "(\\w{3}) ((\\+|-)\\d+)".toRegex()
    private val commandMap = Command.values().associateBy(Command::code)

    private fun parseProgram(program: String): List<Instruction> =
        program.split("\n").asSequence()
            .filter { it.isNotBlank() }
            .map { it.toCommand() }
            .toList()

    private fun String.toCommand(): Instruction {
        val groups = pattern.find(this)?.groupValues ?: throw IllegalStateException("Command '$this' cannot be parsed!")
        val command = commandMap[groups[1]] ?: throw IllegalStateException("Command '$this' cannot be parsed! Token '${groups[1]}' is unknown.")
        val argument = groups[2].toLong()
        return Instruction(command, argument)
    }

    private fun List<Instruction>.runUntilRepeated(accumulator: Long = 0L, fail: Boolean = false): Long {
        var instruction = 0
        var acc = accumulator
        val executed = mutableSetOf<Int>()
        while (instruction < size) {
            executed.add(instruction)
            val (command, argument) = this[instruction]
            when (command) {
                Command.NOP -> instruction += 1
                Command.ACC -> {
                    acc += argument
                    instruction += 1
                }
                Command.JMP -> instruction += argument.toInt()
            }
            if (instruction in executed && fail) throw IllegalStateException("Failed to terminate!")
            if (instruction in executed) return acc
        }
        return acc
    }

    fun List<Instruction>.findBrokenInstruction(): Long {
        for (i in 0 until size) {
            val changed  = when (get(i).command) {
                Command.JMP -> replace(i, Command.NOP)
                Command.NOP -> replace(i, Command.JMP)
                else -> continue
            }
            try {
                return changed.runUntilRepeated(fail = true)
            } catch (e: IllegalStateException) {
                continue
            }
        }
        throw IllegalStateException("Program cannot be recovered!")
    }

    fun List<Instruction>.replace(index: Int, command: Command): List<Instruction> {
        val newInstruction = Instruction(command, this[index].argument)
        return slice(0 until index) + listOf(newInstruction) + slice(index + 1 until size)
    }

    fun start() {
        val program = parseProgram(loadData())
        val result = program.runUntilRepeated()
        println("RESULT: $result")

        val fixedResult = program.findBrokenInstruction()
        println("RESULT: $fixedResult")
    }
}

fun main() {
    Day08.start()
}