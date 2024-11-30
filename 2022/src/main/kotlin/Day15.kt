import domain.Position
import kotlin.math.abs

class Day15(input: String) : StatefulPuzzle<Int, Long>(input) {
    private val sensors = input.lines()
        .map {
            it.split(": ").let {
                Sensor(
                    it[0].substringAfter("Sensor at ").toCoordinate(),
                    it[1].substringAfter("closest beacon is at ").toCoordinate()
                )
            }
        }
    private val atY = if (sensors.count() > 14) 2_000_000 else 10 //test input hack
    private val limitY = 4_000_000

    override fun solvePart1(): Int {
        val reducedRanges = sensors.mapNotNull { it.sensorReachRangeAt(atY) }.reduce()
        val positionsWithoutBeaconAt = reducedRanges.sumOf { it.count() }
        val beaconsCountAt = sensors.map { it.closestBeacon }.distinct()
            .count { b -> reducedRanges.any { it.contains(b.x) } && b.y == atY }
        val sensorsCountAt =
            sensors.map { it.coordinate }.distinct().count { s -> reducedRanges.any { it.contains(s.x) } && s.y == atY }
        return positionsWithoutBeaconAt - beaconsCountAt - sensorsCountAt
    }

    override fun solvePart2(): Long = (0..limitY).asSequence()
        .map { i -> i to sensors.mapNotNull { it.sensorReachRangeAt(i) }.reduce() }
        .first { it.second.size > 1 }
        .let { (it.second.first().endInclusive + 1).toLong() * 4_000_000 + it.first }
}

data class Sensor(val coordinate: Position, val closestBeacon: Position) {
    fun sensorReachRangeAt(y: Int): IntRange? {
        val xHalfSize = closestBeaconDistance() - abs(coordinate.y - y)
        return if (xHalfSize > 0) {
            IntRange(coordinate.x - xHalfSize, coordinate.x + xHalfSize)
        } else null
    }

    private fun closestBeaconDistance() = coordinate.manhattanDistanceTo(closestBeacon)
}

fun List<IntRange>.reduce(): List<IntRange> {
    var reduced = sortedBy { it.first }
    val stack = ArrayDeque<IntRange>()
    stack.addLast(reduced.first())
    for (i in 1 until reduced.size) {
        val range = reduced[i]
        val range2 = stack.removeLast()
        stack.addAll(range2.merge(range))
    }
    return stack.toList()
}

fun IntRange.merge(other: IntRange?): List<IntRange> = if (other == null) {
    listOf(this)
} else if (start in other || endInclusive in other || other.start in this || other.endInclusive in this) {
    listOf(IntRange(kotlin.math.min(start, other.first), kotlin.math.max(endInclusive, other.last)))
} else listOf(this, other)

fun String.toCoordinate() = split(", ").let {
    Position(
        it[0].substringAfter("x=").toInt(),
        it[1].substringAfter("y=").toInt()
    )
}