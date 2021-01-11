import java.io.File

fun main() {
    val lines = File("data/day04_input.txt").readLines()
    val passport = mutableMapOf<String, String>()
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
    val pattern = Regex("""(\S+):(\S+)""")

    return pattern.findAll(line).map {
        val (key, value) = it.destructured
        key to value
    }.toMap()
}

private fun validatePassport(passport: Map<String, String>): Boolean {
    fun validateYearField(value: String, minYear: Int, maxYear: Int): Boolean {
        val expectedFormat = Regex("""\d{4}""").matches(value)
        return expectedFormat && (value.toInt() in minYear..maxYear)
    }

    fun validateHeight(value: String): Boolean {
        val matchResult = Regex("""(\d+)(cm|in)""").matchEntire(value)

        if (matchResult != null) {
            val n = matchResult.groupValues[1].toInt()
            val units = matchResult.groupValues[2]

            return if (units == "cm") {
                n in 150..193
            } else {
                n in 59..76
            }
        }

        return false
    }

    fun validateField(field: String): Boolean {
        if (!passport.containsKey(field))
            return false

        val value = passport[field]!!
        return when (field) {
            "byr" -> validateYearField(value, 1920, 2002)
            "iyr" -> validateYearField(value, 2010, 2020)
            "eyr" -> validateYearField(value, 2020, 2030)
            "hgt" -> validateHeight(value)
            "hcl" -> Regex("""#[a-f0-9]{6}""").matches(value)
            "ecl" -> Regex("""amb|blu|brn|gry|grn|hzl|oth""").matches(value)
            "pid" -> Regex("""\d{9}""").matches(value)
            else -> true
        }
    }

    return listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid").all { validateField(it) }
}