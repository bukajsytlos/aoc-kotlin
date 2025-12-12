object Day11 : StatelessPuzzle<Long, Long>() {

    override fun solvePart1(input: String): Long {
        val nodes = nodes(input)
        val nodesByName = mapByNames(nodes)
        val startNode = nodesByName["you"]!!
        return findPathTo(startNode, "out", nodesByName, mutableMapOf())
    }

    override fun solvePart2(input: String): Long {
        val nodes = nodes(input)
        val nodesByName = mapByNames(nodes)
        val startNode = nodesByName["svr"]!!
        return findPathToVia(startNode, "out", false, false, nodesByName, mutableMapOf())
    }

    private fun findPathTo(
        startNode: Node,
        endNodeName: String,
        nodesByName: Map<String, Node>,
        visited: MutableMap<String, Long>
    ): Long {
        visited[startNode.name]?.let { return it }
        if (startNode.name == endNodeName) {
            return 1L
        }
        val numOfPaths = startNode.connections.sumOf { findPathTo(nodesByName[it]!!, endNodeName, nodesByName, visited) }
        visited[startNode.name] = numOfPaths
        return numOfPaths
    }

    private fun findPathToVia(
        startNode: Node,
        endNodeName: String,
        visitedFft: Boolean,
        visitedDac: Boolean,
        nodesByName: Map<String, Node>,
        visited: MutableMap<Pair<Pair<String, Boolean>, Boolean>, Long>
    ): Long {
        visited[startNode.name to visitedFft to visitedDac]?.let { return it }
        if (startNode.name == endNodeName) {
            return if (visitedDac && visitedFft) 1L else 0L
        }
        val visitedFft1 = visitedFft || startNode.name == "fft"
        val visitedDac1 = visitedDac || startNode.name == "dac"
        val numOfPaths = startNode.connections.sumOf { findPathToVia(
            nodesByName[it]!!, endNodeName,
            visitedFft1, visitedDac1, nodesByName, visited
        ) }
        visited[startNode.name to visitedFft to visitedDac] = numOfPaths
        return numOfPaths
    }

    private fun nodes(input: String): List<Node> = input.lines()
        .map { Node(it.substringBefore(": "),
            it.substringAfter(": ").split(" ")
        ) }

    private fun mapByNames(nodes: List<Node>): Map<String, Node> =
        nodes.associateBy { it.name } + ("out" to Node("out", emptyList()))

    data class Node(val name: String, val connections: List<String>)
}
