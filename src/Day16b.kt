import java.io.File

fun main() {
    val lines = File("data/day16_input.txt").readLines()

    val fields = getFieldRanges(lines)

    val taken = mutableSetOf<String>()
    val results = MutableList(fields.size) { "" }

    buildCandidates(lines, fields)
        .withIndex()
        .sortedBy { it.value.size }
        .forEach { (index, set) ->
            val possibles = set subtract taken
            require(possibles.size == 1)

            taken.add(possibles.first())
            results[index] = possibles.first()
        }

    val departureValues = getDepartureValues(lines, results)
    println(departureValues.fold(1L) { acc, i ->
        acc * i
    })

}

private fun getDepartureValues(lines: List<String>, candidates: List<String>): List<Int> {
    val ticketValues = lines[lines.indexOf("your ticket:") + 1]
        .split(',')
        .map { it.toInt() }

    return ticketValues
        .zip(candidates)
        .filter { it.second.startsWith("departure") }
        .map { it.first }
}

private fun buildCandidates(lines: List<String>, fields: Map<String, Set<Int>>): List<Set<String>> {
    val fullRange = fields.values.fold(setOf<Int>()) { base, next ->
        base union next
    }

    // Start with all fields being possible in every position and then reduce
    val candidates = MutableList(fields.size) { fields.keys }

    lines
        .drop(lines.indexOf("nearby tickets:") + 1)
        .forEach { line ->
            val values = line
                .split(',')
                .map { it.trim().toInt() }

            if (values.all { it in fullRange }) {
                for ((position, value) in values.withIndex()) {
                    candidates[position] = candidates[position].filter { fieldName ->
                        value in fields[fieldName]!!
                    }.toSet()
                }
            }
        }

    return candidates
}

private fun getFieldRanges(lines: List<String>): Map<String, Set<Int>> {
    val rangeRegex = Regex("""(\d+)-(\d+)""")

    return lines
        .takeWhile { it.isNotEmpty() }
        .map { line ->
            val fieldName = line.substringBefore(':')

            val values = rangeRegex
                .findAll(line.substringAfter(':'))
                .fold(setOf<Int>()) { range, matchResult ->
                    val (min, max) = matchResult.destructured
                    range union (min.toInt()..max.toInt()).toSet()
                }

            fieldName to values
        }
        .toMap()
}