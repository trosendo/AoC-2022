import java.lang.Exception

enum class Direction(val code: Char) {
    UP('U'), DOWN('D'), LEFT('L'), RIGHT('R')
}

data class Position(val x: Int, val y: Int) {
    fun move(direction: Direction) = this.move(direction.code)

    fun move(direction: Char): Position = when (Direction.values().find { it.code == direction }) {
        Direction.UP -> this.x to this.y - 1
        Direction.DOWN -> this.x to this.y + 1
        Direction.LEFT -> this.x - 1 to this.y
        Direction.RIGHT -> this.x + 1 to this.y
        else -> {
            println("Unknown direction: $direction")
            this.x to this.y
        }
    }.toPosition()

    fun Pair<Int, Int>.toPosition(): Position = Position(this.first, this.second)

    fun isTouching(position: Position) =
        (this.x == position.x && this.y == position.y) ||
        (this.x == position.x && (this.y == position.y - 1 || this.y == position.y + 1)) ||
        (this.y == position.y && (this.x == position.x - 1 || this.x == position.x + 1)) ||
        (this.x == position.x - 1 && this.y == position.y - 1) ||
        (this.x == position.x + 1 && this.y == position.y - 1) ||
        (this.x == position.x - 1 && this.y == position.y + 1) ||
        (this.x == position.x + 1 && this.y == position.y + 1)

    fun moveToBeTouching(position: Position): Position {
        val xDiff = this.x - position.x
        val yDiff = this.y - position.y
        return when {
            xDiff == 0 && yDiff > 0 -> this.move(Direction.UP)
            xDiff == 0 && yDiff < 0 -> this.move(Direction.DOWN)
            xDiff > 0 && yDiff == 0 -> this.move(Direction.LEFT)
            xDiff < 0 && yDiff == 0 -> this.move(Direction.RIGHT)
            xDiff > 0 && yDiff > 0 -> this.move(Direction.LEFT).move(Direction.UP)
            xDiff < 0 && yDiff > 0 -> this.move(Direction.RIGHT).move(Direction.UP)
            xDiff > 0 && yDiff < 0 -> this.move(Direction.LEFT).move(Direction.DOWN)
            xDiff < 0 && yDiff < 0 -> this.move(Direction.RIGHT).move(Direction.DOWN)
            else -> {
                throw Exception("This should not happen")
            }
        }
    }
}

fun main() {
    fun checkTailIsTouchingHead(currentTailPosition: Position, nextHeadPosition: Position): Position {
        return if (!currentTailPosition.isTouching(nextHeadPosition)) {
            currentTailPosition.moveToBeTouching(nextHeadPosition)
        } else {
            currentTailPosition
        }
    }

    fun moveHeadAndTail(
        headPositions: List<Position>,
        tailPositions: List<Position>,
        direction: String
    ): Pair<List<Position>, List<Position>> {
        val currentHeadPosition = headPositions.last()
        val currentTailPosition = tailPositions.last()

        val nextHeadPosition = currentHeadPosition.move(direction.first())
        val nextTailPosition = checkTailIsTouchingHead(currentTailPosition, nextHeadPosition)

        return headPositions + nextHeadPosition to tailPositions + nextTailPosition
    }

    fun processMoves(input: List<String>) = input.fold(
        listOf(Position(0, 0)) to listOf(Position(0, 0))
    ) { (headPositions, tailPositions), s ->
        val (direction, amount) = s.split(" ")

        (0 until amount.toInt()).fold(
            headPositions to tailPositions
        ) { (headPositions, tailPositions), _ ->
            moveHeadAndTail(headPositions, tailPositions, direction)
        }.let { newPositions ->
            headPositions + newPositions.first.takeLast(amount.toInt()) to
                    tailPositions + newPositions.second.takeLast(amount.toInt())
        }
    }

    fun processMovesV2(input: List<String>, numberOfKnots: Int): Int {
        val listOfPositionsOfKnots = MutableList(numberOfKnots) { mutableListOf(Position(0, 0)) }
        for(s in input) {
            val (direction, amount) = s.split(" ")
            for (i in 0 until amount.toInt()) {

                val (newHeadPositions, newTailPositions) = moveHeadAndTail(listOfPositionsOfKnots[0], listOfPositionsOfKnots[1], direction)

                listOfPositionsOfKnots[0] = newHeadPositions.toMutableList()
                listOfPositionsOfKnots[1] = newTailPositions.toMutableList()

                for(knot in 1 until listOfPositionsOfKnots.size - 1) {
                    val headPositions = listOfPositionsOfKnots[knot]
                    val tailPositions = listOfPositionsOfKnots[knot + 1]

                    val nextTailPosition = checkTailIsTouchingHead(tailPositions.last(), headPositions.last())

                    listOfPositionsOfKnots[knot + 1] += nextTailPosition
                }
            }
        }

        return listOfPositionsOfKnots.last().distinct().size
    }

    fun part1(input: List<String>): Int {
        val positions = processMoves(input)
        return positions.second.distinct().size
    }

    fun part2(input: List<String>): Int {
        return processMovesV2(input, 10)
    }

    println("Result of Part01 (test): " + part1(readInputAsList("Day09_test")))
    println("Result of Part02 (test): " + part2(readInputAsList("Day09_test")))
    println("Result of Part01: " + part1(readInputAsList("Day09")))
    println("Result of Part02: " + part2(readInputAsList("Day09")))
}