fun main() {


    fun getFirstLastNCharsNotRepeated(
        list: List<Char>,
        lastNChars: Int,
        indexFound: Int,
        char: Char,
        index: Int
    ): Pair<List<Char>, Int> {
        val startIndex = maxOf(list.size - lastNChars, 0)
        return if (list.subList(startIndex, list.size)
                .distinct().size == lastNChars
        ) list to indexFound else list + char to index
    }

    fun part1(input: String) : Int = input.foldIndexed(emptyList<Char>() to 0) { index, (list, indexFound), char ->
        getFirstLastNCharsNotRepeated(list, 4, indexFound, char, index)
    }.second + 1


    fun part2(input: String) : Int = input.foldIndexed(emptyList<Char>() to 0) { index, (list, indexFound), char ->
        getFirstLastNCharsNotRepeated(list, 14, indexFound, char, index)
    }.second + 1


    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsText("Day06_test")
    println(part1(testInput))
    check(part1(testInput) == 7)

    val input = readInputAsText("Day06")
    println(part1(input))
    println(part2(input))
}