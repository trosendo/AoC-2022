fun main() {
    fun getRepeatedInBothLists(
        list: Collection<Char>,
        middlePoint: Int
    ) = list.foldIndexed(emptySet<Char>() to emptySet<Char>()) { index, (firstHalf, repeated), currentChar ->
        if (index < middlePoint) firstHalf + currentChar to repeated
        else if (firstHalf.contains(currentChar)) firstHalf to repeated + currentChar
        else firstHalf to repeated
    }

    fun part1(input: List<String>) = input.sumOf {
        val result = getRepeatedInBothLists(it.toList(), it.length/2)

        val char = result.second.single()
        if (char.isLowerCase()) char.code - 96 else char.code - 38
    }

    fun part2(input: List<String>) = input.chunked(3) { elves ->
        val firstSet = elves[0].toList()
        val secondSet = elves[1].toList()
        val thirdSet = elves[2].toList()

        val firstAndSecond = getRepeatedInBothLists(firstSet + secondSet, firstSet.size).second
        val char = getRepeatedInBothLists(firstAndSecond.toList() + thirdSet.toList(), firstAndSecond.size).second.single()
        if (char.isLowerCase()) char.code - 96 else char.code - 38
    }.sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsList("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)
    println(part2(testInput))

    val input = readInputAsList("Day03")
    println(part1(input))
    println(part2(input))
}
