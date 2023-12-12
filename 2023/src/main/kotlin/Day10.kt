import domain.Direction
import domain.Position
import kotlin.math.absoluteValue

class Day10(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val lines = input.lines()
    private val mapSize = lines.size
    private val pipeMap: Array<Array<Char>> = Array(mapSize) { i -> lines[i].map { it }.toTypedArray() }
    private val startPosition: Position = pipeMap.let {
        for (y in it.indices) {
            for (x in it[0].indices) {
                if (pipeMap[y][x] == 'S')
                    return@let Position(x, y)
            }
        }
       error("Could not find start position")
    }
    private val loopPath = mutableListOf<Position>()

    override fun solvePart1(): Int {
        val startPositionPipeType = determineStartPipeType(startPosition)
        pipeMap[startPosition.y][startPosition.x] = startPositionPipeType
        var previousDirection: Direction
        var currentPosition = startPosition
        when (startPositionPipeType) {
            '|' -> {
                previousDirection = Direction.S
            }

            '-' -> {
                previousDirection = Direction.E
            }

            'L' -> {
                previousDirection = Direction.S
            }

            'J' -> {
                previousDirection = Direction.E
            }

            '7' -> {
                previousDirection = Direction.E
            }

            'F' -> {
                previousDirection = Direction.N
            }

            else -> error("imposibru")
        }
        do {
            loopPath.add(currentPosition)
            val nextDirection = currentPosition.valueFrom(pipeMap)!!.follow(previousDirection)
            currentPosition = currentPosition.adjacentIn(nextDirection)
            previousDirection = nextDirection
        } while (currentPosition != startPosition)
        return loopPath.size / 2
    }

    override fun solvePart2(): Int {
        //https://en.wikipedia.org/wiki/Shoelace_formula + https://en.wikipedia.org/wiki/Pick%27s_theorem
        val area = (((loopPath + startPosition).zipWithNext { a, b -> a.x * b.y - b.x * a.y }).sum() / 2).absoluteValue
        return area - (loopPath.size / 2) + 1
    }

    private fun Char.follow(previous: Direction): Direction {
        return when (this) {
            '|' -> when (previous) {
                Direction.N -> Direction.N
                Direction.S -> Direction.S
                else -> error("imposibru")
            }

            '-' -> when (previous) {
                Direction.E -> Direction.E
                Direction.W -> Direction.W
                else -> error("imposibru")
            }

            'L' -> when (previous) {
                Direction.W -> Direction.N
                Direction.S -> Direction.E
                else -> error("imposibru")
            }

            'J' -> when (previous) {
                Direction.E -> Direction.N
                Direction.S -> Direction.W
                else -> error("imposibru")
            }

            '7' -> when (previous) {
                Direction.E -> Direction.S
                Direction.N -> Direction.W
                else -> error("imposibru")
            }

            'F' -> when (previous) {
                Direction.N -> Direction.E
                Direction.W -> Direction.S
                else -> error("imposibru")
            }

            else -> error("imposibru")
        }
    }

    private fun determineStartPipeType(start: Position): Char {
        val leftType = start.adjacentIn(Direction.W).valueFrom(pipeMap)
        val rightType = start.adjacentIn(Direction.E).valueFrom(pipeMap)
        val topType = start.adjacentIn(Direction.N).valueFrom(pipeMap)
        val bottomType = start.adjacentIn(Direction.S).valueFrom(pipeMap)
        return when {
            topType in setOf('|', 'F', '7') && bottomType in setOf('|', 'J', 'L') -> '|'
            leftType in setOf('-', 'F', 'L') && rightType in setOf('-', 'J', '7') -> '-'
            topType in setOf('|', 'F', '7') && rightType in setOf('-', 'J', '7') -> 'L'
            leftType in setOf('-', 'F', 'L') && topType in setOf('|', 'F', '7') -> 'J'
            leftType in setOf('-', 'F', 'L') && bottomType in setOf('|', 'J', 'L') -> '7'
            bottomType in setOf('|', 'J', 'L') && rightType in setOf('-', 'J', '7') -> 'F'
            else -> error("imposibru")
        }
    }
}