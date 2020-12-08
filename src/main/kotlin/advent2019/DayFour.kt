package advent2019

fun exp(ex: Int): Int = (1..ex+1).reduce { acc, _ -> acc * 10 }

operator fun Int.get(index: Int): Int = this % exp(index + 1) / exp(index)

fun Int.splitDigits(): Array<Int> = arrayOf(
    this[5], this[4], this[3], this[2], this[1], this[0]
)

fun Array<Int>.digitsIncrease(): Boolean {
    for (i in 1 until this.size) {
        if (this[i-1] > this[i]) return false
    }
    return true
}

fun Array<Int>.containsPair(): Boolean {
    for (i in 1 until this.size) {
        if (this[i-1] == this[i]) return true
    }
    return false
}

fun Array<Int>.containsNoTriple(): Boolean {
    for (i in 2 until this.size) {
        if (this[i-1] == this[i] && this[i-2] == this[i]) return false
    }
    return true
}

fun Array<Int>.hasPairWithoutGroup(): Boolean {
    return (this[0] == this[1] && this[1] != this[2]) ||
            (this[0] != this[1] && this[1] == this[2] && this[2] != this[3]) ||
            (this[1] != this[2] && this[2] == this[3] && this[3] != this[4]) ||
            (this[2] != this[3] && this[3] == this[4] && this[4] != this[5]) ||
            (this[3] != this[4] && this[4] == this[5])
}


fun main(vararg args: String) {
    val range = 402328..864247
    val result = range.asSequence()
        .map { it.splitDigits() }
        .filter { it.digitsIncrease() }
        .filter { it.containsPair() }
        .count()
    println("RESULT1: $result")
    val first = range.asSequence()
        .map { it.splitDigits() }
        .filter { it.digitsIncrease() }
        .filter { it.containsPair() }
        .map { it.foldIndexed(0) { index, acc, value ->
            acc + value * exp(it.size - index)
        } }
        .toSet()

    val result2 = range.asSequence()
        .map { it.splitDigits() }
        .filter { it.digitsIncrease() }
        .filter { it.containsPair() }
        .filter { it.hasPairWithoutGroup() }
        .map { it.foldIndexed(0) { index, acc, value ->
            acc + value * exp(it.size - index)
        } }
        .count()
//    println(first - result2)
    println("RESULT2: $result2")
}