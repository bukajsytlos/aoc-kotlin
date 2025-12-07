import domain.Direction
import domain.Point

class Day7(input: String) : StatefulPuzzle<Int, Long>(input) {
    val lines = input.lines()
    var startPosition = Point(0, 0)
    val splitterPositions = mutableSetOf<Point>()
    init {
        Array(lines.size) { y ->
            lines[y].forEachIndexed { x, c ->
                when (c) {
                    '^' -> { splitterPositions.add(Point(x.toLong(), y.toLong())) }
                    'S' -> { startPosition = Point(x.toLong(), y.toLong()) }
                }
            }
        }
    }
    val maxY = lines.lastIndex.toLong()

    override fun solvePart1(): Int {
        val visitedSplitters = mutableSetOf<Point>()
        val toVisitStack = ArrayDeque<Point>()
        toVisitStack.add(startPosition)
        do {
            findSplitter(toVisitStack.removeLast(), maxY)
                ?.let { if (it in visitedSplitters) null else it }
                ?.also { visitedSplitters.add(it) }
                ?.let {
                    toVisitStack.add(it.adjacentIn(Direction.W))
                    toVisitStack.add(it.adjacentIn(Direction.E))
                }
        } while (toVisitStack.isNotEmpty())
        return visitedSplitters.size
    }

    override fun solvePart2(): Long {
        return numberOfTimelines(startPosition, maxY, mutableMapOf())
    }

    fun numberOfTimelines(fromPosition: Point, end: Long, visited: MutableMap<Point, Long>): Long {
        visited[fromPosition]?.let { return it }
        val numberOfTimelines = findSplitter(fromPosition, end)?.let {
            (numberOfTimelines(it.adjacentIn(Direction.W), end, visited)
            + numberOfTimelines(it.adjacentIn(Direction.E), end, visited))
        } ?: 1L
        visited[fromPosition] = numberOfTimelines
        return numberOfTimelines
    }

    fun findSplitter(fromPosition: Point, end: Long): Point? {
        return (fromPosition.y ..< end).asSequence()
            .map { Point(fromPosition.x, it) }
            .firstOrNull { it in splitterPositions }
    }
}


