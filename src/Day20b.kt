import java.io.File
import kotlin.math.sqrt

data class Coord(val x: Int, val y: Int)

fun Set<Coord>.rotated(size: Int) = this
    .map {
        Coord(size - 1 - it.y, it.x)
    }.toSet()

fun Set<Coord>.mirrored(size: Int) = this
    .map {
        Coord(size - 1 - it.x, it.y)
    }.toSet()

object UnknownTile : Tile(-1, emptySet())

open class Tile(val id: Int, val marks: Set<Coord>, val size: Int = DEFAULT_SIZE) {
    class Border(val marks: Set<Int>, val direction: Direction) {
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
    }

    val borders = listOf(
        Border(marks.filter { it.y == 0 }.map { it.x }.toSet(), Border.Direction.TOP),
        Border(marks.filter { it.x == size - 1 }.map { it.y }.toSet(), Border.Direction.RIGHT),
        Border(marks.filter { it.y == size - 1 }.map { it.x }.toSet(), Border.Direction.BOTTOM),
        Border(marks.filter { it.x == 0 }.map { it.y }.toSet(), Border.Direction.LEFT)
    ).associateBy { border -> border.direction }

    // there are 8 unique transformations of a tile (4 rotational + combinations of mirroring and rotating)
    fun transformations() = sequence {
        var rotated = marks
        repeat(4) {
            rotated = rotated.rotated(size)
            yield(Tile(id, rotated, size))
        }

        yield(Tile(id, marks.mirrored(size), size))
        yield(Tile(id, marks.rotated(size).mirrored(size), size))
        yield(Tile(id, marks.rotated(size).rotated(size).mirrored(size), size))
        yield(Tile(id, marks.mirrored(size).rotated(size), size))
    }

    fun stripBorders(): Tile {
        return Tile(
            id,
            marks.filterNot {
                it.x == 0 || it.x == size - 1 || it.y == 0 || it.y == size - 1
            }.toSet(),
            size - 2
        ).offset(-1, -1)
    }

    fun offset(dx: Int, dy: Int): Tile {
        return Tile(
            id,
            marks.map {
                Coord(it.x + dx, it.y + dy)
            }.toSet(),
            size
        )
    }

    fun print() {
        println("Tile $id")

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

            return Tile(id, marks)
        }
    }
}

private class Image(tiles: List<Tile>) {
    val size = sqrt(tiles.size.toDouble()).toInt()
    val marks: Set<Coord>

    init {
        val tileArray = arrangeTiles(tiles)
        marks = joinTiles(tileArray)
    }

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

    private fun joinTiles(tileArray: Array<Array<Tile?>>): Set<Coord> {
        val unified = mutableSetOf<Coord>()

        for (row in 0 until size) {
            for (col in 0 until size) {
                val tile = tileArray[row][col]!!
                val updated = tile.stripBorders().offset(col * (Tile.DEFAULT_SIZE - 2), row * (Tile.DEFAULT_SIZE - 2))

                unified.addAll(updated.marks)
            }
        }

        return unified
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
        fun match(tile: Tile, direction: Tile.Border.Direction, required: Tile?): Boolean {
            val foundTile = tileCollection
                .filter { it.id != tile.id }
                .firstOrNull { candidate ->
                    val tileBorder = tile.borders[direction]!!
                    val candidateBorder = candidate.borders[direction.opposite()]!!

                    tileBorder.marks == candidateBorder.marks
                }

            return when (required) {
                null -> foundTile == null
                is UnknownTile -> foundTile != null
                else -> foundTile == required
            }
        }

        return candidates.first { tile ->
            match(tile, Tile.Border.Direction.TOP, top) &&
                    match(tile, Tile.Border.Direction.RIGHT, right) &&
                    match(tile, Tile.Border.Direction.BOTTOM, bottom) &&
                    match(tile, Tile.Border.Direction.LEFT, left)
        }
    }

    fun print() {
        for (y in 0 until (Tile.DEFAULT_SIZE - 2) * size) {
            for (x in 0 until (Tile.DEFAULT_SIZE - 2) * size) {
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

fun main() {
    val lines = File("data/day20_testing.txt")
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


//    val monster = """
//        |                  #
//        |#    ##    ##    ###
//        | #  #  #  #  #  #
//    """.trimMargin()
//
//    val monsterLines = monster.split('\n')
//    for (line in monsterLines) {
//        println("line is ${line.length} and has ${line.count { it == '#' }} marks")
//    }

    // represent the monster as a set of coordinates and push it to every possible offset and orientation
}
