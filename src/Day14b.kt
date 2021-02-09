import java.io.File
import java.lang.Long.parseLong
import kotlin.math.pow

const val BIT_LENGTH = 36

fun <T> Collection<T>.powerSet(): Set<Set<T>> = when {
    isEmpty() -> setOf(setOf())
    else -> drop(1).powerSet().let { it + it.map { it + first() } }
}

class Mask(private val mask: String) {
    fun convertValue(value: Long): List<Long> {
        val valueString = value.toString(radix = 2).padStart(BIT_LENGTH, '0')

        val baseResult = CharArray(BIT_LENGTH) { i ->
            when (mask[i]) {
                '0' -> valueString[i]
                '1' -> '1'
                'X' -> '0'
                else -> throw IllegalStateException()
            }
        }

        val baseValue = parseLong(baseResult.concatToString(), 2)

        return mask
            .reversed()
            .withIndex()
            .filter { it.value == 'X' }
            .map { 2.0.pow(it.index).toLong() }
            .toSet()
            .powerSet()
            .map {
                baseValue + it.sum()
            }
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
            val originalAddress = line.substring(4, line.indexOf(']')).toLong()
            val value = line.substringAfter('=').trim().toLong()

            val addresses = mask?.convertValue(originalAddress) ?: emptyList()
            for (address in addresses) {
                memory[address] = value
            }
        }
    }

    println("Sum: ${memory.values.sum()}")
}