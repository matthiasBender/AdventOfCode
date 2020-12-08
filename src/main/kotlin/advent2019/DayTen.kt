package advent2019

import kotlin.math.absoluteValue

object DayTen {
    val test0 = """
        .#..#
        .....
        #####
        ....#
        ...##
    """.trimIndent()
    val test1 = """
        ......#.#.
        #..#.#....
        ..#######.
        .#.#.###..
        .#..#.....
        ..#....#.#
        #..#....#.
        .##.#..###
        ##...#..#.
        .#....####
    """.trimIndent()
    val test2 = """
        #.#...#.#.
        .###....#.
        .#....#...
        ##.#.#.#.#
        ....#.#.#.
        .##..###.#
        ..#...##..
        ..##....##
        ......#...
        .####.###.
    """.trimIndent()
    val test3 = """
        .#..#..###
        ####.###.#
        ....###.#.
        ..###.##.#
        ##.##.#.#.
        ....###..#
        ..#.#..#.#
        #..#.#.###
        .##...##.#
        .....#.#..
    """.trimIndent()
    val test4 = """
        .#..##.###...#######
        ##.############..##.
        .#.######.########.#
        .###.#######.####.#.
        #####.##.#.##.###.##
        ..#####..#.#########
        ####################
        #.####....###.#.#.##
        ##.#################
        #####.##.###..####..
        ..######..##.#######
        ####.##.####...##..#
        .#####..#.######.###
        ##...#.##########...
        #.##########.#######
        .####.#.###.###.#.##
        ....##.##.###..#####
        .#.#.###########.###
        #.#.#.#####.####.###
        ###.##.####.##.#..##
    """.trimIndent()
    val taskInput = """
        .###..#######..####..##...#
        ########.#.###...###.#....#
        ###..#...#######...#..####.
        .##.#.....#....##.#.#.....#
        ###.#######.###..##......#.
        #..###..###.##.#.#####....#
        #.##..###....#####...##.##.
        ####.##..#...#####.#..###.#
        #..#....####.####.###.#.###
        #..#..#....###...#####..#..
        ##...####.######....#.####.
        ####.##...###.####..##....#
        #.#..#.###.#.##.####..#...#
        ..##..##....#.#..##..#.#..#
        ##.##.#..######.#..#..####.
        #.....#####.##........#####
        ###.#.#######..#.#.##..#..#
        ###...#..#.#..##.##..#####.
        .##.#..#...#####.###.##.##.
        ...#.#.######.#####.#.####.
        #..##..###...###.#.#..#.#.#
        .#..#.#......#.###...###..#
        #.##.#.#..#.#......#..#..##
        .##.##.##.#...##.##.##.#..#
        #.###.#.#...##..#####.###.#
        #.####.#..#.#.##.######.#..
        .#.#####.##...#...#.##...#.
    """.trimIndent()

    data class Coordinates(
        val x: Int,
        val y: Int
    ) {
        operator fun plus(c: Coordinates): Coordinates = Coordinates(x = x + c.x, y = y + c.y)
        operator fun minus(c: Coordinates): Coordinates = Coordinates(x = x - c.x, y = y - c.y)
        operator fun times(c: Coordinates): Coordinates = Coordinates(x = x * c.x, y = y * c.y)
        operator fun div(c: Coordinates): Pair<Double, Double> = try {
            specialDiv(x, c.x) to specialDiv(y, c.y)
        } catch (e: IllegalArgumentException) {
            0.0 to 0.0
        }

        private fun specialDiv(a: Int, b: Int): Double =
            if (b == 0 && a == 0) {
                throw IllegalArgumentException()
            }
            else a.toDouble() / b

        val length = x.absoluteValue + y.absoluteValue
        
        val quadrant = when {
            x >= 0 && y > 0 -> 1
            x >= 0 && y <= 0 -> 0
            x < 0 && y <= 0 -> 3
            x < 0 && y > 0 -> 2
            else -> throw IllegalStateException("This is impossible to happen!")
        }

        val pitch: Double = y.toDouble() / x
    }

    data class AsteroidMap(
        val entities: List<List<Char>>
    ) {
        val height = entities.size
        val width = entities[0].size

        constructor(field: String) : this(
            field.split('\n')
                .filter { it.isNotBlank() }
                .map { it.toList() }
        )

        fun toCoordinates(): List<Coordinates> = entities.asSequence()
            .mapIndexed { y, row ->
                row.asSequence()
                    .mapIndexed {  x, char ->
                        Coordinates(x = x, y = y) to char
                    }
            }
            .flatten()
            .filter { (_, c) -> c == '#' }
            .map(Pair<Coordinates, Char>::first)
            .toList()
    }

    fun Coordinates.findVisible(targets: List<Coordinates>): List<Coordinates> {
        val fixed = targets.asSequence()
            .map { it - this }
            .filter { it.x != 0 || it.y != 0 }
            .toList()
        return fixed.filterNot { c1 ->
            fixed.asSequence()
                .filter { c2 -> c2.length < c1.length }
                .filter { c2 ->
                    val m = c1 * c2
                    m.x >= 0 && m.y >= 0
                }
                .map { c2 -> (c1 / c2) }
                .any { (d1, d2) -> d1 == d2 }
        }
    }

    fun List<Coordinates>.searchOptimalPosition(): Pair<Coordinates, Int> {
       return  map { it to it.findVisible(this).size }
            .maxByOrNull { it.second }!!
    }
    
    fun orderQuadrant(c1: Coordinates, c2: Coordinates): Int {
        return c1.quadrant - c2.quadrant
    }

    private fun compare(c1: Coordinates, c2: Coordinates): Int {
        val q = c1.quadrant - c2.quadrant
        return if (q == 0) {
            c1.pitch.compareTo(c2.pitch)
        } else q
    }
    

    fun start() {
        val map = AsteroidMap(taskInput).toCoordinates()
        val (position, count) = map.searchOptimalPosition()
        println("RESULT: $position => $count")

        val targets = position.findVisible(map)
        val result = targets.sortedWith(Comparator(this::compare))[199] + position
        println("RESULT: $result")
    }
}

fun main(vararg args: String) {
    DayTen.start()
}