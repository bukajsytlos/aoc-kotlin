import kotlin.math.abs
import kotlin.math.sign

class Day9(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val moves = input.lines().map {
        MoveCommand(it.substringBefore(" ").toDirection(), it.substringAfter(" ").toInt())
    }

    override fun solvePart1(): Int = Rope(List(2) { Knot(Position(0, 0)) }).apply {
        moves.forEach { this.moveHead(it) }
    }.visitedTailPositions.size

    override fun solvePart2(): Int = Rope(List(10) { Knot(Position(0, 0)) }).apply {
        moves.forEach { this.moveHead(it) }
    }.visitedTailPositions.size

    data class Rope(val knots: List<Knot>) {
        val visitedTailPositions = mutableSetOf<Position>()
        fun moveHead(moveCommand: MoveCommand) {
            repeat(moveCommand.amount) {
                knots[0].moveTo(moveCommand.direction)
                knots.windowed(2).map { it.last().follow(it.first()) }
                visitedTailPositions.add(knots.last().position)
            }
        }
    }

    data class Knot(var position: Position) {
        fun moveTo(direction: Direction) {
            position = Position(position.x + direction.dx, position.y + direction.dy)
        }
        fun follow(leaderKnot: Knot) {
            moveTo(position.directionTo(leaderKnot.position))
        }
    }

    data class Position(val x: Int, val y: Int) {
        fun directionTo(relative: Position): Direction = if (abs(relative.x - x) > 1 || abs(relative.y - y) > 1) {
            Direction.values().first { it.dx == (relative.x - x).sign && it.dy == (relative.y - y).sign }
        } else {
            Direction.NON
        }
    }

    data class MoveCommand(val direction: Direction, val amount: Int)

    enum class Direction(val dx: Int, val dy: Int) {
        LEFT(-1, 0),
        RIGHT(1, 0),
        UP(0, 1),
        DOWN(0, -1),
        UL(-1, 1),
        UR(1, 1),
        DL(-1, -1),
        DR(1, -1),
        NON(0, 0);
    }

    fun String.toDirection() = when (this) {
        "L" -> Direction.LEFT
        "R" -> Direction.RIGHT
        "U" -> Direction.UP
        "D" -> Direction.DOWN
        else -> error("unknown direction")
    }
}