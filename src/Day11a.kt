import java.io.File

class SeatMap(filename: String) {
    private var seats = File(filename)
        .readLines()
        .map {
            it.toList()
        }

    private val numRows = seats.size
    private val numCols = seats[0].size

    val numOccupied
        get() = seats.flatten().count { it == '#' }

    fun processRound(): Boolean {
        val result = MutableList(numRows) {
            MutableList(numCols) {
                '.'
            }
        }

        for ((r, row) in seats.withIndex()) {
            for ((c, col) in row.withIndex()) {
                result[r][c] = col

                when (col) {
                    'L' -> {
                        if (getAdjacent(r, c).none { it == '#' }) {
                            result[r][c] = '#'
                        }
                    }
                    '#' -> {
                        if (getAdjacent(r, c).count { it == '#' } >= 4) {
                            result[r][c] = 'L'
                        }
                    }
                }
            }
        }

        return if (result == seats) {
            false
        } else {
            seats = result
            true
        }
    }

    private fun getAdjacent(r: Int, c: Int): List<Char> {
        val adjacent = mutableListOf<Char>()

        for (rx in -1..1) {
            for (ry in -1..1) {
                if (rx == 0 && ry == 0)
                    continue

                if ((r + ry) in 0 until numRows && (c + rx) in 0 until numCols)
                    adjacent.add(seats[r + ry][c + rx])
            }
        }

        return adjacent
    }

    fun display() {
        println("-".repeat(10))
        seats.forEach {
            println(it.joinToString(separator = ""))
        }
    }
}


fun main() {
    val seatMap = SeatMap("data/day11_input.txt")
    while (seatMap.processRound()) {
        seatMap.display()
    }

    println("num occupied seats: ${seatMap.numOccupied}")
}