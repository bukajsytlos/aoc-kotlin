import domain.Direction
import domain.Position
import domain.findPositionsOf
import domain.valueFrom
import java.util.ArrayDeque
import java.util.PriorityQueue

class Day16(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val maze: Array<Array<Char>> = input.asTyped2DArray { it }
    private val reindeerStartPosition = maze.findPositionsOf('S').first()
    private val destinationPosition = maze.findPositionsOf('E').first()
    val scoreByPosition = mutableMapOf<Position, MutableMap<Direction, Int>>()

    override fun solvePart1(): Int {
        val visited = mutableSetOf<Reindeer>()
        val toEvaluate = PriorityQueue<Pair<Reindeer, Int>> { o1, o2 -> o1.second - o2.second }

        toEvaluate.add(Reindeer(reindeerStartPosition, Direction.E) to 0)
        scoreByPosition.getOrPut(reindeerStartPosition) { mutableMapOf() }.put(Direction.E, 0)

        while (toEvaluate.isNotEmpty()) {
            val (reindeer, score) = toEvaluate.poll()
            visited.add(reindeer)
            val possibleActions = buildList {
                listOf(reindeer.move() to 1, reindeer.turnLeft().move() to 1001, reindeer.turnRight().move() to 1001)
                    .filter { maze.valueFrom(it.first.position) != '#' }
                    .forEach { add(it) }
            }
            for (action in possibleActions) {
                val (newReindeerState, cost) = action
                if (newReindeerState in visited) continue
                val newScore = score + cost
                if ((scoreByPosition[newReindeerState.position]?.let { it[newReindeerState.direction] }?: Int.MAX_VALUE) <= newScore) continue
                toEvaluate.offer(newReindeerState to newScore)
                scoreByPosition.getOrPut(newReindeerState.position) { mutableMapOf<Direction, Int>() }.put(newReindeerState.direction, newScore)
            }
        }
            return scoreByPosition.filter { it.key == destinationPosition }.flatMap { it.value.values }.minOf { it }
    }

    override fun solvePart2(): Int {
        //backtrack
        val bestPathPositions = mutableSetOf<Position>()
        val minScore = scoreByPosition[destinationPosition]!!.minOf { it.value }
        val toVisit = ArrayDeque<Pair<Position, Int>>()
        toVisit.add(destinationPosition to minScore)
        while (toVisit.isNotEmpty()) {
            val (position, score) = toVisit.removeFirst()
            bestPathPositions.add(position)
            for (direction in Direction.perpendicular()) {
                val candidatePosition = position.adjacentIn(direction)
                val movedCandidateScores = scoreByPosition.getOrDefault(candidatePosition, emptyMap<Direction, Int>()).values.filter { it + 1 == score || it + 1001 == score}.toSet()
                movedCandidateScores.forEach { toVisit.add(candidatePosition to it) }
            }
        }
        return bestPathPositions.size
    }

    data class Reindeer(val position: Position, val direction: Direction) {
        fun turnLeft(): Reindeer = Reindeer(position, direction.left90())
        fun turnRight(): Reindeer = Reindeer(position, direction.right90())
        fun move(): Reindeer = Reindeer(position.adjacentIn(direction), direction)
    }
}
