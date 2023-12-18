import domain.Direction
import domain.Point
import kotlin.math.absoluteValue

object Day18 : StatelessPuzzle<Long, Long>() {
    private val instructionPattern = """(\w) (\d+).*""".toRegex()

    override fun solvePart1(input: String): Long = input.lines().map {
        val match = instructionPattern.matchEntire(it)!!.groupValues
        val direction = when (match[1]) {
            "U" -> Direction.N
            "R" -> Direction.E
            "D" -> Direction.S
            else -> Direction.W
        }
        direction to match[2].toInt()
    }.calculatePoolSize()

    @OptIn(ExperimentalStdlibApi::class)
    override fun solvePart2(input: String): Long = input.lines().map {
        val code = it.substringAfter("#").substringBefore(")")
        val directionCode = code.last()
        val direction = when (directionCode) {
            '3' -> Direction.N
            '0' -> Direction.E
            '1' -> Direction.S
            else -> Direction.W
        }
        direction to code.substring(0, code.length - 1).hexToInt()
    } .calculatePoolSize()
    
    private fun List<Pair<Direction, Int>>.calculatePoolSize(): Long = (this.runningFold(Point(0, 0)) { previousPosition, instruction -> 
            previousPosition.adjacentIn(instruction.first, instruction.second)
        }.zipWithNext { a, b -> a.x * b.y - b.x * a.y }.sum().absoluteValue + this.sumOf { it.second }) / 2 + 1
}