private enum class RockPaperScissor(
    val points: Int,
    val winsOver: Char,
    val loosesOver: Char,
    val drawsOver: Char,
) {
    ROCK(1, 'C', 'B', 'A'),
    PAPER(2, 'A', 'C', 'B'),
    SCISSOR(3, 'B', 'A', 'C')
}


private enum class RockPaperScissorOutcome(
    val points: Int
) {
    WIN(6),
    DRAW(3),
    LOST(0)
}


fun main() {

    fun Char.translateToRockPaperScissor() =
        when(this) {
            'A' -> RockPaperScissor.ROCK
            'B' -> RockPaperScissor.PAPER
            else -> RockPaperScissor.SCISSOR
        }

    fun Char.translateToRockPaperScissorOutcome() =
        when(this) {
            'X' -> RockPaperScissorOutcome.LOST
            'Y' -> RockPaperScissorOutcome.DRAW
            else -> RockPaperScissorOutcome.WIN
        }

    fun RockPaperScissor.playAgainst(opponentMove: RockPaperScissor) =
        this.points + when(opponentMove) {
            this.drawsOver.translateToRockPaperScissor() -> RockPaperScissorOutcome.DRAW.points
            this.winsOver.translateToRockPaperScissor() -> RockPaperScissorOutcome.WIN.points
            this.loosesOver.translateToRockPaperScissor() -> RockPaperScissorOutcome.LOST.points
            else -> 0
        }

    fun RockPaperScissor.getShapeForOutcome(outcome: RockPaperScissorOutcome) =
        when(outcome) {
            RockPaperScissorOutcome.WIN -> this.loosesOver.translateToRockPaperScissor()
            RockPaperScissorOutcome.DRAW -> this.drawsOver.translateToRockPaperScissor()
            RockPaperScissorOutcome.LOST -> this.winsOver.translateToRockPaperScissor()
        }

    fun part1(input: List<String>): Int {
        val (_, myResult) = input.map {
            val play = it.split(" ")
            play[0][0].translateToRockPaperScissor() to
                    Char(play[1][0].code - 23).translateToRockPaperScissor()
        }.fold(0 to 0) { (opponentResult, myResult), (opponentMove, myMove) ->
            opponentResult + opponentMove.playAgainst(myMove) to myResult + myMove.playAgainst(opponentMove)
        }

        return myResult
    }

    fun part2(input: List<String>): Int {
        val (_, myResult) = input.map {
            val play = it.split(" ")
            play[0][0].translateToRockPaperScissor() to
                    play[1][0].translateToRockPaperScissorOutcome()
        }.fold(0 to 0) { (opponentResult, myResult), (opponentMove, outcome) ->
            val myMove = opponentMove.getShapeForOutcome(outcome)
            opponentResult + opponentMove.playAgainst(myMove) to myResult + myMove.playAgainst(opponentMove)
        }

        return  myResult
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsList("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)


    val input = readInputAsList("Day02")
    println(part1(input))
    println(part2(input))
}
