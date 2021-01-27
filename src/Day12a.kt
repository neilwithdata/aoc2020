import java.io.File
import java.lang.Math.toRadians
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

// A couple of convenience extension functions for adding and scalar multiplication of vectors
data class Vector(val x: Int, val y: Int)

operator fun Vector.plus(element: Vector) = Vector(this.x + element.x, this.y + element.y)
operator fun Vector.times(element: Int) = Vector(this.x * element, this.y * element)

object Ship {
    private var angleDegrees = 0
    var position = Vector(0, 0)

    fun performAction(instruction: Char, value: Int) {
        when (instruction) {
            'N' -> position += Vector(0, 1) * value
            'S' -> position += Vector(0, -1) * value
            'E' -> position += Vector(1, 0) * value
            'W' -> position += Vector(-1, 0) * value
            'L' -> angleDegrees = (angleDegrees + value) % 360
            'R' -> angleDegrees = (angleDegrees - value) % 360
            'F' -> {
                val directionVector = Vector(
                    cos(toRadians(angleDegrees.toDouble())).roundToInt(),
                    sin(toRadians(angleDegrees.toDouble())).roundToInt()
                )
                position += directionVector * value
            }
        }
    }

    fun distance(): Int {
        return abs(position.x) + abs(position.y)
    }
}

fun main() {
    File("data/day12_input.txt")
        .readLines()
        .forEach { line ->
            Ship.performAction(line[0], line.substring(1).toInt())
        }

    println(Ship.distance())
    println("position is ${Ship.position}")
}