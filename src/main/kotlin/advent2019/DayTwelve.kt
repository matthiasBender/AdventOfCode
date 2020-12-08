package advent2019

import kotlin.math.absoluteValue

object DayTwelve {
    private val test1 = listOf(
        Moon(Vector(-1, 0, 2)),
        Moon(Vector(2, -10, -7)),
        Moon(Vector(4, -8, 8)),
        Moon(Vector(3, 5, -1))
    )
    private val test2 = listOf(
        Moon(Vector(-8, -10, 0)),
        Moon(Vector(5, 5, 10)),
        Moon(Vector(2, -7, 3)),
        Moon(Vector(9, -8, -3))
    )
    private val input = listOf(
        Moon(Vector(13, 9, 5)),
        Moon(Vector(8, 14, -2)),
        Moon(Vector(-5, 4, 11)),
        Moon(Vector(2, -6, 1))
    )

    data class Vector(
        val x: Int,
        val y: Int,
        val z: Int
    ) {
        val energy = x.absoluteValue + y.absoluteValue + z.absoluteValue

        operator fun plus(v: Vector) = Vector(
            x = x + v.x,
            y = y + v.y,
            z = z + v.z
        )

        operator fun minus(v: Vector) = Vector(
            x = x - v.x,
            y = y - v.y,
            z = z - v.z
        )

        fun sign() = Vector(
            x = x.sign(),
            y = y.sign(),
            z = z.sign()
        )

        companion object {
            val zero = Vector(0, 0, 0)
            val one = Vector(1, 1, 1)
        }
    }

    fun Int.sign() = when {
        this > 0 -> 1
        this == 0 -> 0
        else -> -1
    }

    data class Moon(
        val position: Vector,
        val velocity: Vector = Vector.zero
    ) {
        val energy = position.energy.toLong() * velocity.energy

        fun move(): Moon = copy(position = position + velocity)

        fun pullTowards(moon: Moon): Vector = (moon.position - position).sign()
    }

    fun pullByGravity(moon1: Moon, moon2: Moon): Pair<Moon, Moon> {
        val distance = (moon1.position - moon2.position).sign()
        val new1 = moon1.copy(
            velocity = moon1.velocity - distance
        )
        val new2 = moon2.copy(
            velocity = moon2.velocity + distance
        )
        return new1 to new2
    }

    fun List<Moon>.simulateStep(): List<Moon> = this.map { moon ->
        val velocity = moon.velocity +
                this.map{ moon2 -> moon.pullTowards(moon2) }.reduce(Vector::plus)
        moon.copy(
            velocity = velocity,
            position = moon.position + velocity
        )
    }

    fun List<Moon>.calculateEnergy(): Long = map(Moon::energy).sum()

    fun List<Moon>.simulate(steps: Int): List<Moon> {
        var moons = this
        repeat(steps) {
            moons = moons.simulateStep()
        }
        return moons
    }

//    fun List<Moon>.

    fun start() {
        val result = input.simulate(1000)
        println("RESULT: ${result.calculateEnergy()}")
    }
}

fun main(vararg args: String) {
//    DayTwelve.start()
    val target = 23
    val range1 = 5 until target step 5
    val range2 = (1..(target / 5)).map{ it * 5 }
    range1.forEach { println(it) }
    range2.forEach { println(it) }

}