import java.io.File

fun main() {
    val inputFile = File("data/day19_input.txt")

    val rules = inputFile
        .readLines()
        .takeWhile { it.isNotEmpty() }
        .sortedBy { it.substringBefore(':').toInt() }
        .map {
            when (it) {
                "8: 42" -> "42 | 42 8"
                "11: 42 31" -> "42 31 | 42 11 31"
                else -> it.substringAfter(':').trim()
            }
        }
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

    // hard-code in some specific handling for our self-referential rules
    if (ruleId == 8) {
        return "(${resolveRule(42, rules)})+"
    } else if (ruleId == 11) {
        return (1..4).joinToString(separator = "|") { i ->
            "(${resolveRule(42, rules)}){$i}(${resolveRule(31, rules)}){$i}"
        }
    }

    return if (ruleString.contains('"')) {
        ruleString[ruleString.indexOf('"') + 1].toString()
    } else {
        ruleString
            .split(" ")
            .joinToString(separator = "", prefix = "(", postfix = ")") {
                when (it) {
                    "|" -> ")|("
                    else -> {
                        "(${resolveRule(it.toInt(), rules)})"
                    }
                }
            }
    }
}