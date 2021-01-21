import java.io.File
import java.math.BigInteger
import java.util.*

private const val WINDOW_SIZE = 25

fun main() {
    val window: Queue<BigInteger> = LinkedList()

    val nums = File("data/day09_input.txt")
        .readLines()
        .map { it.toBigInteger() }

    window.addAll(nums.take(WINDOW_SIZE))
    for (n in nums.drop(WINDOW_SIZE)) {
        if (!window.canSumTo(n)) {
            println("can't make $n from the current window")
            break
        }

        window.remove()
        window.add(n)
    }
}

private fun Collection<BigInteger>.canSumTo(n: BigInteger): Boolean {
    val required = mutableSetOf<BigInteger>()

    for (curr in this) {
        if (curr in required)
            return true
        required.add(n - curr)
    }

    return false
}