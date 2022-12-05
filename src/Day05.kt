@file:JvmName("Day04Kt")

fun main() {
    fun <E> List<List<E>>.transpose(): List<List<E?>> {
        val columnIndices = 0 until this.maxOf { it.size } // 0 a 9

        val rowIndices = 0 until this.size // 0 a 7

        return columnIndices.map { columnIndex ->
            rowIndices.map { rowIndex ->
                val element = this.getOrNull(rowIndex)?.getOrNull(columnIndex)
                element
            }
        }
    }


    fun parseMoves(moves: List<String>) = moves.map {
        val (amountToMove, afterFrom) = it.substringAfter("move ").split(" from ")
        val (sourceStack, targetStack) = afterFrom.split(" to ")

        Triple(amountToMove.toInt(), sourceStack.toInt() - 1, targetStack.toInt() - 1)
    }

    fun parseCargo(cargo: List<String>, numberOfStacks: List<Int>): List<List<Char>> {
        val start = 1
        val allIndexes = numberOfStacks.fold(emptyList<Int>()) { acc, stackIndex ->
            if(stackIndex == start) listOf(start) else acc + (acc.last() + 4)
        }
        val cargoStacks = cargo.take(cargo.size-1).map { current ->
            allIndexes.map { containerIndex ->
                val containerChar = current.getOrNull(containerIndex)
                if(containerChar == ' ') null else containerChar
            }
        }

        return cargoStacks.transpose().map { it.asReversed().mapNotNull { it } }
    }

    fun List<List<Char>>.moveCargo(move: Triple<Int, Int, Int>): List<List<Char>> {
        val (amountToMove, srcStackIndex, targetStackIndex) = move
        if(amountToMove == 0) return this
        val mutableList = this.toMutableList()
        val targetStack = mutableList[targetStackIndex].toMutableList()
        targetStack += mutableList[srcStackIndex].last()
        mutableList[targetStackIndex] = targetStack
        mutableList[srcStackIndex] = mutableList[srcStackIndex].dropLast(1)
        return mutableList.toList().moveCargo(Triple(amountToMove-1, srcStackIndex, targetStackIndex))
    }

    fun List<List<Char>>.moveCargoV2(move: Triple<Int, Int, Int>): List<List<Char>> {
        val (amountToMove, srcStackIndex, targetStackIndex) = move
        val mutableList = this.toMutableList()
        val targetStack = mutableList[targetStackIndex].toMutableList()

        val startIndex = if(mutableList[srcStackIndex].size < amountToMove) 0 else mutableList[srcStackIndex].size - amountToMove

        targetStack += mutableList[srcStackIndex].subList(startIndex, mutableList[srcStackIndex].size)
        mutableList[targetStackIndex] = targetStack
        mutableList[srcStackIndex] = mutableList[srcStackIndex].dropLast(amountToMove)
        return mutableList.toList()
    }

    fun getNumberOfStacks(cargo: List<String>) =
        cargo.takeLast(1).single().split("   ").map { it.trim().toInt() }

    fun part1(cargo: List<String>, moves: List<String>): String {
        val numberOfStacks = getNumberOfStacks(cargo)
        val cargoParsed = parseCargo(cargo, numberOfStacks)


        val movesParsed = parseMoves(moves)

        val result = movesParsed.fold(cargoParsed) { cargoStacks, move ->
            cargoStacks.moveCargo(move)
        }

        return result.map { it.last() }.joinToString("")
    }

    fun part2(cargo: List<String>, moves: List<String>) : String {
        val numberOfStacks = getNumberOfStacks(cargo)
        val cargoParsed = parseCargo(cargo, numberOfStacks)


        val movesParsed = parseMoves(moves)

        val result = movesParsed.fold(cargoParsed) { cargoStacks, move ->
            cargoStacks.moveCargoV2(move)
        }

        return result.map { it.last() }.joinToString("")
    }

    // test if implementation meets criteria from the description, like:
    val (demoCargo, demoMoves) = readInputAsText("Day04_test").split("\n\n").map { it.split("\n") }
    check(part1(demoCargo, demoMoves) == "CMZ")

    val (cargo, moves) = readInputAsText("Day04").split("\n\n").map { it.split("\n") }
    println(part1(cargo, moves))
    println(part2(cargo, moves))
}
