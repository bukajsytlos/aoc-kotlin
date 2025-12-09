package domain

import domain.Direction.entries
import kotlin.math.abs

data class Position(val x: Int, val y: Int) {
    fun adjacents() = Direction.entries.map { adjacentIn(it) }.toSet()
    fun perpendicularAdjacents() = Direction.perpendicular().map { adjacentIn(it) }.toSet()
    fun diagonalAdjacents() = Direction.diagonal().map { adjacentIn(it) }.toSet()
    fun adjacentIn(direction: Direction, length: Int = 1) = Position(x + direction.dx * length, y + direction.dy * length)
    fun manhattanDistanceTo(other: Position): Int = abs(x - other.x) + abs(y - other.y)
    fun <T> valueFrom(array: Array<Array<T>>): T? = runCatching { array[this.y][this.x] }.getOrNull()
    fun isOutOfBound(maxX: Int, maxY: Int = maxX): Boolean = x < 0 || x > maxX - 1 || y < 0 || y > maxY - 1
    fun vector(to: Position) = Vector(to.x - x, to.y - y)
    fun plus(vector: Vector) = Position(x + vector.dx, y + vector.dy)
    companion object {
        fun from(commaSeparated: String): Position {
            val (x, y) = commaSeparated.split(",")
            return Position(x.toInt(), y.toInt())
        }
    }
}

fun <T> Array<Array<T>>.findPositionsOf(target: T): Set<Position> =
    flatMapIndexed { y, line -> line.mapIndexed { x, o -> if (o == target) Position(x, y) else null }.filterNotNull() }
        .toSet()

fun <T> Array<Array<T>>.valueFrom(position: Position): T? = runCatching { this[position.y][position.x] }.getOrNull()
fun <T> Array<Array<T>>.setValueTo(position: Position, value: T) {
    this[position.y][position.x] = value
}

data class Vector(val dx: Int, val dy: Int) {
    fun invert() = Vector(-dx, -dy)
}

data class Point(val x: Long, val y: Long) {
    fun manhattanDistanceTo(other: Point): Long = abs(x - other.x) + abs(y - other.y)
    fun adjacentIn(direction: Direction, length: Int = 1) = Point(x + direction.dx * length, y + direction.dy * length)
}

enum class Direction(val dx: Int, val dy: Int, val isDiagonal: Boolean) {
    NW(-1, -1, true),
    N(0, -1, false),
    NE(1, -1, true),
    E(1, 0, false),
    SE(1, 1, true),
    S(0, 1, false),
    SW(-1, 1, true),
    W(-1, 0, false),
    ;

    fun opposite(): Direction = when (this) {
        NW -> SE
        N -> S
        NE -> SW
        E -> W
        SE -> NW
        S -> N
        SW -> NE
        W -> E
    }
    fun left90(): Direction = when (this) {
        Direction.NW -> Direction.SW
        Direction.N -> Direction.W
        Direction.NE -> Direction.NW
        Direction.E -> Direction.N
        Direction.SE -> Direction.NE
        Direction.S -> Direction.E
        Direction.SW -> Direction.SE
        Direction.W -> Direction.S
    }
    fun right90(): Direction = when (this) {
        Direction.NW -> Direction.NE
        Direction.N -> Direction.E
        Direction.NE -> Direction.SE
        Direction.E -> Direction.S
        Direction.SE -> Direction.SW
        Direction.S -> Direction.W
        Direction.SW -> Direction.NW
        Direction.W -> Direction.N
    }
    companion object {
        fun perpendicular() = entries.filter { !it.isDiagonal }
        fun diagonal() = entries.filter { it.isDiagonal }
        fun of(char: Char): Direction = when (char) {
            '^' -> N
            '>' -> E
            '<' -> W
            'v' -> S
            else -> error("unknown direction: $char")
        }
    }
}

data class Position3D(val x: Int, val y: Int, val z: Int) {
    fun perpendicularAdjacents(): Set<Position3D> = setOf(
        copy(x = x + 1),
        copy(x = x - 1),
        copy(y = y + 1),
        copy(y = y - 1),
        copy(z = z + 1),
        copy(z = z - 1),
    )
    companion object {
        fun from(commaSeparated: String): Position3D {
            val (x, y, z) = commaSeparated.split(",")
            return Position3D(x.toInt(), y.toInt(), z.toInt())
        }
    }
}