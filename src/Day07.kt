import utils.TreeNode


private data class Folder(
    val path: String,
    val name: String,
    val files: MutableSet<File> = mutableSetOf()
) {
    fun getFilesSize() = files.sumOf { it.size }
}

data class File(
    val name: String,
    val size: Int
)

fun main() {

    fun TreeNode<Folder>.getTotalSizeOfFolder(): Int {
        var totalSize = 0
        this.forEachLevelOrder {
            totalSize += it.value.getFilesSize()
        }
        return totalSize
    }


    fun TreeNode<Folder>.getPath(folderName: String): String {
        return this.value.path + "/" + folderName
    }

    fun parseInput(inputAsList: List<String>, currentFolder: TreeNode<Folder>): TreeNode<Folder> {
        inputAsList.forEachIndexed { index, commandLine ->
            val command = commandLine.split(" ")
            when (command[0]) {
                "$" -> when (command[1]) {
                    "cd" -> {
                        return when (val cdTarget = command[2]) {
                            ".." -> {
                                parseInput(
                                    inputAsList.subList(index + 1, inputAsList.size),
                                    currentFolder.getParent()!!
                                )
                            }

                            else -> {
                                val child =
                                    if (currentFolder.isRoot() && cdTarget == currentFolder.value.name)
                                        currentFolder
                                    else {
                                        currentFolder.search { it.path == currentFolder.getPath(cdTarget) }
                                            ?: throw Exception(
                                                "Trying to change to an unknown folder... Current state: $currentFolder"
                                            )
                                    }
                                parseInput(inputAsList.subList(index + 1, inputAsList.size), child)
                            }
                        }
                    }

                    "ls" -> return parseInput(inputAsList.subList(index + 1, inputAsList.size), currentFolder)
                }

                "dir" -> {
                    val folderName = command[1]
                    val path = currentFolder.getPath(folderName)
                    currentFolder.search { it.path == path }
                        ?: currentFolder.add(TreeNode(value = Folder(name = folderName, path = path)))
                }

                else -> {
                    val size = command[0].toInt()
                    val fileName = command[1]
                    currentFolder.value.files.find { it.name == fileName && it.size == size }
                        ?: currentFolder.value.files.add(File(name = fileName, size = size))
                }
            }
        }
        return currentFolder
    }

    fun findSmallestFolderWithAtLeastSize(size: Int, folder: TreeNode<Folder>): Int? {
        return folder.getNodes().map { it.getTotalSizeOfFolder() }.sorted().find { it >= size }
    }


    fun part1() {
        val root = TreeNode(value = Folder(name = "/", path = ""))
        val parsed = parseInput(readInputAsList("Day07"), root)
        println(parsed.getRoot().getNodes().map { it.getTotalSizeOfFolder() }.filter { it <= 100_000 }.sum())
    }

    fun part2() {
        val totalDiskSpace = 70_000_000
        val spaceNeeded = 30_000_000
        val root = TreeNode(value = Folder(name = "/", path = ""))
        val parsed = parseInput(readInputAsList("Day07"), root)

        val spaceUsed = parsed.getRoot().getTotalSizeOfFolder()
        val freeSpace = totalDiskSpace - spaceUsed
        val minimumSpaceToFree = spaceNeeded - freeSpace

        val result = findSmallestFolderWithAtLeastSize(minimumSpaceToFree, parsed.getRoot())

        println(result)
    }

    part1()
    part2()
}

