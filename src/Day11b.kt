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
        val result = MutableList(numRows) { MutableList(numCols) { '.' } }

        for ((r, row) in seats.withIndex()) {
            for ((c, col) in row.withIndex()) {
                result[r][c] = col

                when (col) {
                    'L' -> {
                        if (getVisible(r, c).none { it == '#' }) {
                            result[r][c] = '#'
                        }
                    }
                    '#' -> {
                        if (getVisible(r, c).count { it == '#' } >= 5) {
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

    private fun getVisible(r: Int, c: Int): List<Char> {
        val visible = mutableListOf<Char>()

        for (rx in -1..1) {
            for (ry in -1..1) {
                if (rx == 0 && ry == 0)
                    continue

                // step in the current direction until you run in to an L, #, or go out of bounds
                var step = 1
                while (true) {
                    val rowIndex = r + (step * ry)
                    val colIndex = c + (step * rx)

                    if (rowIndex !in 0 until numRows || colIndex !in 0 until numCols)
                        break

                    val seat = seats[rowIndex][colIndex]
                    if (seat == 'L' || seat == '#') {
                        visible.add(seat)
                        break // found first visible seat - move on to a different direction now
                    }

                    step++
                }
            }
        }

        return visible
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