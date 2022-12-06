fun main() {


    fun part1(input: String) : Int = input.foldIndexed(emptyList<Char>() to 0) { index, (list, indexFound), char ->
        val startIndex = maxOf(list.size - 4, 0)
        if(list.subList(startIndex, list.size).distinct().size == 4) list to indexFound else list + char to index
    }.second + 1


    fun part2(input: String) : Int = input.foldIndexed(emptyList<Char>() to 0) { index, (list, indexFound), char ->
        val startIndex = maxOf(list.size - 14, 0)
        if(list.subList(startIndex, list.size).distinct().size == 14) list to indexFound else list + char to index
    }.second + 1


    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsText("Day06_test")
    println(part1(testInput))
    check(part1(testInput) == 7)

    val input = readInputAsText("Day06")
    println(part1(input))
    println(part2(input))
}