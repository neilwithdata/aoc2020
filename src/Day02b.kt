import java.io.File

fun main() {
    val lines = File("data/day02_input.txt").readLines()

    val pattern = """(\d+)-(\d+) (\w): (\w+)""".toRegex()

    lines.count { line ->
        val (pos1, pos2, letter, password) = pattern.find(line)!!.destructured
        validatePassword(letter[0], pos1.toInt(), pos2.toInt(), password)
    }.also { count -> println(count) }
}

private fun validatePassword(letter: Char, pos1: Int, pos2: Int, password: String) =
    listOf(pos1, pos2).count { pos ->
        password.getOrNull(pos - 1) == letter
    } == 1