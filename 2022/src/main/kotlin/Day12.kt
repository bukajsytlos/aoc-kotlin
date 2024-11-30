import domain.Position
import java.util.PriorityQueue

class Day12(input: String) : StatefulPuzzle<Int, Int>(input) {
    val lines = input.lines()
    var startPosition = Position(0, 0)
    var endPosition = Position(0, 0)
    val aPositions = mutableSetOf<Position>()
    val heightMap: Array<IntArray> = Array(lines.size) { i -> lines[i].mapIndexed { x, c ->
        when (c) {
            'a' -> { aPositions.add(Position(x, i)); 'a' }
            'S' -> { startPosition = Position(x, i); 'a' }
            'E' -> { endPosition = Position(x, i); 'z' }
            else -> c
        }.code - 'a'.code
    }.toIntArray() }
    init {
        aPositions + startPosition
    }

    override fun solvePart1(): Int = findShortestPathMap(heightMap, listOf(startPosition))[endPosition.y][endPosition.x]

    override fun solvePart2(): Int = findShortestPathMap(heightMap, aPositions)[endPosition.y][endPosition.x]
}

private fun findShortestPathMap(
    heightMap: Array<IntArray>,
    startPositions: Collection<Position>
): Array<IntArray> {
    val stepsMap: Array<IntArray> = Array(heightMap.size) { IntArray(heightMap[it].size) { Int.MAX_VALUE } }
    val pathParents: MutableMap<Position, Position> = mutableMapOf()

    val unvisited: MutableSet<Position> = (0 until heightMap.size)
        .flatMap { y -> (0 until heightMap[y].size)
            .map { x -> Position(x, y) }
        }
        .toMutableSet()
    val visiting: PriorityQueue<Pair<Position, Int>> = PriorityQueue { o1, o2 -> o1.second - o2.second }

    startPositions.forEach {
        visiting.add(it to 0)
        stepsMap[it.y][it.x] = 0
    }

    while (visiting.isNotEmpty()) {
        val (nextCandidate, parentSteps) = visiting.poll()
        for (adjacent in heightMap.adjacentsOf(nextCandidate)) {
            if (adjacent in unvisited) {
                val adjacentHeight = heightMap[adjacent.y][adjacent.x]
                val nextCandidateHeight = heightMap[nextCandidate.y][nextCandidate.x]
                val adjacentMinimumSteps = stepsMap[adjacent.y][adjacent.x]
                if (adjacentHeight - 1 <= nextCandidateHeight && parentSteps + 1 < adjacentMinimumSteps) {
                    stepsMap[adjacent.y][adjacent.x] = parentSteps + 1
                    pathParents[adjacent] = nextCandidate
                    visiting.offer(adjacent to parentSteps + 1)
                }
            }
        }
        unvisited.remove(nextCandidate)
    }
    return stepsMap
}

fun Array<IntArray>.adjacentsOf(position: Position): Set<Position> {
    val points = mutableSetOf<Position>()
    if (position.x > 0) points.add(Position(position.x - 1, position.y))
    if (position.x < this[position.y].size) points.add(Position(position.x + 1, position.y))
    if (position.y > 0) points.add(Position(position.x, position.y - 1))
    if (position.y < this.size) points.add(Position(position.x, position.y + 1))
    return points
}