import java.io.File

class Program(filename: String) {
    private val prev = mutableSetOf<Int>()
    private var indx = 0

    private val instructions = File(filename)
        .readLines()
        .map { Instruction.fromString(it) }
        .toTypedArray()

    var accumulator = 0
        private set

    fun runUntilLoop() {
        while (true) {
            if (indx in prev)
                return
            else
                prev.add(indx)

            val current = instructions[indx]
            when (current.op) {
                "nop" -> indx++
                "jmp" -> indx += current.arg
                "acc" -> {
                    accumulator += current.arg
                    indx++
                }
            }
        }
    }

    data class Instruction(val op: String, val arg: Int) {
        companion object {
            fun fromString(str: String): Instruction {
                return str.split(" ").let { strings ->
                    Instruction(strings[0], strings[1].toInt())
                }
            }
        }
    }
}

fun main() {
    val p = Program("data/day08_input.txt")
    p.runUntilLoop()
    println("accumulator: ${p.accumulator}")
}