import java.lang.Exception

// enum Direction with U for Up, D for Down, L for Left, R for Right
enum class Direction(val code: Char) {
    UP('U'), DOWN('D'), LEFT('L'), RIGHT('R')
}

// data class Position with x and y coordinates
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
            xDiff > 0 && yDiff > 0 -> this.move(Direction.LEFT).move(Direction.UP)//this.x - 1 to this.y - 1
            xDiff < 0 && yDiff > 0 -> this.move(Direction.RIGHT).move(Direction.UP)//this.x + 1 to this.y - 1
            xDiff > 0 && yDiff < 0 -> this.move(Direction.LEFT).move(Direction.DOWN)//this.x - 1 to this.y + 1
            xDiff < 0 && yDiff < 0 -> this.move(Direction.RIGHT).move(Direction.DOWN)//this.x + 1 to this.y + 1
            else -> {
                throw Exception("This should not happen")
            }
        }
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val positions = input.fold(
            listOf(Position(0,0)) to listOf(Position(0,0))
        ) { (headPositions, tailPositions), s ->
            val (direction, amount) = s.split(" ")

            (0 until amount.toInt()).fold(
                headPositions to tailPositions
            ) { (headPositions, tailPositions), _ ->
                val currentHeadPosition = headPositions.last()
                val currentTailPosition = tailPositions.last()

                //println("Current head position $currentHeadPosition moving $direction...")
                val nextHeadPosition = currentHeadPosition.move(direction.first())
                //println("Current head position $currentHeadPosition moved to $nextHeadPosition...")
                val nextTailPosition = if(!currentTailPosition.isTouching(nextHeadPosition)) {
                    //println("Tail is not touching head, moving tail to be touching head... Tail: $currentTailPosition, Head: $nextHeadPosition")
                    currentTailPosition.moveToBeTouching(nextHeadPosition)
                } else {
                    currentTailPosition
                }

                headPositions + nextHeadPosition to tailPositions + nextTailPosition
            }.let { newPositions ->
                headPositions + newPositions.first.takeLast(amount.toInt()) to
                        tailPositions + newPositions.second.takeLast(amount.toInt())
            }
        }

        return positions.second.distinct().size
    }

    fun part2(input: List<String>): Int {

        return -1
    }

    println("Result of Part01 (test): " + part1(readInputAsList("Day09_test")))
    println("Result of Part02 (test): " + part2(readInputAsList("Day09_test")))
    println("Result of Part01: " + part1(readInputAsList("Day09")))
    println("Result of Part02: " + part2(readInputAsList("Day09")))
}