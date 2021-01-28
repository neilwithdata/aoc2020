import java.io.File
import kotlin.math.abs

// A couple of convenience extension functions for adding and scalar multiplication of vectors
data class Vector(val x: Int, val y: Int)

operator fun Vector.plus(element: Vector) = Vector(this.x + element.x, this.y + element.y)
operator fun Vector.times(element: Int) = Vector(this.x * element, this.y * element)

object Ship {
    var position = Vector(0, 0)
    private var waypointOffset = Vector(10, 1)

    fun performAction(instruction: Char, value: Int) {
        when (instruction) {
            'N' -> waypointOffset += Vector(0, value)
            'S' -> waypointOffset += Vector(0, -value)
            'E' -> waypointOffset += Vector(value, 0)
            'W' -> waypointOffset += Vector(-value, 0)
            'L' -> {
                repeat(value / 90) {
                    waypointOffset = Vector(-waypointOffset.y, waypointOffset.x)
                }
            }
            'R' -> {
                repeat(value / 90) {
                    waypointOffset = Vector(waypointOffset.y, -waypointOffset.x)
                }
            }
            'F' -> {
                position += waypointOffset * value
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