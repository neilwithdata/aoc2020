import java.io.File

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

    var oneDiffCount = 0
    var threeDiffCount = 0
    for (i in 1 until values.size) {
        when (values[i] - values[i - 1]) {
            1 -> oneDiffCount++
            3 -> threeDiffCount++
        }
    }

    println("oneDiffCount: $oneDiffCount, threeDiffCount: $threeDiffCount, product: ${oneDiffCount * threeDiffCount}")
}