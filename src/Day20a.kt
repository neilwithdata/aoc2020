import java.io.File

private class Tile(fileData: List<String>) {
    val id: Int = fileData[0].substring(5..8).toInt()
    val borders: List<String> = fileData.drop(1).let { tileData ->
        listOf(
            tileData.first(), // top
            tileData.joinToString(separator = "") { it.last().toString() }, // right
            tileData.last(), // bottom
            tileData.joinToString(separator = "") { it.first().toString() } // left
        )
    }

    fun hasSharedBorder(tile: Tile): Boolean {
        return borders.any { border ->
            border in tile.borders || border.reversed() in tile.borders
        }
    }

    companion object {
        const val ROWS = 10
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
            Tile(lines.slice(it.index until it.index + Tile.ROWS + 1))
        }


    var result = 1L
    for (tile in tiles) {
        val borderCount = tiles.count {
            it != tile && it.hasSharedBorder(tile)
        }

        if (borderCount == 2) {
            result *= tile.id
            println("Tile ${tile.id} is a border tile")
        }
    }

    println(result)
}