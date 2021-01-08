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

    val slopes = listOf(
        1 to 1,
        3 to 1,
        5 to 1,
        7 to 1,
        1 to 2
    )

    val product = slopes
        .map { slope -> treeCount(treePositions, numRows, numCols, slope.first, slope.second) }
        .reduce { acc, i -> acc * i }

    println(product)
}

fun treeCount(
    treePositions: Set<Pair<Int, Int>>,
    numRows: Int,
    numCols: Int,
    xStep: Int,
    yStep: Int
): Int {
    var pos = Pair(0, 0)
    var treeCount = 0

    while (pos.second < numRows) {
        if (pos in treePositions)
            treeCount++

        pos = Pair((pos.first + xStep) % numCols, pos.second + yStep)
    }

    return treeCount
}