import java.io.File

fun main() {
    val lines = File("data/day18_input.txt").readLines()
    println(lines.sumOf { evaluateWithGroups(it) })
}

private fun evaluateWithGroups(expression: String): Long {
    var simplified = expression

    while (true) {
        val startIndex = simplified.lastIndexOf("(")
        if (startIndex != -1) {
            val endIndex = simplified.indexOf(")", startIndex)
            val group = simplified.substring(startIndex + 1, endIndex)
            simplified = simplified.replaceRange(startIndex, endIndex + 1, evaluate(group).toString())
        } else {
            return evaluate(simplified)
        }
    }
}

private fun evaluate(expression: String): Long {
    var tokens = expression.split(" ").toMutableList()

    while (true) {
        val plusIndex = tokens.indexOf("+")
        if (plusIndex != -1) {
            val result = tokens[plusIndex - 1].toLong() + tokens[plusIndex + 1].toLong()
            tokens[plusIndex - 1] = result.toString()
            tokens.removeAt(plusIndex)
            tokens.removeAt(plusIndex)
        } else {
            break
        }
    }

    // now multiply all the numerical tokens remaining
    return tokens.fold(1L) { acc, s ->
        s.toLongOrNull()?.let {
            acc * it
        } ?: acc
    }
}

