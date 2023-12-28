import domain.Direction
import domain.Position

typealias Vector = Pair<Position, Direction>

class Day16(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val contraption = input.lines()
    
    override fun solvePart1(): Int = beaming(Position(0,0) to Direction.E).size

    override fun solvePart2(): Int =
        (contraption.indices.map { Position(0, it) to Direction.E } +
            contraption.indices.map { Position(contraption[0].length - 1, it) to Direction.W } +
            contraption[0].indices.map { Position(it, 0) to Direction.S } +
            contraption[0].indices.map { Position(it, contraption.size - 1) to Direction.N })
            .maxOf { beaming(it).size }

    private fun beaming(startVector: Vector): Collection<Position> {
        val visited = mutableSetOf<Vector>()
        val toVisit = ArrayDeque<Vector>()
        visited.add(startVector)
        toVisit.add(startVector)
        while (toVisit.isNotEmpty()) {
            val (from, direction) = toVisit.removeLast()
            for (newDirection in contraption[from.y][from.x].to(direction)) {
                val newBeamPosition = from.adjacentIn(newDirection)
                val newVector = newBeamPosition to newDirection
                if (newVector in visited || newVector.first.y !in contraption.indices || newVector.first.x !in contraption[newVector.first.y].indices) continue
                toVisit.add(newVector) && visited.add(newVector)
            }
        }
        return visited.map { it.first }.toSet()
    }
    
    private fun Char.to(from: Direction): Set<Direction> {
        return when (from to this) {
            Direction.N to '/' -> setOf(Direction.E)
            Direction.S to '/' -> setOf(Direction.W)
            Direction.W to '/' -> setOf(Direction.S)
            Direction.E to '/' -> setOf(Direction.N)
            Direction.N to '\\' -> setOf(Direction.W)
            Direction.S to '\\' -> setOf(Direction.E)
            Direction.W to '\\' -> setOf(Direction.N)
            Direction.E to '\\' -> setOf(Direction.S)
            Direction.N to '-' -> setOf(Direction.W, Direction.E)
            Direction.S to '-' -> setOf(Direction.W, Direction.E)
            Direction.W to '-' -> setOf(Direction.W)
            Direction.E to '-' -> setOf(Direction.E)
            Direction.N to '|' -> setOf(Direction.N)
            Direction.S to '|' -> setOf(Direction.S)
            Direction.W to '|' -> setOf(Direction.N, Direction.S)
            Direction.E to '|' -> setOf(Direction.N, Direction.S)
            else -> setOf(from)
        }
    }    
}