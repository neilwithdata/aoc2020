import java.io.File

fun main() {
    val lines = File("data/day13_input.txt").readLines()

    val target = lines[0].toInt()
    val earliest = lines[1]
        .split(",")
        .filter { it != "x" }
        .map {
            it.toInt().let { id ->
                id to if (target % id == 0) 0 else id - (target % id)
            }
        }
        .minByOrNull { it.second }!!

    println("earliest: $earliest with product: ${earliest.first * earliest.second}")
}