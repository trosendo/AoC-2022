fun main() {
    fun getListForEachElf(input: String): List<List<Int>> =
        input.split("\n\n").map {
            it.split("\n").map { it.toIntOrNull() }.filterNotNull()
        }

    fun part1(input: String) =
        getListForEachElf(input)
            .maxOf { it.sum() }


    fun part2(input: String) =
        getListForEachElf(input)
            .map { it.sum() }
            .sortedDescending()
            .take(3)
            .sum()


    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsText("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInputAsText("Day01")
    println(part1(input))
    println(part2(input))
}
