import java.io.File

data class Cube(val x: Int, val y: Int, val z: Int, val w: Int) {
    fun getNeighbors(): Set<Cube> {
        val neighbors = mutableSetOf<Cube>()

        for (dx in -1..1) {
            for (dy in -1..1) {
                for (dz in -1..1) {
                    for (dw in -1..1) {
                        if (dx == 0 && dy == 0 && dz == 0 && dw == 0)
                            continue

                        neighbors.add(Cube(x + dx, y + dy, z + dz, w + dw))
                    }
                }
            }
        }

        return neighbors
    }
}

fun Set<Cube>.getRange(selector: (Cube) -> Int): IntRange {
    return this.minOf(selector)..this.maxOf(selector)
}

fun IntRange.widen(delta: Int): IntRange {
    return (this.first - delta)..(this.last + delta)
}

fun main() {
    val lines = File("data/day17_input.txt").readLines()

    val activeCubes = mutableSetOf<Cube>()

    val z = 0
    val w = 0
    for ((y, line) in lines.withIndex()) {
        for ((x, c) in line.withIndex()) {
            if (c == '#') {
                activeCubes.add(Cube(x, -y, z, w))
            }
        }
    }

    var state = activeCubes.toSet()
    repeat(6) {
        state = runCycle(state)
    }

    println("${state.size} active cubes")
}

private fun runCycle(activeCubes: Set<Cube>): Set<Cube> {
    // establish the bounding ranges of the collection of cubes
    val xRange = activeCubes.getRange { it.x }.widen(1)
    val yRange = activeCubes.getRange { it.y }.widen(1)
    val zRange = activeCubes.getRange { it.z }.widen(1)
    val wRange = activeCubes.getRange { it.w }.widen(1)

    val newActiveCubes = mutableSetOf<Cube>()

    for (x in xRange) {
        for (y in yRange) {
            for (z in zRange) {
                for (w in wRange) {
                    val currentCube = Cube(x, y, z, w)
                    val activeNeighbors = currentCube.getNeighbors() intersect activeCubes

                    if (currentCube in activeCubes) {
                        if (activeNeighbors.size in 2..3) {
                            newActiveCubes.add(currentCube)
                        }
                    } else {
                        if (activeNeighbors.size == 3) {
                            newActiveCubes.add(currentCube)
                        }
                    }
                }
            }
        }
    }

    return newActiveCubes
}
