import java.io.File
import java.math.BigInteger

data class Entry(val busId: BigInteger, val offset: BigInteger)

fun main() {
    val lines = File("data/day13_input.txt").readLines()

    val entries = lines[1]
        .split(",")
        .withIndex()
        .filter { it.value != "x" }
        .map { Entry(it.value.toBigInteger(), it.index.toBigInteger()) }
        .sortedByDescending { it.busId }

    var factor = entries[0].busId
    var timestamp = factor - entries[0].offset

    for (entry in entries.drop(1)) {
        while (true) {
            if ((timestamp + entry.offset) % entry.busId == 0.toBigInteger()) {
                factor *= entry.busId
                break
            } else {
                timestamp += factor
            }
        }
    }

    println("optimal timestamp: $timestamp")
}