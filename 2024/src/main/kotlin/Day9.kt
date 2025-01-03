class Day9(input: String) : StatefulPuzzle<Long, Long>(input) {
    val diskMap = input.map { it.digitToInt() }
    val disk = diskMap.flatMapIndexed { index, d ->
        buildList(d) {
            repeat(d) { add(if (index % 2 == 0) index / 2 else -1) }
        }
    }

    override fun solvePart1(): Long = disk.toTypedArray().defragment().checksum()

    override fun solvePart2(): Long = disk.toTypedArray().compact().checksum()

/*    data class Disk(private val diskMap: List<Int>) {
        private lateinit var first: Node
        private lateinit var last: Node

        init {

        }

        fun fragmentingCompact() {
            nodes.reverse()
        }

        fun compact() {

        }

        fun checksum(): Long = sequence { yield() }
            .fold(0L to 0) { acc, node -> acc.first + node.checksum(acc.second) to acc.second + node.size }
            .first

        sealed class Node(val size: Int, var previous: Node?, var next: Node?) {
            abstract fun checksum(position: Int): Int
            class File(val id: Int, size: Int, previous: Node, next: Node) : Node(size, previous, next) {
                override fun checksum(position: Int): Int = (position..<position + size).sumOf { id * it }

            }

            class Empty(size: Int, previous: Node, next: Node) : Node(size, previous, next) {
                override fun checksum(position: Int) = 0
            }
        }
    }*/

    private fun Array<Int>.defragment(): Array<Int> {
        var headIndex = 0
        var tailIndex = lastIndex
        while (headIndex < tailIndex) {
            val fileId = this[headIndex]
            if (fileId != -1) {
                headIndex++
                continue
            }
            var movedFileId: Int = this[tailIndex]
            if (movedFileId != -1) {
                this[headIndex] = movedFileId
                this[tailIndex] = -1
                headIndex++
            }
            tailIndex--
        }
        return this
    }

    private fun Array<Int>.compact(): Array<Int> {
        var tailIndex = lastIndex
        while (0 < tailIndex) {
            var fileId = this[tailIndex]
            if (fileId == -1) {
                tailIndex--
                continue
            } else {
                var fileSize = 1
                var fileIndex = tailIndex
                while (fileIndex > 0 && fileId == this[fileIndex - 1]) {
                    fileSize++
                    fileIndex--
                }
                tailIndex -= fileSize
                var headIndex = 0
                var freeSpaceSize = 0
                while (headIndex <= tailIndex && freeSpaceSize < fileSize) {
                    val head = this[headIndex]
                    if (head == -1) {
                        freeSpaceSize++
                    } else {
                        freeSpaceSize = 0
                    }
                    headIndex++
                }
                if (freeSpaceSize >= fileSize) {
                    (0..< fileSize).forEach {
                        this[headIndex - 1 - it] = fileId
                        this[tailIndex + 1 + it] = -1
                    }
                }
            }
        }
        return this
    }

    private fun Array<Int>.checksum(): Long = foldIndexed(0L) { index, acc, d -> acc + (if (d != -1) d * index else 0) }
}

