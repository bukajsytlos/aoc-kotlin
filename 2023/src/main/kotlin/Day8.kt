import domain.lcm

class Day8(input: String) : StatefulPuzzle<Long, Long>(input) {
    private val lines = input.lines()
    private val instructions = lines[0].map { if (it == 'L') Instruction.LEFT else Instruction.RIGHT }
    private val nodesById = mutableMapOf<String, Node>()

    init {
        val nodeDefPattern = """(\w{3}) = \((\w{3}), (\w{3})\)""".toRegex()
        lines.drop(2)
            .forEach { nodeDef ->
                val parts = nodeDefPattern.matchEntire(nodeDef)!!.groupValues
                val id = parts[1]
                val leftId = parts[2]
                val rightId = parts[3]
                val node = nodesById.getOrPut(id) { Node(id) }
                val leftNode = nodesById.getOrPut(leftId) { Node(leftId) }
                node.left = leftNode
                val rightNode = nodesById.getOrPut(rightId) { Node(rightId) }
                node.right = rightNode
            }
    }

    override fun solvePart1(): Long = nodesById["AAA"]!!.stepsToDestinationNode { it.id == "ZZZ" }

    override fun solvePart2(): Long = nodesById.values
        .filter { it.isStartNode() }
        .map { startNode -> startNode.stepsToDestinationNode { it.isEndNode() } }
        .lcm()

    private fun Node.stepsToDestinationNode(condition: (node: Node) -> Boolean): Long {
        var steps = 0L
        var currentNode = this
        while (!condition(currentNode)) {
            val instruction = instructions[steps.mod(instructions.size)]
            currentNode = currentNode.follow(instruction)
            steps++
        }
        return steps
    }

    data class Node(val id: String) {
        lateinit var left: Node
        lateinit var right: Node
        fun follow(instruction: Instruction) = when (instruction) {
            Instruction.LEFT -> left
            Instruction.RIGHT -> right
        }

        fun isStartNode() = id.endsWith('A')
        fun isEndNode() = id.endsWith('Z')
    }

    enum class Instruction { LEFT, RIGHT }
}