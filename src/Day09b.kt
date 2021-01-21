import java.io.File
import java.math.BigInteger
import java.util.*

fun main() {
    val target = 21806024.toBigInteger()
    val window: Queue<BigInteger> = LinkedList()

    val nums = File("data/day09_input.txt")
        .readLines()
        .map { it.toBigInteger() }

    var endIndex = 0
    while (true) {
        while ((window.sumOf { it } + nums[endIndex]) <= target) {
            window.add(nums[endIndex])
            endIndex++
        }

        if (window.sumOf { it } == target) {
            println("Sum of min + max in window is ${window.minOf { it } + window.maxOf { it }}")
            break
        }

        window.remove()
    }
}