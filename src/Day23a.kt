private const val INPUT = "198753462"

class CupRing(labels: List<Int>) {
    private var current = Cup(labels[0])

    private val lowest = labels.minOrNull() ?: 0
    private val highest = labels.maxOrNull() ?: 0

    init {
        var roving = current

        for (cup in labels.drop(1)) {
            roving.next = Cup(cup)
            roving = roving.next!!
        }
        roving.next = current
    }

    fun performMove() {
        val removed = removeCups()
        val destination = selectDestination(removed)

        insertRemoved(removed, destination)
        current = current.next!!
    }

    private fun insertRemoved(removed: Cup, destination: Int) {
        // find the destination first
        var roving = current
        while (true) {
            if (roving.label == destination)
                break

            roving = roving.next!!
        }

        var following = roving.next
        roving.next = removed
        removed.next?.next?.next = following
    }

    private fun removeCups(): Cup {
        val removed = current.next!!

        var roving = current
        repeat(4) {
            roving = roving.next!!
        }

        current.next = roving
        return removed
    }

    private fun selectDestination(removed: Cup): Int {
        // Flatten the removed labels
        val removedLabels = mutableListOf<Int>()
        var roving = removed

        repeat(3) {
            removedLabels.add(roving.label)
            roving = roving.next!!
        }

        var destination = current.label - 1
        while (true) {
            if (destination < lowest) {
                destination = highest
            }

            if (destination in removedLabels) {
                destination--
                continue
            } else {
                break
            }
        }

        return destination
    }

    data class Cup(val label: Int, var next: Cup? = null)

    override fun toString(): String {
        var res = "(${current.label}) "

        var roving = current.next!!
        while (roving != current) {
            res += "${roving.label} "
            roving = roving.next!!
        }

        return res
    }
}

fun main() {
    val cupRing = CupRing(INPUT.map { it.digitToInt() })
    println(cupRing)

    repeat(100) {
        cupRing.performMove()
        println(cupRing)
    }
}