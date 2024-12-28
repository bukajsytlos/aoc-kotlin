import domain.Direction
import domain.Position
import domain.findPositionsOf
import domain.setValueTo
import domain.valueFrom
import kotlin.collections.contains

class Day15(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val inputParts = input.split("\n\n")
    private val robotMoves = inputParts[1].replace("\n", "").map { Direction.of(it) }
    private val lines = inputParts[0].lines()
    private val mapSize = lines.size

    private val warehouseMap: Array<Array<Char>> = Array(mapSize) { i -> lines[i].map { it }.toTypedArray() }
    private val initRobotPosition = warehouseMap.findPositionsOf('@').first()

    private val warehouseMap2: Array<Array<Char>> = Array(mapSize) { i -> lines[i].flatMap { it.widen() }.toTypedArray() }
    private val initRobotPosition2 = warehouseMap2.findPositionsOf('@').first()

    override fun solvePart1(): Int {
        var robotPosition = initRobotPosition
        for (moveDirection in robotMoves) {
            robotPosition = checkAndMove(robotPosition, moveDirection)
        }
        return warehouseMap.findPositionsOf('O').sumOf { it.toGPS() }
    }

    private fun checkAndMove(position: Position, direction: Direction): Position {
        val moveTo = position.adjacentIn(direction)
        return if (canMove(moveTo, direction)) move(position, direction) else position
    }

    private fun move(position: Position, direction: Direction): Position {
        val moveTo = position.adjacentIn(direction)
        if (warehouseMap.valueFrom(moveTo)!! == 'O') {
            move(moveTo, direction)
        }
        warehouseMap.setValueTo(moveTo, warehouseMap.valueFrom(position)!!)
        warehouseMap.setValueTo(position, '.')
        return moveTo
    }

    private fun canMove(position: Position, direction: Direction): Boolean {
        return when (warehouseMap.valueFrom(position)) {
            '.' -> true
            'O' -> canMove(position.adjacentIn(direction), direction)
            else -> false
        }
    }

    override fun solvePart2(): Int {
        var robotPosition = initRobotPosition2
        for (moveDirection in robotMoves) {
            robotPosition = checkAndMove2(robotPosition, moveDirection)
        }
        return warehouseMap2.findPositionsOf('[').sumOf { it.toGPS() }
    }

    private fun checkAndMove2(position: Position, direction: Direction): Position {
        val moveTo = position.adjacentIn(direction)
        return if (canMove2(moveTo, direction)) move2(position, direction) else position
    }

    private fun move2(position: Position, direction: Direction): Position {
        val moveTo = position.adjacentIn(direction)
        val moveToEntity = warehouseMap2.valueFrom(moveTo)!!
        if (moveToEntity in setOf('[', ']') && direction in setOf(Direction.W, Direction.E)) {
            move2(moveTo, direction)
        } else {
            if (moveToEntity == '[') {
                move2(moveTo, direction)
                move2(moveTo.adjacentIn(Direction.E), direction)
            }
            if (moveToEntity == ']') {
                move2(moveTo, direction)
                move2(moveTo.adjacentIn(Direction.W), direction)
            }
        }
        warehouseMap2.setValueTo(moveTo, warehouseMap2.valueFrom(position)!!)
        warehouseMap2.setValueTo(position, '.')
        return moveTo
    }
    private fun canMove2(position: Position, direction: Direction): Boolean {
        val valueFromPosition = warehouseMap2.valueFrom(position)
        return when {
            valueFromPosition == '.' -> true
            valueFromPosition in setOf('[', ']') && direction in setOf(Direction.W, Direction.E) -> canMove2(position.adjacentIn(direction, 2), direction)
            valueFromPosition == '[' -> canMove2(position.adjacentIn(direction), direction) && canMove2(position.adjacentIn(direction).adjacentIn(Direction.E), direction)
            valueFromPosition == ']' -> canMove2(position.adjacentIn(direction), direction) && canMove2(position.adjacentIn(direction).adjacentIn(Direction.W), direction)
            else -> false
        }
    }

    fun Position.toGPS() = x + y * 100

    fun Char.widen(): Iterable<Char> = when (this) {
        '.' -> listOf('.', '.')
        '#' -> listOf('#', '#')
        'O' -> listOf('[', ']')
        '@' -> listOf('@', '.')
        else -> error("unknown map entity")
    }
}

