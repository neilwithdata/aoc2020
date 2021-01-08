import java.io.File

fun main() {
    val lines = File("data/day03_input.txt").readLines()
    val numRows = lines.count()
    val numCols = lines[0].length

    // Positions stored as (col/x, row/y) coordinate pairs
    val treePositions = mutableSetOf<Pair<Int, Int>>()

    // Save all the tree positions from the input
    for ((row, line) in lines.withIndex()) {
        for ((col, c) in line.withIndex()) {
            if (c == '#') {
                treePositions.add(Pair(col, row))
            }
        }
    }

    var pos = Pair(0, 0)
    var treeCount = 0
    while (pos.second < numRows) {
        if (pos in treePositions)
            treeCount++

        pos = Pair((pos.first + 3) % numCols, pos.second + 1)
    }

    println("Trees encountered: $treeCount")
}