package domain

import kotlin.math.abs

data class Position(val x: Int, val y: Int) {
    fun adjacents() = Direction.entries.map { adjacentIn(it) }.toSet()
    fun perpendicularAdjacents() = Direction.entries.filter { !it.isDiagonal }.map { adjacentIn(it) }.toSet()
    fun diagonalAdjacents() = Direction.entries.filter { it.isDiagonal }.map { adjacentIn(it) }.toSet()
    fun adjacentIn(direction: Direction) = Position(x + direction.dx, y + direction.dy)
    fun manhattanDistanceTo(other: Position): Int = abs(x - other.x) + abs(y - other.y)
    fun <T> valueFrom(array: Array<Array<T>>): T? = runCatching { array[this.y][this.x] }.getOrNull()
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
}