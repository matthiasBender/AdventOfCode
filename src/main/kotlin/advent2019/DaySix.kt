package advent2019

import java.io.File

data class Orbit(
    val center: String,
    val satelite: String
) {
    override fun toString() = "$center)$satelite"

    companion object Factory {
        fun fromString(str: String): Orbit {
            val (center, satelite) = str.split(')')
            return Orbit(
                center = center,
                satelite = satelite
            )
        }
    }
}

class Node(
    val name: String,
    val inOrbit: List<Node>,
    private val parentProvider: () -> Node?
) {
    val parent: Node? get() = parentProvider()

    val connections: List<Node> get() =
        parent?.let { inOrbit + listOf(it) } ?: inOrbit

    override fun toString() =
        "($name<${parent?.name}><-$inOrbit)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int = name.hashCode()
}


object DaySix {
    fun loadData(): List<Orbit> {
        val text = File("src/main/resources/advent2019/daysix.txt").readText()
        return text.split("\n").filter(String::isNotBlank).map(Orbit.Factory::fromString)
    }

    fun buildDAGs(orbits: List<Orbit>): List<Node> {
        val connections = orbits.groupBy(Orbit::center, Orbit::satelite)

        val rootNodes = connections.keys - connections.values.flatten().toSet()
        return rootNodes.map { name ->
            buildNode(name, connections) { null }
        }
    }

    private fun buildNode(name: String, connections: Map<String, List<String>>, parentProvider: () -> Node?): Node {
        var result: Node? = null
        return Node(
            name = name,
            inOrbit = connections.getOrDefault(name, emptyList())
                .map { buildNode(it, connections) { result } },
            parentProvider = parentProvider
        ).also { result = it }
    }

    fun countOrbits(node: Node, depth: Int): Int {
        if (node.inOrbit.isEmpty()) return depth
        return depth + node.inOrbit
            .map { countOrbits(it, depth + 1) }
            .sum()
    }

    fun Node.allNames(): Sequence<String> = sequenceOf(this.name) + this.inOrbit.asSequence()
        .map { it.allNames() }.flatten()

    fun Node.search(name: String): Node? =
        if (this.name == name) this
        else inOrbit.asSequence()
            .mapNotNull { it.search(name) }
            .firstOrNull()

    fun Node.findShortestPath(targetName: String): List<Node> {
        val visited = mutableSetOf(this)
        val queue = ArrayDeque<List<Node>>()
        queue.addLast(listOf(this))
        while (queue.isNotEmpty()) {
            val current: List<Node> = queue.removeFirst()
            if (current.first().name == targetName) return current
            queue.addAll(
                current.first().connections.asSequence()
                    .filter { it !in visited }
                    .map {
                        listOf(it) + current
                    }
            )
            visited.addAll(current.first().connections)
        }
        return emptyList()
    }
}

val example = """
    COM)B
    B)C
    C)D
    D)E
    E)F
    B)G
    G)H
    D)I
    E)J
    J)K
    K)L
""".trimIndent().split("\n").filter(String::isNotBlank).map(Orbit.Factory::fromString)

fun main(vararg args: String) {
    with(DaySix) {
        val orbits = loadData()
        val dags = buildDAGs(orbits)
        println("RESULT: ${dags.map{ countOrbits(it, 0) }.sum()}")

        val you = dags[0].search("YOU")!!
        println("Santa: $you")
        val path = you.findShortestPath("SAN").map(Node::name)

        println("PATH: $path")
        println("RESULT: ${path.size - 3}")
    }
}