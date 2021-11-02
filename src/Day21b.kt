import java.io.File

fun main() {
    val lines = File("data/day21_input.txt").readLines()

    val ingredientCounts = mutableMapOf<String, Int>()

    // Mapping of allergens to possible ingredients
    val possibles = mutableMapOf<String, Set<String>>()

    for (line in lines) {
        val ingredients = line
            .substringBefore('(')
            .trim()
            .split(' ')
            .toSet()

        val allergens = line.substringAfter("contains")
            .substringBefore(')')
            .split(',')
            .map { it.trim() }

        for (ingredient in ingredients) {
            ingredientCounts[ingredient] = ingredientCounts.getOrDefault(ingredient, 0) + 1
        }

        for (allergen in allergens) {
            if (allergen in possibles) {
                // intersection between previous set of ingredients and new set of possible ingredients
                possibles[allergen] = possibles[allergen]!!.intersect(ingredients)
            } else {
                possibles[allergen] = ingredients
            }
        }
    }

    // resolved allergen:ingredient pairings
    val solutions = resolve(possibles)
    for ((allergen, ingredient) in solutions) {
        println("$allergen -> $ingredient")
    }

    val result = ingredientCounts
        .filter { it.key !in solutions.values }
        .map { it.value }
        .sum()

    println(result)

    // sort by the allergens and then print the ingredients
    println(solutions.toSortedMap()
        .map { it.value }
        .joinToString(separator = ","))
}

private fun resolve(possibles: Map<String, Set<String>>): Map<String, String> {
    val possibles = possibles.toMutableMap()
    val solutions = mutableMapOf<String, String>()

    while (true) {
        val allergen = possibles.keys.firstOrNull {
            possibles[it]!!.size == 1 && it !in solutions
        } ?: return solutions

        val ingredient = possibles[allergen]!!.first()
        solutions[allergen] = ingredient

        // Now remove this allergen from all the other possibles
        for (a in possibles.keys) {
            if (a != allergen) {
                possibles[a] = possibles[a]!!.minus(ingredient)
            }
        }
    }
}