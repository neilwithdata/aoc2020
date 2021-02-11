const val TARGET = 30_000_000

fun main() {
    val numbers = listOf(13, 16, 0, 12, 15, 1)

    val indexMap = numbers
        .dropLast(1)
        .withIndex()
        .map {
            it.value to it.index
        }
        .toMap()
        .toMutableMap()

    var prev = numbers.last()

    for (prevIndex in numbers.lastIndex..TARGET - 2) {
        val next = if (prev in indexMap) {
            prevIndex - indexMap[prev]!!
        } else {
            0
        }

        indexMap[prev] = prevIndex
        prev = next
    }

    println("final output: $prev")
}
