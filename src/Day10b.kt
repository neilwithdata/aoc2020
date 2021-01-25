import java.io.File

// for each value in the array, maintain a separate array holding the distinct ways that value can be reached
// and walk through summing as you go (with appropriate conditions)

fun main() {
    val values = File("data/day10_input.txt")
        .readLines()
        .map { it.toInt() }
        .toMutableList()
        .apply {
            add(0)
            add(maxOrNull()!! + 3)
        }
        .sorted()


    val distinctCounts = LongArray(values.size)
    distinctCounts[0] = 1

    for ((index, value) in values.withIndex().drop(1)) {
        var sum = 0L
        for (j in 1..3) {
            sum += (index - j)
                .takeIf { it >= 0 && value - values[it] <= 3 }
                ?.let { distinctCounts[it] }
                ?: 0
        }

        distinctCounts[index] = sum
    }

    println(values)
    println(distinctCounts.joinToString(prefix = "[", postfix = "]"))
}