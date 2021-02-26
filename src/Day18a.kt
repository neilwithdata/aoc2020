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
    var result = 0L
    var operator = "+"

    expression.split(" ").forEachIndexed { index, s ->
        if (index % 2 == 0) {
            if (operator == "+")
                result += s.toLong()
            else
                result *= s.toLong()
        } else {
            operator = s
        }
    }

    return result
}

