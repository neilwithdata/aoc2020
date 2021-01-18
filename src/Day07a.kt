import java.io.File

private class Bag(val color: String) {
    val inside = mutableListOf<Bag>()
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
            val innerColor = matchResult.groups[2]?.value ?: throw IllegalStateException()

            bags.getOrPut(innerColor) {
                Bag(innerColor)
            }.apply {
                inside.add(outerBag)
            }
        }
    }

    val uniqueBags = mutableSetOf<Bag>()
    fun traverse(bag: Bag) {
        uniqueBags.add(bag)
        for (innerBag in bag.inside) {
            traverse(innerBag)
        }
    }

    bags["shiny gold"]?.let { traverse(it) }
    println("unique bags: ${uniqueBags.size - 1}")
}