package day23

import StatefulPuzzle
import domain.Direction
import domain.Position
import kotlin.math.max
import kotlin.math.min

class Day23(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val elves = input.lines().flatMapIndexed { y: Int, line: String ->
        line.mapIndexedNotNull { x, c ->
            if (c == '#') Elf(Position(x, y)) else null
        }
    }.toSet()
    override fun solvePart1(): Int {
        repeat(10) {
            elves.round(it)
        }
        var minX = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var minY = Int.MAX_VALUE
        var maxY = Int.MIN_VALUE
        elves.forEach {
            minX = min(minX, it.position.x)
            maxX = max(maxX, it.position.x)
            minY = min(minY, it.position.y)
            maxY = max(maxY, it.position.y)
        }
        return (maxX - minX + 1) * (maxY - minY + 1) - elves.size
    }

    override fun solvePart2(): Int {
        var numberOfRounds = 10
        while (elves.round(numberOfRounds)) {
            numberOfRounds++
        }
        return numberOfRounds + 1
    }
}

fun Set<Elf>.round(index: Int): Boolean {
    var hasAnyElfMoved = false
    val elvesPositions = this.map { it.position }.toSet()
    map { elf -> elf.proposeMove(index, elvesPositions) to elf }
        .groupBy({ it.first }, { it.second })
        .filter { it.value.size == 1 }
        .forEach {
            if (it.value[0].position != it.key) {
                it.value[0].position = it.key
                hasAnyElfMoved = true
            }
        }
    return hasAnyElfMoved
}

val directionGroups = listOf(
    setOf(Direction.NW, Direction.N, Direction.NE) to Direction.N,
    setOf(Direction.SW, Direction.S, Direction.SE) to Direction.S,
    setOf(Direction.NW, Direction.W, Direction.SW) to Direction.W,
    setOf(Direction.NE, Direction.E, Direction.SE) to Direction.E,
)

data class Elf(var position: Position) {
    fun proposeMove(directionGroupStartIndex: Int, elvesPositions: Set<Position>): Position {
        var newPosition: Position? = null
        var hasNeighbour = false
        for (i in directionGroups.indices) {
            val directionGroup = directionGroups[(directionGroupStartIndex + i) % 4]
            if (directionGroup.first.all { position.adjacentIn(it) !in elvesPositions }) {
                newPosition = newPosition ?: position.adjacentIn(directionGroup.second)
            } else {
                hasNeighbour = true
            }
        }
        return if (newPosition != null && hasNeighbour) newPosition else position
    }
}