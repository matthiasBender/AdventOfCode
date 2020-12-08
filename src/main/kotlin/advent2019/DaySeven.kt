package advent2019

import advent2019.intcode.Program
import advent2019.intcode.Runtime
import advent2019.intcode.executeInstruction
import java.util.*
import kotlin.collections.ArrayDeque

/**
 * Opcode:
 * 99 -> end program
 * 1, x, y, z -> X[z] = X[x] + X[y]
 * 2, x, y, z -> X[z] = X[x] * X[y]
 */
object DaySeven {
    // max = 43210 (phase settings => 4,3,2,1,0)
    val testProgram1 = arrayOf(
        3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0
    )
    // max = 54321 (phase settings => 0,1,2,3,4)
    val testProgram2 = arrayOf(
        3,23,3,24,1002,24,10,24,1002,23,-1,23,
        101,5,23,23,1,24,23,23,4,23,99,0,0
    )
    // max = 65210 (phase settings => 1,0,4,3,2)
    val testProgram3 = arrayOf(
        3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,
        1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0
    )

    val task1 = longArrayOf(
        3,8,1001,8,10,8,105,1,0,0,21,38,47,64,89,110,191,272,353,434,99999,3,9,101,4,9,9,102,3,9,9,101,5,9,9,4,9,99,3,9,1002,9,5,9,4,9,99,3,9,101,2,9,9,102,5,9,9,1001,9,5,9,4,9,99,3,9,1001,9,5,9,102,4,9,9,1001,9,5,9,1002,9,2,9,1001,9,3,9,4,9,99,3,9,102,2,9,9,101,4,9,9,1002,9,4,9,1001,9,4,9,4,9,99,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,99,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,99,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,99
    )

    class AmplifierRuntime(
        private val _program: Program,
        val sequence: Int,
        val previousOutput: Long = 0,
        override var relativeBase: Int = 0
    ) : Runtime {
        override val program = _program.copyOf()
        private var sequenceRead = false
        private var lastOutput: Long? = null
        val output get() = lastOutput!!

        override fun readInput(): Long =
            if (sequenceRead) {
                println("READ PREVIOUS OUTPUT << $previousOutput")
                previousOutput
            } else {
                println("READ SEQUENCE << $sequence")
                sequenceRead = true
                sequence.toLong()
            }

        override fun writeOutput(value: Long) {
            println("OUTPUT: $value")
            lastOutput = value
        }

        fun followUpAmp(sequence: Int): AmplifierRuntime =
            AmplifierRuntime(
                _program,
                sequence,
                previousOutput = output
            )
    }

    class LinkedAmplifiers(
        program: Program,
        private val sequence: AmpSequence
    ) {
        private val signals = listOf(
            ArrayDeque<Long>().apply {
                addLast(sequence.s1.toLong())
                addLast(0)
            },
            ArrayDeque<Long>().apply {
                addLast(sequence.s2.toLong())
            },
            ArrayDeque<Long>().apply {
                addLast(sequence.s3.toLong())
            },
            ArrayDeque<Long>().apply {
                addLast(sequence.s4.toLong())
            },
            ArrayDeque<Long>().apply {
                addLast(sequence.s5.toLong())
            }
        )
        private val amplifiers = listOf(
            LinkedRuntime(program.copyOf(), signals[0], signals[1], 0),
            LinkedRuntime(program.copyOf(), signals[1], signals[2], 1),
            LinkedRuntime(program.copyOf(), signals[2], signals[3], 2),
            LinkedRuntime(program.copyOf(), signals[3], signals[4], 3),
            LinkedRuntime(program.copyOf(), signals[4], signals[0], 4)
        )

        fun calculateResult(): Long {
            while (amplifiers.any { it.canContinue }) {
                amplifiers.forEachIndexed { _, linkedRuntime ->
                    linkedRuntime.continueProcess()
                }
            }
            return signals[0].last()
        }

        private inner class LinkedRuntime(
            override val program: Program,
            private val inputQueue: ArrayDeque<Long>,
            private val outputQueue: ArrayDeque<Long>,
            private val index: Int
        ) : Runtime {
            override var relativeBase: Int = 0
            private var pointer = 0

            val isDone get() = pointer >= program.size
            val canContinue get() = !isDone && inputQueue.isNotEmpty()

            override fun readInput(): Long = inputQueue.removeFirst()

            override fun writeOutput(value: Long) {
                outputQueue.addLast(value)
            }

            fun continueProcess() {
                if (canContinue) {
                    while (!isDone) {
                        try {
                            pointer = this.executeInstruction(pointer)
                        } catch (e: NoSuchElementException) {
                            return
                        }
                    }
                }
            }
        }
    }

    data class AmpSequence(
        val s1: Int,
        val s2: Int,
        val s3: Int,
        val s4: Int,
        val s5: Int,
        val offset: Int = 0
    ) {
        fun isValid(): Boolean {
            val range = offset..(offset + 4)
            val inRange = s1 in range && s2 in range && s3 in range && s4 in range && s5 in range
            val unequal = s1 != s2 && s1 != s3 && s1 != s4 && s1 != s5
                    && s2 != s3 && s2 != s4 && s2 != s5
                    && s3 != s4 && s3 != s5 && s4 != s5
            return inRange && unequal
        }

        override fun toString() = "S($s1$s2$s3$s4$s5)"
    }

    fun generateSequences(offset: Int = 0): Sequence<AmpSequence> {
        val range = (offset)..(offset + 4)
        return range.asSequence().map { s1 ->
            range.asSequence().map { s2 ->
                range.asSequence().map { s3 ->
                    range.asSequence().map{ s4 ->
                        range.asSequence().map { s5 ->
                            AmpSequence(s1, s2, s3, s4, s5, offset)
                        }
                    }.flatten()
                }.flatten()
            }.flatten()
        }
            .flatten()
            .filter(AmpSequence::isValid)
    }

    fun evaluateSequence(seq: AmpSequence, program: Program): Long {
        val r1 = AmplifierRuntime(program, seq.s1)
        r1.execute()
        val r2 = r1.followUpAmp(seq.s2)
        r2.execute()
        val r3 = r2.followUpAmp(seq.s3)
        r3.execute()
        val r4 = r3.followUpAmp(seq.s4)
        r4.execute()
        val r5 = r4.followUpAmp(seq.s5)
        r5.execute()
        return r5.output
    }

    fun findSequenceWithLargestOutput(program: Program): Pair<AmpSequence, Long> =
        generateSequences(offset = 0)
            .map {  seq -> seq to evaluateSequence(seq, program) }
            .maxByOrNull(Pair<AmpSequence, Long>::second)!!

    fun findBestFeedbackSequence(program: Program): Pair<AmpSequence, Long> =
        generateSequences(offset = 5)
            .map { it to LinkedAmplifiers(program, it).calculateResult() }
            .maxByOrNull(Pair<AmpSequence, Long>::second)!!
}

fun main(vararg args: String) {
    with (DaySeven) {
        println(findSequenceWithLargestOutput(task1))

        println(findBestFeedbackSequence(task1))
    }
}
