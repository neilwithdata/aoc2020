import java.io.File

fun main() {
    val values = File("data/day01_input.txt")
        .readLines()
        .map { it.toInt() }

    val required = mutableMapOf<Int, Int>()

    for ((indx, n) in values.withIndex()) {
        if (n in required) {
            println("The answer is ${n * required[n]!!}")
            break
        } else {
            for (i in 0 until indx) {
                required[2020 - (n + values[i])] = n * values[i]
            }
        }
    }
}