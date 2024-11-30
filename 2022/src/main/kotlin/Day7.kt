import domain.Position
import java.io.File
import kotlin.math.abs
import kotlin.math.sign

class Day7(input: String) : StatefulPuzzle<Long, Int>(input) {
    private val root = Node.Directory("")
    private var currentDirectory: Node.Directory = root

    init {
        val iterator = input.lineSequence().iterator()
        while (iterator.hasNext()) {
            val lastCommand = iterator.next()
            when {
                lastCommand == "$ cd /" -> currentDirectory = root
                lastCommand.startsWith("$ cd ..") -> currentDirectory = currentDirectory.parent!!
                lastCommand.startsWith("$ cd ") -> currentDirectory =
                    currentDirectory.getDirectory(lastCommand.substringAfter("$ cd "))

                lastCommand.startsWith("$ ls") -> {}
                else -> currentDirectory.add(lastCommand.toNode(currentDirectory))
            }
        }
    }

    override fun solvePart1(): Long = getSize(root)

    override fun solvePart2(): Int = getNodesOfAtLeastSize(root, 30_000_000L - (70_000_000L - root.getSize())).minOf { it.getSize() }

    fun getSize(node: Node.Directory): Long {
        var totalSize = 0L
        val filesSize = node.getSize()
        if (filesSize <= 100_000) {
            totalSize += filesSize
        }
        totalSize += node.getDirectories().sumOf { getSize(it) }
        return totalSize
    }

    fun getNodesOfAtLeastSize(node: Node.Directory, minSize: Long): List<Node.Directory> {
        val matchedNodes = mutableListOf<Node.Directory>()
        val filesSize = node.getSize()
        if (filesSize >= minSize) {
            matchedNodes.add(node)
        }
        node.getDirectories().forEach {
            matchedNodes.addAll(getNodesOfAtLeastSize(it, minSize))
        }
        return matchedNodes
    }

    sealed class Node(val parent: Directory? = null) {
        abstract fun getSize(): Int
        class Directory(
            val name: String,
            private val children: MutableList<Node> = mutableListOf(),
            parent: Directory? = null
        ) : Node(parent) {
            override fun getSize(): Int = children.sumOf { it.getSize() }
            fun add(node: Node) = children.add(node)
            fun getDirectories(): List<Directory> = children.filterIsInstance(Directory::class.java).toList()
            fun getDirectory(name: String) = getDirectories().first { it.name == name }
        }

        class File(
            val name: String, val fileSize: Int,
            parent: Directory? = null
        ) : Node(parent) {
            override fun getSize(): Int = fileSize
        }
    }

    fun String.toNode(currentDirectory: Node.Directory): Node = when {
        startsWith("dir ") -> Node.Directory(name = substringAfter("dir "), parent = currentDirectory)
        else -> Node.File(
            name = substringAfter(" "),
            fileSize = substringBefore(" ").toInt(),
            parent = currentDirectory
        )
    }
}