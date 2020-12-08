package advent2020

import java.io.File

object Day07 {
    private val testRule1 = "posh green bags contain no other bags."
    private val testRule2 = "plaid gold bags contain 3 pale fuchsia bags, 5 dull lime bags, 5 wavy chartreuse bags, 5 dim tomato bags."

    private fun loadData() = File("src/main/resources/advent2020/Day07.txt").readText()
        .split("\n")
        .map { it.parseRule() }
        .associateBy { it.name }

    private fun String.parseRule(): Rule {
        val emptyResult = emptyPattern.find(this)
        return if (emptyResult != null) Rule(emptyResult.groupValues[1], emptyMap())
        else {
            val (name, content) = this.split(" bags contain ")
            val contentMap = content.split(", ").associate {
                val parts = containingPattern.find(it)!!.groupValues
                parts[2] to parts[1].toInt()
            }
            Rule(name, contentMap)
        }
    }

    private val emptyPattern = "(\\w+ \\w+) bags contain no other bags.".toRegex()
    private val containingPattern = "(\\d+) (\\w+ \\w+) bag(|s)(|.)".toRegex()

    data class Rule(
        val name: String,
        val content: Map<String, Int>
    )

    private fun Map<String, Rule>.findAllBagsContaining(target: String): List<Rule> =
        values.filter {
            target in findAllContents(it.name).map(Rule::name).toSet()
        }

    private fun Map<String, Rule>.findAllContents(target: String): Sequence<Rule> = (this[target]?.content ?: emptyMap()).asSequence()
        .mapNotNull { (name, _) -> this[name] }
        .map { rule ->
            sequenceOf(rule) +
            rule.content.keys.asSequence()
                .map {
                    sequenceOf(this[it]!!) +
                    findAllContents(it).asSequence() }
                .flatten()
        }
        .flatten()

    private fun Map<String, Rule>.countContentsOf(target: String): Long {
        val contents = this[target]?.content ?: emptyMap()
        return contents
            .map { (name, number) ->
                number.toLong() + number * countContentsOf(name)
            }
            .sum()
    }

    fun start() {
        val rules = loadData()

        println("RESULT: ${rules.findAllBagsContaining("shiny gold").size}")
        println("RESULT: ${rules.countContentsOf("shiny gold")}")
    }
}

fun main() {
    Day07.start()
}