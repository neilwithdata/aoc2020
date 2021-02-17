import java.io.File

fun main() {
    val lines = File("data/day16_testing.txt").readLines()

    val rangeRegex = Regex("""(\d+)-(\d+)""")
    var rangeSet = setOf<Int>()

    val rules = lines
        .takeWhile { it.isNotEmpty() }
        .joinToString()

    rangeRegex.findAll(rules).forEach {
        val (min, max) = it.destructured
        rangeSet = rangeSet union (min.toInt()..max.toInt()).toSet()
    }

    val sum = lines
        .dropWhile { it != "nearby tickets:" }
        .drop(1)
        .joinToString()
        .split(',')
        .map { it.trim().toInt() }
        .filter { !rangeSet.contains(it) }
        .sum()

    println(sum)
}