import java.io.File

fun main() {
    val lines = File("data/day02_input.txt").readLines()

    val pattern = """(\d+)-(\d+) (\w): (\w+)""".toRegex()

    lines.count { line ->
        val (min, max, letter, password) = pattern.find(line)!!.destructured
        validatePassword(letter[0], min.toInt()..max.toInt(), password)
    }.also { count -> println(count) }
}

private fun validatePassword(letter: Char, range: IntRange, password: String) =
    password.count { it == letter } in range