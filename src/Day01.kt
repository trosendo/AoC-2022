fun main() {
    fun part1(input: List<String>) =
        input.fold(listOf(emptyList<Int>())) { acc, value ->
            acc.plusElement(value.toIntOrNull()?.run {
                acc[acc.lastIndex].plus(this)
            } ?: emptyList())
        }.maxOfOrNull { it.sum() } ?: -1


    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
