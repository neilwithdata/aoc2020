import java.io.File

fun main() {
    val values = File("data/day01_input.txt")
        .readLines()
        .map { it.toInt() }

    val required = mutableSetOf<Int>()

    for (n in values) {
        if (n in required) {
            println("The answer is ${n * (2020 - n)}")
            break
        } else {
            required.add(2020 - n)
        }
    }
}