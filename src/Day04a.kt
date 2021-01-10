import java.io.File

fun main() {
    val lines = File("data/day04_input.txt").readLines()
    var passport = mutableMapOf<String, String>()
    var validCount = 0

    for (line in lines) {
        if (line.isBlank()) {
            if (validatePassport(passport))
                validCount++

            passport.clear()
        }

        passport.putAll(extractValues(line))
    }

    // process final passport, if any
    if (validatePassport(passport))
        validCount++

    println("Valid: $validCount")
}

private fun extractValues(line: String): Map<String, String> {
    val pattern = """(\S+):(\S+)""".toRegex()

    return pattern.findAll(line).map {
        val (key, value) = it.destructured
        key to value
    }.toMap()
}

private fun validatePassport(passport: Map<String, String>) =
    listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
        .all { key -> passport.containsKey(key) }