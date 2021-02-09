// dumb brute force solution

fun main() {
    val numbers = mutableListOf(13, 16, 0, 12, 15, 1)

    repeat(2014) {
        val last = numbers.last()
        if (numbers.dropLast(1).contains(last)) {
            numbers.add(numbers.lastIndex - numbers.dropLast(1).lastIndexOf(last))
        } else {
            numbers.add(0)
        }
    }


    println(numbers)
}