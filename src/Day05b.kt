import java.io.File

fun main() {
    val lines = File("data/day05_input.txt").readLines()

    val min: Int
    val max: Int
    val sum: Int

    lines.map { calculateSeatId(it) }
        .also { seatIds ->
            sum = seatIds.sum()
            min = seatIds.minOrNull()!!
            max = seatIds.maxOrNull()!!
        }

    println("The missing seat ID is ${(min..max).sum() - sum}")
}

private fun calculateSeatId(code: String): Int {
    val row = code.take(7).fold(0 until 128) { range, c ->
        val midpoint = (range.first + range.last + 1) / 2
        if (c == 'F') range.first until midpoint else midpoint until range.last + 1
    }.first

    val col = code.takeLast(3).fold(0 until 8) { range, c ->
        val midpoint = (range.first + range.last + 1) / 2
        if (c == 'L') range.first until midpoint else midpoint until range.last + 1
    }.first

    return row * 8 + col
}