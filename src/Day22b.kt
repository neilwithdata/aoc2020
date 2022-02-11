import java.io.File

class Game(cards1: List<Int>, cards2: List<Int>) {
    private val deck1 = ArrayDeque(cards1)
    private val deck2 = ArrayDeque(cards2)

    private val prevStates = mutableSetOf<String>()

    fun play(): Int {
        while (true) {
            if (isLoop())
                return 1

            if (deck1.isEmpty())
                return 2

            if (deck2.isEmpty())
                return 1

            playRound()
        }
    }

    private fun isLoop(): Boolean {
        val state = deck1.joinToString() + deck2.joinToString()

        return if (prevStates.contains(state)) {
            true
        } else {
            prevStates.add(state)
            false
        }
    }

    private fun playRound() {
        val top1 = deck1.removeFirst()
        val top2 = deck2.removeFirst()

        val winner = if (deck1.size >= top1 && deck2.size >= top2) {
            Game(deck1.take(top1), deck2.take(top2)).play()
        } else {
            if (top1 > top2) 1 else 2
        }

        if (winner == 1) {
            deck1.add(top1)
            deck1.add(top2)
        } else {
            deck2.add(top2)
            deck2.add(top1)
        }
    }

    fun calculateWinningScore(winner: Int): Int {
        val winningDeck = if (winner == 1) deck1 else deck2

        var sum = 0
        for (i in winningDeck.size downTo 1) {
            sum += (i * winningDeck[winningDeck.size - i])
        }

        return sum
    }

    companion object {
        fun fromFile(filename: String): Game {
            val d1 = mutableListOf<Int>()
            val d2 = mutableListOf<Int>()

            var current = d1

            File(filename)
                .readLines()
                .forEach { line ->
                    when (line) {
                        "Player 1:" -> current = d1
                        "Player 2:" -> current = d2
                        else -> {
                            line.takeIf { it.isNotEmpty() }?.let {
                                current.add(it.toInt())
                            }
                        }
                    }
                }

            return Game(d1, d2)
        }
    }
}

fun main() {
    val game = Game.fromFile("data/day22_input.txt")
    val winner = game.play()

    println("Score: ${game.calculateWinningScore(winner)}")
}