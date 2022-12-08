import kotlin.math.abs

private data class ForestTree(
    val height: Int,
    val yPos: Int,
    val xPos: Int,
    val id: Int
)

private data class TreeRow(
    val items: List<ForestTree>
)

fun main() {
    var count = 0
    val forest = readInputAsList("Day08").mapIndexed { y, row ->
        TreeRow(row.mapIndexed { x, char ->
            ForestTree(char.toString().toInt(), x, y, count++)
        })
    }

    println("Result of part1: " + part1(forest).filter { it.value }.size)
    println("Result of part2: " + part2(forest).values.max())
}

private fun part1(forest: List<TreeRow>): Map<ForestTree, Boolean> {
    val visibleTrees = forest.flatMap { row ->
        row.items.map { tree ->
            val treesInRow = row.items
            val treesInColumn =
                forest.flatMap { treeRow -> treeRow.items.filter { it.yPos == tree.yPos } }//.filter { it.items..yPos != tree.yPos }.flatMap { it.items }.filter { it.xPos == tree.xPos }

            val highestTreeAbove = treesInRow.filter { it.yPos < tree.yPos }.maxByOrNull { it.height }?.height ?: -1
            val highestTreesBelow = treesInRow.filter { it.yPos > tree.yPos }.maxByOrNull { it.height }?.height ?: -1
            val highestTreesToTheLeft =
                treesInColumn.filter { it.xPos < tree.xPos }.maxByOrNull { it.height }?.height ?: -1
            val highestTreesToTheRight =
                treesInColumn.filter { it.xPos > tree.xPos }.maxByOrNull { it.height }?.height ?: -1

            val visibleFromAbove = highestTreeAbove < tree.height
            val visibleFromBelow = highestTreesBelow < tree.height
            val visibleFromThRight = highestTreesToTheRight < tree.height
            val visibleFromTheLeft = highestTreesToTheLeft < tree.height

            tree to (visibleFromAbove || visibleFromBelow || visibleFromThRight || visibleFromTheLeft)
        }
    }.toMap()
    return visibleTrees
}

private fun part2(forest: List<TreeRow>): Map<ForestTree, Int> {
    val visibleTrees = forest.flatMap { row ->
        row.items.map { tree ->
            val treesInRow = row.items
            val treesInColumn =
                forest.flatMap { treeRow -> treeRow.items.filter { it.yPos == tree.yPos } }//.filter { it.items..yPos != tree.yPos }.flatMap { it.items }.filter { it.xPos == tree.xPos }

            val treesToTheLeft =
                treesInRow.filter { it.yPos < tree.yPos }
            val treesToTheRight =
                treesInRow.filter { it.yPos > tree.yPos }
            val treesAbove =
                treesInColumn.filter { it.xPos < tree.xPos }
            val treesBelow =
                treesInColumn.filter { it.xPos > tree.xPos }

            val diffToHighestTreeAbove = treesAbove.reversed().firstOrNull {it.height >= tree.height}?.let { abs(it.xPos - tree.xPos) } ?: (tree.xPos)
            val diffToHighestTreeBelow =
                treesBelow.firstOrNull {it.height >= tree.height}?.let { abs(it.xPos - tree.xPos) } ?: (row.items.size - (tree.xPos + 1))
            val diffToHighestTreeToTheLeft = treesToTheLeft.reversed().firstOrNull {it.height >= tree.height}?.let { abs(it.yPos - tree.yPos) } ?: (tree.yPos)
            val diffToHighestTreeToTheRight =
                treesToTheRight.firstOrNull {it.height >= tree.height}?.let { abs(it.yPos - tree.yPos) } ?: (treesInRow.size - (tree.yPos + 1))

            val result =
                diffToHighestTreeAbove * diffToHighestTreeBelow * diffToHighestTreeToTheLeft * diffToHighestTreeToTheRight

            tree to result
        }
    }.toMap()
    return visibleTrees
}