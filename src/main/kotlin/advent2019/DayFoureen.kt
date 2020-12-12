package advent2019

import java.io.File

object DayFoureen {
    val example = """
        10 ORE => 10 A
        1 ORE => 1 B
        7 A, 1 B => 1 C
        7 A, 1 C => 1 D
        7 A, 1 D => 1 E
        7 A, 1 E => 1 FUEL
    """.trimIndent().parseFile()

    private fun loadData() = File("src/main/resources/advent2019/DayFourteen.txt").readText().parseFile()

    data class Requirement(
        val amount: Int,
        val chemical: String
    ) {
        override fun toString() = "$amount $chemical"
    }

    data class Reaction (
        val produced: Requirement,
        val requirements: List<Requirement>
    ) {
        override fun toString() = requirements.joinToString() + " => $produced"
    }

    private fun String.parseFile(): List<Reaction> = split("\n")
        .filter { it.isNotBlank() }
        .map { it.parseLine() }

    private fun String.parseLine(): Reaction {
        val (req, produced) = split(" => ")
        val reqs = req.split(", ")
            .map { it.parseRequirement() }
        return Reaction(produced.parseRequirement(), reqs)
    }

    private fun String.parseRequirement(): Requirement {
        val (amount, name) = split(" ")
        return Requirement(amount.toInt(), name)
    }

    private fun List<Reaction>.byChemical() =
        associateBy { it.produced.chemical }

    private fun Map<String, Reaction>.produce(requirement: Requirement): List<Requirement> {
        val amount = requirement.amount
        val reaction = get(requirement.chemical) ?: return listOf(requirement)
        val producedAmount = reaction.produced.amount
        val scale = amount / producedAmount + (if (amount % producedAmount == 0) 0 else 1)
        println("target = $requirement --> $reaction -- $scale")
        return reaction.requirements
            .map { Requirement(it.amount * scale, it.chemical) }
            .map { produce(it) }
            .flatten()
    }

    private fun Map<String, Reaction>.produce2(requirement: Requirement): Pair<List<Requirement>, Int> {
        val amount = requirement.amount
        val reaction = get(requirement.chemical) ?: return listOf(requirement) to 0
        val producedAmount = reaction.produced.amount
        val overProduced = amount % producedAmount
        val scale = amount / producedAmount + (if (overProduced == 0) 0 else 1)

        return reaction.requirements
            .map { Requirement(it.amount * scale, it.chemical) }
            .map { produce2(it).first }
            .flatten() to overProduced
    }

    fun start() {
        val index = example.byChemical()
        val target = Requirement(1, "FUEL")
        val leaf = "ORE"
        println(index.produce2(target))
    }

    fun <T: Any, V: Any> higherLevel(input: T, mapper: (T) -> V): V {
        return mapper(input)
    }
}

fun main() {
    println(DayFoureen.higherLevel(123) {
        "123"
    })
//    DayFoureen.start()
}