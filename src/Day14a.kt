import java.io.File
import java.lang.Long.parseLong

const val BIT_LENGTH = 36

class Mask(private val mask: String) {
    fun convertValue(value: Long): Long {
        val valueString = value.toString(radix = 2).padStart(BIT_LENGTH, '0')

        var result = CharArray(BIT_LENGTH) { i ->
            when (mask[i]) {
                'X' -> valueString[i]
                else -> mask[i]
            }
        }

        return parseLong(result.concatToString(), 2)
    }
}

fun main() {
    val memory = mutableMapOf<Long, Long>()

    val lines = File("data/day14_input.txt").readLines()

    var mask: Mask? = null
    for (line in lines) {
        if (line.startsWith("mask")) {
            mask = Mask(line.substringAfter('=').trim())
        } else if (line.startsWith("mem")) {
            val address = line.substring(4, line.indexOf(']')).toLong()
            val value = line.substringAfter('=').trim()
            memory[address] = mask?.convertValue(value.toLong()) ?: 0
        }
    }

    println("Sum: ${memory.values.sum()}")
}