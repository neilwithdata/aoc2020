import java.io.File
import kotlin.math.sqrt

// Utility classes
data class Coord(val x: Int, val y: Int)

enum class Direction {
    TOP {
        override fun opposite() = BOTTOM
    },
    RIGHT {
        override fun opposite() = LEFT
    },
    BOTTOM {
        override fun opposite() = TOP
    },
    LEFT {
        override fun opposite() = RIGHT
    };

    abstract fun opposite(): Direction
}

class Grid(val size: Int, val marks: Set<Coord>) {
    class Border(val borderMarks: Set<Int>, val direction: Direction)

    val borders = listOf(
        Border(marks.filter { it.y == 0 }.map { it.x }.toSet(), Direction.TOP),
        Border(marks.filter { it.x == size - 1 }.map { it.y }.toSet(), Direction.RIGHT),
        Border(marks.filter { it.y == size - 1 }.map { it.x }.toSet(), Direction.BOTTOM),
        Border(marks.filter { it.x == 0 }.map { it.y }.toSet(), Direction.LEFT)
    ).associateBy { border -> border.direction }

    private fun rotated() = Grid(
        size,
        marks.map {
            Coord(size - 1 - it.y, it.x)
        }.toSet()
    )

    private fun mirrored() = Grid(
        size,
        marks.map {
            Coord(size - 1 - it.x, it.y)
        }.toSet()
    )

    // there are 8 unique transformations of a grid (4 rotational + combinations of mirroring and rotating)
    fun transformations() = sequence {
        var grid = this@Grid
        repeat(4) {
            grid = grid.rotated()
            yield(grid)
        }

        yield(grid.mirrored())
        yield(grid.rotated().mirrored())
        yield(grid.rotated().rotated().mirrored())
        yield(grid.mirrored().rotated())
    }

    fun stripBorders() = Grid(
        size - 2,
        marks.filterNot {
            it.x == 0 || it.x == size - 1 || it.y == 0 || it.y == size - 1
        }.toSet()
    ).offset(-1, -1)

    fun offset(dx: Int, dy: Int) = Grid(
        size,
        marks.map {
            Coord(it.x + dx, it.y + dy)
        }.toSet()
    )

    fun count() = marks.size

    fun print() {
        for (y in 0 until size) {
            for (x in 0 until size) {
                print(
                    if (marks.contains(Coord(x, y))) {
                        '#'
                    } else {
                        '.'
                    }
                )
            }
            println()
        }
        println()
    }
}

object UnknownTile : Tile(-1, Grid(0, emptySet()))

open class Tile(val id: Int, val grid: Grid) {
    fun transformations() = grid.transformations().map { Tile(id, it) }

    companion object {
        const val DEFAULT_SIZE = 10

        fun fromFile(fileData: List<String>): Tile {
            val id = fileData[0].substring(5..8).toInt()
            val marks = mutableSetOf<Coord>()

            fileData.drop(1).let { imageData ->
                for ((y, row) in imageData.withIndex()) {
                    for ((x, c) in row.withIndex()) {
                        if (c == '#') {
                            marks.add(Coord(x, y))
                        }
                    }
                }
            }

            return Tile(id, Grid(DEFAULT_SIZE, marks))
        }
    }
}

class Pattern(val marks: Set<Coord>) {
    val width = marks.maxOf { it.x } + 1
    val height = marks.maxOf { it.y } + 1

    fun count() = marks.count()

    fun offset(dx: Int, dy: Int) = Pattern(
        marks.map {
            Coord(it.x + dx, it.y + dy)
        }.toSet()
    )

    companion object {
        fun fromString(str: String): Pattern {
            val marks = mutableSetOf<Coord>()

            for ((y, line) in str.lines().withIndex()) {
                for ((x, c) in line.withIndex()) {
                    if (c == '#') {
                        marks.add(Coord(x, y))
                    }
                }
            }

            return Pattern(marks)
        }
    }
}

class Image(tiles: List<Tile>) {
    val size = sqrt(tiles.size.toDouble()).toInt()
    val grid = joinTiles(arrangeTiles(tiles))

    private fun arrangeTiles(tiles: List<Tile>): Array<Array<Tile?>> {
        val imageTiles: Array<Array<Tile?>> = Array(size) { Array(size) { null } }
        val tileCollection = tiles.flatMap { it.transformations() }

        for (row in 0 until size) {
            for (col in 0 until size) {
                val top: Tile? = if (row == 0) null else imageTiles[row - 1][col]
                val bottom: Tile? = if (row == size - 1) null else UnknownTile
                val left: Tile? = if (col == 0) null else imageTiles[row][col - 1]
                val right: Tile? = if (col == size - 1) null else UnknownTile

                imageTiles[row][col] = findTile(imageTiles, tileCollection, top, right, bottom, left)
            }
        }

        return imageTiles
    }

    private fun joinTiles(tileArray: Array<Array<Tile?>>): Grid {
        val unified = mutableSetOf<Coord>()

        for (row in 0 until size) {
            for (col in 0 until size) {
                val tile = tileArray[row][col]!!

                val stripped = tile.grid.stripBorders()
                val positioned = stripped.offset(col * stripped.size, row * stripped.size)

                unified.addAll(positioned.marks)
            }
        }

        return Grid((Tile.DEFAULT_SIZE - 2) * size, unified)
    }

    fun print() {
        grid.print()
    }

    private fun findTile(
        imageTiles: Array<Array<Tile?>>,
        tileCollection: List<Tile>,
        top: Tile?,
        right: Tile?,
        bottom: Tile?,
        left: Tile?
    ): Tile {
        val candidates = tileCollection.filter { tile ->
            tile.id !in imageTiles.flatten().map { it?.id }
        }

        // find true if `tile` has `required` tile in the specified `direction` (based on border marks)
        fun match(tile: Tile, direction: Direction, required: Tile?): Boolean {
            val foundTile = tileCollection
                .filter { it.id != tile.id }
                .firstOrNull { candidate ->
                    val tileBorder = tile.grid.borders[direction]!!
                    val candidateBorder = candidate.grid.borders[direction.opposite()]!!

                    tileBorder.borderMarks == candidateBorder.borderMarks
                }

            return when (required) {
                null -> foundTile == null
                is UnknownTile -> foundTile != null
                else -> foundTile == required
            }
        }

        return candidates.first { tile ->
            match(tile, Direction.TOP, top) &&
                    match(tile, Direction.RIGHT, right) &&
                    match(tile, Direction.BOTTOM, bottom) &&
                    match(tile, Direction.LEFT, left)
        }
    }

    // Return a count of the number of times `pattern` is found in the image (may require transformation first)
    fun search(pattern: Pattern): Int {
        for (transformation in grid.transformations()) {
            var count = 0
            for (dx in 0..(transformation.size - pattern.width)) {
                for (dy in 0..(transformation.size - pattern.height)) {
                    val offset = pattern.offset(dx, dy)

                    if (transformation.marks.containsAll(offset.marks)) {
                        count++
                    }
                }
            }

            // this is the right transformation for the image
            if (count > 0) {
                return count
            }
        }

        return 0
    }
}

fun main() {
    val lines = File("data/day20_input.txt")
        .readLines()

    val tiles = lines
        .withIndex()
        .filter {
            it.value.startsWith("Tile")
        }
        .map {
            Tile.fromFile(lines.slice(it.index until it.index + Tile.DEFAULT_SIZE + 1))
        }

    val image = Image(tiles)
    image.print()

    val monster = """
        |                  #
        |#    ##    ##    ###
        | #  #  #  #  #  #
    """.trimMargin()

    val pattern = Pattern.fromString(monster)
    val count = image.search(pattern)

    val result = image.grid.count() - (count * pattern.count())
    println("result: $result")
}
