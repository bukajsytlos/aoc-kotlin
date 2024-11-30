import domain.Position
import kotlin.math.max
import kotlin.math.min

class Day14(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val rockLines = input.lines().flatMap {
        it.toRockLines()
    }

    private val coordinates = rockLines.flatMap { listOf(it.from, it.to) }
    private val minX = coordinates.minOf { it.x }
    private val maxX = coordinates.maxOf { it.x }
    private val maxY = coordinates.maxOf { it.y }

    private val cave = Cave(SandSource(Position(500, 0)), rockLines.flatMap { it.getTiles() }, minX, maxX, maxY)


    override fun solvePart1(): Int = cave.pourSand()

    override fun solvePart2(): Int = cave.pourSand2()
}

data class Cave(
    val sandSource: SandSource,
    val rockTiles: List<Position>,
    val minX: Int,
    val maxX: Int,
    val maxY: Int
) {
    private val tiles = HashSet(rockTiles)

    fun pourSand(): Int {
        do {
            val sandUnit = sandSource.pour()
            when (val sandUnitState = evaluateSandUnitState(sandUnit)) {
                is SandUnitState.Rest -> tiles.add(sandUnitState.coordinate)
                is SandUnitState.Overflow -> break
            }
        } while (true)
        return sandSource.numberOfPouredSandUnits - 1
    }

    private fun evaluateSandUnitState(sandUnit: SandUnit): SandUnitState {
        val x = sandUnit.coordinate.x
        val y = sandUnit.coordinate.y
        return if (x !in (minX..maxX) || y > maxY) {
            SandUnitState.Overflow
        } else if (!tiles.contains(Position(x, y + 1))) {
            evaluateSandUnitState(SandUnit(Position(x, y + 1)))
        } else if (!tiles.contains(Position(x - 1, y + 1))) {
            evaluateSandUnitState(SandUnit(Position(x - 1, y + 1)))
        } else if (!tiles.contains(Position(x + 1, y + 1))) {
            evaluateSandUnitState(SandUnit(Position(x + 1, y + 1)))
        } else {
            SandUnitState.Rest(sandUnit.coordinate)
        }
    }

    fun pourSand2(): Int {
        do {
            val sandUnit = sandSource.pour()
            when (val sandUnitState = evaluateSandUnitState2(sandUnit)) {
                is SandUnitState.Rest -> {
                    if (sandUnitState.coordinate == sandSource.coordinate) {
                        break
                    }
                    tiles.add(sandUnitState.coordinate)
                }
                is SandUnitState.Overflow -> continue
            }
        } while (true)
        return sandSource.numberOfPouredSandUnits - 1
    }

    private fun evaluateSandUnitState2(sandUnit: SandUnit): SandUnitState {
        val x = sandUnit.coordinate.x
        val y = sandUnit.coordinate.y
        return if (!tiles.contains(Position(x, y + 1)) && y + 1 < maxY + 2) {
            evaluateSandUnitState2(SandUnit(Position(x, y + 1)))
        } else if (!tiles.contains(Position(x - 1, y + 1)) && y + 1 < maxY + 2) {
            evaluateSandUnitState2(SandUnit(Position(x - 1, y + 1)))
        } else if (!tiles.contains(Position(x + 1, y + 1)) && y + 1 < maxY + 2) {
            evaluateSandUnitState2(SandUnit(Position(x + 1, y + 1)))
        } else {
            SandUnitState.Rest(sandUnit.coordinate)
        }
    }
}

class SandSource(val coordinate: Position) {
    var numberOfPouredSandUnits: Int = 0
    fun pour(): SandUnit {
        numberOfPouredSandUnits++
        return SandUnit(coordinate)
    }
}

class SandUnit(var coordinate: Position)

sealed class SandUnitState {
    class Rest(val coordinate: Position): SandUnitState()
    object Overflow: SandUnitState()
}

data class RockLine(val from: Position, val to: Position) {
    fun getTiles(): List<Position> = (min(from.x, to.x)..max(from.x, to.x)).flatMap { x ->
        (min(from.y, to.y)..max(from.y, to.y)).map { y -> Position(x, y) } }
}

fun String.toRockLines(): List<RockLine> = split(" -> ")
    .map { Position(it.substringBefore(",").toInt(), it.substringAfter(",").toInt()) }
    .windowed(2)
    .map { RockLine(it.first(), it.last()) }