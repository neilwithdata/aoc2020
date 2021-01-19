import java.io.File

class Program(private val instructions: Array<Instruction>) {
    private val prev = mutableSetOf<Int>()
    private var indx = 0

    var accumulator = 0
        private set

    fun run(): Boolean {
        while (true) {
            when (indx) {
                instructions.size -> return true
                in prev -> return false
                else -> prev.add(indx)
            }

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

    data class Instruction(var op: String, val arg: Int) {
        fun toggle() {
            when (op) {
                "nop" -> op = "jmp"
                "jmp" -> op = "nop"
            }
        }

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
    val instructions = File("data/day08_input.txt")
        .readLines()
        .map { Program.Instruction.fromString(it) }
        .toTypedArray()

    for (instruction in instructions) {
        instruction.toggle() // swap and test

        val program = Program(instructions)
        if (program.run()) {
            println("Program terminated successfully. Accumulator: ${program.accumulator} ")
            return
        }

        instruction.toggle() // swap back
    }
}