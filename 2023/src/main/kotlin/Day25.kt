class Day25(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val nodes = input.lines().let {
        buildMap<String, MutableSet<String>> {
            for (line in it) {
                val (id, connectionsRaw) = line.split(": ")
                val connections = connectionsRaw.split(" ")
                getOrPut(id, ::hashSetOf).addAll(connections)
                connections.forEach { connection -> getOrPut(connection, ::hashSetOf).add(id) }
            }
        }
    }

    override fun solvePart1(): Int {
        val connectionUsage = mutableMapOf<Connection, Int>()
        nodes.keys.forEach { visitAll(it, connectionUsage) }
        val topUsedConnections = connectionUsage.entries.sortedByDescending { it.value }.map { it.key }
        val firstNode = nodes.keys.random()
        for (topUsedConnection in topUsedConnections) {
            nodes.getValue(topUsedConnection.from).remove(topUsedConnection.to)
            nodes.getValue(topUsedConnection.to).remove(topUsedConnection.from)
            val numberOfVisited = visitAll(firstNode, hashMapOf())
            if (numberOfVisited != nodes.size) {
                return (nodes.size - numberOfVisited) * numberOfVisited
            }
        }
        return 0
    }

    override fun solvePart2(): Int = 0

    private fun visitAll(origin: String, connectionsUsage: MutableMap<Connection, Int>): Int {
        val visited = mutableSetOf<String>()
        val toVisit = ArrayDeque<Pair<String, String>>()
        toVisit.addAll(origin.connections())
        visited.add(origin)
        while (toVisit.isNotEmpty()) {
            val (from, to) = toVisit.removeFirst()
            val connection = Connection.connect(from, to)
            connectionsUsage.compute(connection) { _,v -> if (v == null) 1 else v + 1}
            val toVisitConnections = to.connections().filter { it.second !in visited }
            toVisit.addAll(toVisitConnections)
            visited.addAll(toVisitConnections.map { it.second })
        }
        return visited.size
    }
    private fun String.connections() = nodes.getValue(this).map { this to it }
    
    data class Connection private constructor(val from: String, val to: String) {
        companion object {
            fun connect(node1: String, node2: String): Connection {
                val (from, to) = listOf(node1, node2).sorted()
                return Connection(from, to)
            }
        }
    }
}

