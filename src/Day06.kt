import java.io.File

fun main() {
    val lines = File("data/day06_input.txt").readLines()
    val answers = mutableSetOf<Char>()

    var count = 0

    // process all the groups
    for (line in lines) {
        if (line.isBlank()) {
            count += answers.size
            answers.clear()
        } else {
            answers.addAll(line.asIterable())
        }
    }

    // also process the final group
    count += answers.size
    println("total count: $count")
}
