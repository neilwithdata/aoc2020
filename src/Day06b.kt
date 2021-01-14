import java.io.File

// convenience method to grab contiguous groups of lines
fun groups() = sequence<List<String>> {
    val lines = File("data/day06_input.txt").readLines()
    val group = mutableListOf<String>()

    for ((indx, line) in lines.withIndex()) {
        if (line.isBlank()) {
            yield(group)
            group.clear()
        } else {
            group.add(line)
            if (indx == lines.lastIndex) {
                yield(group)
            }
        }
    }
}

fun main() {
    var count = 0
    for (group in groups()) {
        var answers = setOf(*group.first().toCharArray().toTypedArray())

        for (line in group.drop(1)) {
            answers = answers.intersect(line.asIterable())
        }

        count += answers.size
    }

    println("total count: $count")
}
