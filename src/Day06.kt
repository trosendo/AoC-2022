fun main() {

    fun findIndexOfFirstNonRepeatingNChars(input: String, lastNChars: Int) =
        input.foldIndexed(emptyList<Char>() to 0) { index, (list, indexFound), char ->
            if (list.distinct().size == lastNChars)
                list to indexFound
            else (list + char).takeLast(lastNChars) to index
        }.second + 1

    fun part1(input: String) : Int {
        val lastNChars = 4
        return findIndexOfFirstNonRepeatingNChars(input, lastNChars)
    }


    fun part2(input: String) : Int {
        val lastNChars = 14
        return findIndexOfFirstNonRepeatingNChars(input, lastNChars)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsText("Day06_test")
    println(part1(testInput))
    check(part1(testInput) == 7)

    val input = readInputAsText("Day06")
    println(part1(input))
    println(part2(input))
}