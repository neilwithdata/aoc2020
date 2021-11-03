import java.io.File

class Game(filename: String) {
    private val deck1 = ArrayDeque<Int>()
    private val deck2 = ArrayDeque<Int>()

    init {
        var current = deck1

        File(filename)
            .readLines()
            .forEach { line ->
                when (line) {
                    "Player 1:" -> current = deck1
                    "Player 2:" -> current = deck2
                    else -> {
                        line.takeIf { it.isNotEmpty() }?.let {
                            current.addLast(it.toInt())
                        }
                    }
                }
            }
    }

    val isFinished: Boolean
        get() = deck1.isEmpty() || deck2.isEmpty()

    fun playRound() {
        val top1 = deck1.removeFirst()
        val top2 = deck2.removeFirst()

        if (top1 > top2) {
            deck1.addLast(top1)
            deck1.addLast(top2)
        } else if (top2 > top1) {
            deck2.addLast(top2)
            deck2.addLast(top1)
        } else {
            throw IllegalStateException("cards equal in value")
        }
    }

    fun calculateWinningScore(): Int {
        val winningDeck = if (deck1.isEmpty()) deck2 else deck1

        var sum = 0
        for (i in winningDeck.size downTo 1) {
            sum += (i * winningDeck[winningDeck.size - i])
        }

        return sum
    }
}

fun main() {
    val game = Game("data/day22_input.txt")
    while (!game.isFinished) {
        game.playRound()
    }

    println("Score: ${game.calculateWinningScore()}")
}