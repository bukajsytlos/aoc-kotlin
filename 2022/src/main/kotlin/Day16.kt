class Day16(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val connections = mutableMapOf<String, List<String>>()
    private val valves = input.lines().associate {
        val name = it.substringAfter("Valve ").substringBefore(" has flow ")
        val flowRate = it.substringAfter(" has flow rate=").substringBefore(";").toInt()
        val destinations = it.substringAfter("to valve").substringAfter(" ").split(", ")
        connections[name] = destinations
        name to Valve(name, flowRate)
    }
    private val distanceMatrix = buildDistanceMatrix(connections)

    private val valvesWithPositiveFlowRate = valves.filter { it.value.flowRate > 0 }

    override fun solvePart1(): Int = findMaximumPressureReleased(
        "AA",
        30,
        0,
        valvesWithPositiveFlowRate,
        valvesWithPositiveFlowRate.keys,
        distanceMatrix
    )

    override fun solvePart2(): Int = (1 until (1 shl (valvesWithPositiveFlowRate.size - 1))).maxOf {
        val closedValvesToVisitByMe = mutableSetOf<String>()
        val closedValvesToVisitByElephant = mutableSetOf<String>()
        valvesWithPositiveFlowRate.keys.forEachIndexed { i, s ->
            if (it and (1 shl i) > 0) {
                closedValvesToVisitByElephant += s
            } else {
                closedValvesToVisitByMe += s
            }
        }
        findMaximumPressureReleased("AA", 26, 0, valves, closedValvesToVisitByMe, distanceMatrix) + findMaximumPressureReleased("AA", 26, 0, valves, closedValvesToVisitByElephant, distanceMatrix)
    }
}

fun findMaximumPressureReleased(
    from: String,
    timeLeft: Int,
    alreadyReleasedPressure: Int,
    valves: Map<String, Valve>,
    closedValves: Set<String>,
    distanceMatrix: Map<Pair<String, String>, Int>
): Int {
    return if (timeLeft < 2 || closedValves.isEmpty()) {
        alreadyReleasedPressure
    } else {
        closedValves.maxOf {
            val distance = distanceMatrix[from to it]!!
            val newTimeLeft = timeLeft - distance - 1
            findMaximumPressureReleased(it, newTimeLeft, alreadyReleasedPressure + newTimeLeft * valves[it]!!.flowRate, valves, closedValves - it, distanceMatrix)
        }
    }
}

fun buildDistanceMatrix(connections: Map<String, List<String>>): Map<Pair<String, String>, Int> {
    val matrix = mutableMapOf<Pair<String, String>, Int>()
    connections.keys.forEach { c1 ->
        connections.keys.forEach { c2 ->
            val shortestDistance = getShortestDistance(c1, c2, connections)
            matrix[c1 to c2] = shortestDistance
            matrix[c2 to c1] = shortestDistance
        }
    }
    return matrix
}

fun getShortestDistance(from: String, to: String, connections: Map<String, List<String>>): Int {
    val visited = mutableSetOf(from)
    val queue = ArrayDeque<Pair<String, Int>>()
    queue.addLast(from to 0)
    while (queue.isNotEmpty()) {
        val (valve, distance) = queue.removeFirst()
        if (valve == to) {
            return distance
        }
        visited.add(valve)
        connections[valve]!!.forEach {
            if (!visited.contains(it))
                queue.addLast(it to distance + 1)
        }
    }
    error("no path found")
}

data class Valve(val name: String, val flowRate: Int)