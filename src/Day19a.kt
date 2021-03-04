import java.io.File

fun main() {
    val inputFile = File("data/day19_input.txt")

    val rules = inputFile
        .readLines()
        .takeWhile { it.isNotEmpty() }
        .sortedBy { it.substringBefore(':').toInt() }
        .map { it.substringAfter(':').trim() }
        .toTypedArray()

    val regex = Regex(resolveRule(0, rules))

    println(inputFile
        .readLines()
        .drop(rules.size + 1)
        .count {
            regex.matchEntire(it) != null
        })
}

private fun resolveRule(ruleId: Int, rules: Array<String>): String {
    val ruleString = rules[ruleId]

    return if (ruleString.contains('"')) {
        ruleString[ruleString.indexOf('"') + 1].toString()
    } else {
        ruleString
            .split(" ")
            .joinToString(separator = "", prefix = "(", postfix = ")") {
                when (it) {
                    "|" -> ")|("
                    else -> "(${resolveRule(it.toInt(), rules)})"
                }
            }
    }
}