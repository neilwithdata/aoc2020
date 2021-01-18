import java.io.File

private class Bag(val color: String) {
    val contains = mutableListOf<Pair<Int, Bag>>()
}

fun main() {
    val lines = File("data/day07_input.txt").readLines()
    val bags = mutableMapOf<String, Bag>()

    val childPattern = Regex("""(\d+) (.+?) bag""")

    for (line in lines) {
        val outerColor = line.substringBefore("bags").trim()
        val outerBag = bags.getOrPut(outerColor) {
            Bag(outerColor)
        }

        for (matchResult in childPattern.findAll(line)) {
            val (innerCount, innerColor) = matchResult.destructured

            bags.getOrPut(innerColor) {
                Bag(innerColor)
            }.also { bag ->
                outerBag.contains.add(innerCount.toInt() to bag)
            }
        }
    }

    var totalCount = 0
    fun traverse(bag: Bag, multiplier: Int = 1) {
        for ((innerCount, innerBag) in bag.contains) {
            totalCount += multiplier * innerCount
            traverse(innerBag, multiplier * innerCount)
        }
    }

    bags["shiny gold"]?.let { traverse(it) }
    println("total count: $totalCount")
}