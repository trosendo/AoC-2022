fun main() {
    fun getListForEachElf(input: List<String>): Set<List<Int>> {
        val (acc, elfBasket) = input.fold(setOf<List<Int>>() to emptyList<Int>()) { (acc, elfBasket), value ->
            value.toIntOrNull()?.run {
                acc to (elfBasket + this)
            } ?: (acc.plusElement(elfBasket) to emptyList())
        }

        return acc.plusElement(elfBasket)
    }

    fun part1(input: List<String>) =
        getListForEachElf(input)
            .maxOfOrNull { it.sum() } ?: -1


    fun part2(input: List<String>) =
        getListForEachElf(input)
            .map { it.sum() }
            .sortedDescending()
            .take(3)
            .sum()


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
