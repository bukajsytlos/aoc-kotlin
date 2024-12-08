import domain.Direction
import domain.Position

class Day6(input: String) : StatefulPuzzle<Int, Int>(input) {
    lateinit var initGuardPosition: Guard
    val obstaclePositions: MutableSet<Position> = mutableSetOf()
    val mapSize = input.lines().size
    init {
        input.lines().forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    '#' -> obstaclePositions.add(Position(x, y))
                    '^' -> initGuardPosition = Guard(Position(x, y), Direction.N)
                }
            }
        }    
    }
    val visitedPositions = walk(obstaclePositions, initGuardPosition)?.map { it.position }?.distinct()

    override fun solvePart1(): Int {
        return visitedPositions?.size ?: 0
    }
    
    override fun solvePart2(): Int = visitedPositions?.count {
        walk(obstaclePositions + it, initGuardPosition) == null
    } ?: 0

    fun walk(obstaclesMap: Set<Position>, start: Guard): Set<Guard>? {
        val seen = mutableSetOf<Guard>()
        var guard = start
        while (true) {
            if (guard in seen) return null
            if (guard.position.x < 0 || guard.position.x > mapSize - 1 || guard.position.y < 0 || guard.position.y > mapSize - 1) return seen
            seen += guard
            guard = guard.move().let { if (it.position in obstaclesMap) guard.turn() else it }
        }
    }
    
    data class Guard(val position: Position, val direction: Direction) {
        fun move() = Guard(position.adjacentIn(direction), direction)
        fun turn() = Guard(position, when (direction) {
            Direction.N -> Direction.E
            Direction.E -> Direction.S
            Direction.S -> Direction.W
            Direction.W -> Direction.N
            else -> error("invalid direction")
        })
    }
}
