import java.io.File

fun main() {
    val lines = File("data/day05_input.txt").readLines()
    var highest = lines.first()

    for (line in lines) {
        for ((i, c) in line.withIndex()) {
            if ((c == 'B' && highest[i] == 'F') || (c == 'R' && highest[i] == 'L')) {
                highest = line // New highest found - can go to next line
                break
            } else if ((c == 'F' && highest[i] == 'B') || (c == 'L' && highest[i] == 'R')) {
                break // Always going to be lower - can go to next line
            }
        }
    }

    println("highest: $highest has seat ID: ${calculateSeatId(highest)}")
}

fun calculateSeatId(code: String): Int {
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