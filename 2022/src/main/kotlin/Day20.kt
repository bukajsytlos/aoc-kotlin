object Day20 : StatelessPuzzle<Long, Long>() {

    override fun solvePart1(input: String): Long {
        val sourceNodes = input.lines()
            .map { it.toLong() }
            .map { Node(it) }
        sourceNodes.wire()
        sourceNodes.mix(1, 1)

        val zeroNode = sourceNodes.first { it.value == 0L }
        return zeroNode.groveCoordinates(1)
    }

    override fun solvePart2(input: String): Long {
        val sourceNodes = input.lines()
            .map { it.toLong() }
            .map { Node(it) }
        sourceNodes.wire()
        val decryptionKey = 811589153L
        sourceNodes.mix(decryptionKey, 10)

        val zeroNode = sourceNodes.first { it.value == 0L }
        return  zeroNode.groveCoordinates(decryptionKey)
    }
}

fun Node.groveCoordinates(decryptionKey: Long): Long = (nextNth(1000).value + nextNth(2000).value + nextNth(3000).value) * decryptionKey

fun List<Node>.mix(decryptionKey: Long, times: Int) = repeat(times) {
    forEach {
        val shift = (it.value * decryptionKey).mod(this.size - 1)
        it.moveAfter(it.nextNth(shift))
    }
}

fun List<Node>.wire() {
    this.windowed(2).forEach {
        it[0].next = it[1]
        it[1].previous = it[0]
    }.also {
        this.first().previous = this.last()
        this.last().next = this.first()
    }
}

class Node(val value: Long) {
    lateinit var previous: Node
    lateinit var next: Node

    fun moveAfter(node: Node) {
        if (node == this) {
            return
        }

        previous.next = next
        next.previous = previous
        node.next.previous = this
        this.next = node.next
        node.next = this
        this.previous = node
    }

    fun nextNth(n: Int): Node {
        var node = this
        repeat(n) {
            node = node.next
        }
        return node
    }
}