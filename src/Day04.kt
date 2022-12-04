fun main() {
    fun getElfZones(elfPair: String): Pair<Sequence<Int>, Sequence<Int>> {
        val (elf1, elf2) = elfPair.split(",").map { it.split("-").map { it.toInt() } }
        val elf1Zones = generateSequence(elf1[0]) { if (it < elf1[1]) it + 1 else null }
        val elf2Zones = generateSequence(elf2[0]) { if (it < elf2[1]) it + 1 else null }
        return elf1Zones to elf2Zones
    }

    fun part1(input: List<String>) = input.map { elfPair ->
        val (elf1Zones, elf2Zones) = getElfZones(elfPair)
        if (elf1Zones.toList().containsAll(elf2Zones.toList()) || elf2Zones.toList().containsAll(elf1Zones.toList())) 1
        else 0
    }.sum()

    fun part2(input: List<String>) = input.map { elfPair ->
        val (elf1Zones, elf2Zones) = getElfZones(elfPair)
        if(elf1Zones.toList().any { elf2Zones.contains(it) } || elf2Zones.toList().any { elf1Zones.contains(it) }) 1 else 0
    }.sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsList("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInputAsList("Day04")
    println(part1(input))
    println(part2(input))
}
