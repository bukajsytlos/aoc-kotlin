import domain.Direction
import domain.Position
import java.util.PriorityQueue

class Day24(input: String) : StatefulPuzzle<Int, Int>(input) {

    private val lines = input.lines()
    private val obstacles = lines
        .flatMapIndexed { y: Int, line: String ->
            line.mapIndexedNotNull { x, c ->
                when (c) {
                    '#' -> Obstacle.Wall(Position(x, y))
                    '^' -> Obstacle.Blizzard(Position(x, y), Direction.N)
                    'v' -> Obstacle.Blizzard(Position(x, y), Direction.S)
                    '<' -> Obstacle.Blizzard(Position(x, y), Direction.W)
                    '>' -> Obstacle.Blizzard(Position(x, y), Direction.E)
                    else -> null
                }
            }
        }.toSet()
    private val start = Position(lines.first().indexOf('.'), 0)
    private val end = Position(lines.last().indexOf('.'), lines.indices.last)

    private val basinWidth = obstacles.maxOf { it.position.x } + 1
    private val basinHeight = obstacles.maxOf { it.position.y } + 1

    override fun solvePart1(): Int {
        val tripPart1 = getShortestDistance(
            start,
            end,
            obstacles,
            basinWidth,
            basinHeight
        )
        return tripPart1.first
    }

    override fun solvePart2(): Int {
        val tripPart1 = getShortestDistance(
            start,
            end,
            obstacles,
            basinWidth,
            basinHeight
        )
        val tripPart2 = getShortestDistance(
            end,
            start,
            tripPart1.second,
            basinWidth,
            basinHeight
        )
        val tripPart3 = getShortestDistance(
            start,
            end,
            tripPart2.second,
            basinWidth,
            basinHeight
        )
        return tripPart1.first + tripPart2.first + tripPart3.first
    }
}

fun getShortestDistance(from: Position, to: Position, initObstacles: Set<Obstacle>, basinWidth: Int, basinHeight: Int): Pair<Int, Set<Obstacle>> {

    val xRange = 0 until basinWidth
    val yRange = 0 until basinHeight
    val queue = PriorityQueue<Triple<Position, Int, Set<Obstacle>>> { p1, p2 ->
        p1.first.manhattanDistanceTo(to) + p1.second - (p2.first.manhattanDistanceTo(to) + p2.second)
    }
    val seenStates = mutableSetOf<Pair<Position, Int>>()
    queue.offer(Triple(from, 0, initObstacles))
    while (queue.isNotEmpty()) {
        val (position, elapsedTime, obstacles) = queue.poll()
        if (position == to) {
            return elapsedTime to obstacles
        }
        if (position to elapsedTime in seenStates) continue
        seenStates.add(position to elapsedTime)
        val nextStateOfObstacles = obstacles.next(basinWidth, basinHeight)
        val obstaclePositions = nextStateOfObstacles.map { it.position }.toSet()
        (position.adjacents() + position).filter { it.x in xRange && it.y in yRange }.forEach {
            if (it !in obstaclePositions)
                queue.offer(Triple(it, elapsedTime + 1, nextStateOfObstacles))
        }
    }
    error("no path found")
}

fun Set<Obstacle>.next(basinWidth: Int, basinHeight: Int): Set<Obstacle> = map {
    when (it) {
        is Obstacle.Blizzard -> it.move(basinWidth, basinHeight)
        is Obstacle.Wall -> it
    }
}.toSet()

sealed class Obstacle(val position: Position) {
    class Wall(position: Position) : Obstacle(position)

    class Blizzard(position: Position, private val direction: Direction) : Obstacle(position) {
        fun move(basinWidth: Int, basinHeight: Int): Blizzard {
            val newPosition = when (direction) {
                Direction.N -> {
                    if (position.y + direction.dy < 1) Position(position.x, basinHeight - 2)
                    else position.adjacentIn(direction)
                }

                Direction.S -> {
                    if (position.y + direction.dy > basinHeight - 2) Position(position.x, 1)
                    else position.adjacentIn(direction)
                }

                Direction.W -> {
                    if (position.x + direction.dx < 1) Position(basinWidth - 2, position.y)
                    else position.adjacentIn(direction)
                }

                Direction.E -> {
                    if (position.x + direction.dx > basinWidth - 2) Position(1, position.y)
                    else position.adjacentIn(direction)
                }

                else -> error("unexpected direction")
            }
            return Blizzard(newPosition, direction)
        }
    }
}