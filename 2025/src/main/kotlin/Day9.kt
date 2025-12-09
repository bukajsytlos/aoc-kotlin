import domain.Position
import kotlin.math.max
import kotlin.math.min

class Day9(input: String) : StatefulPuzzle<Long, Long>(input) {
    val redTiles = input.lines().map { Position.from(it) }
    val rectangles = (0..<redTiles.lastIndex - 1).flatMap { tile1Index ->
        (tile1Index + 1..<redTiles.lastIndex).map { tile2Index ->
            Rectangle.from(redTiles[tile1Index], redTiles[tile2Index])
        }
    }
    val surfaceLines = (redTiles + redTiles.first()).zipWithNext().map { Line.from(it.first, it.second) }

    override fun solvePart1(): Long = rectangles.map { it.surface() }.max()

    override fun solvePart2(): Long {
        val filter = rectangles.filter { rectangle -> surfaceLines.none { rectangle.overlapsWith(it) } }
        return filter.map { it.surface() }.max()
    }

    data class Rectangle(val bottomLeft: Position, val topRight: Position) {

        fun surface(): Long = (topRight.x - bottomLeft.x + 1).toLong() * (topRight.y - bottomLeft.y + 1)
        fun overlapsWith(line: Line): Boolean = bottomLeft.x < line.end.x && line.start.x < topRight.x
                                             && bottomLeft.y < line.end.y && line.start.y < topRight.y

        companion object {
            fun from(corner: Position, corner2: Position): Rectangle {
                return Rectangle(
                    Position(min(corner.x, corner2.x), min(corner.y, corner2.y)),
                    Position(max(corner.x, corner2.x), max(corner.y, corner2.y))
                )
            }
        }
    }

    data class Line(val start: Position, val end: Position) {
        companion object {
            fun from(start: Position, end: Position): Line {
                return Line(
                    Position(min(start.x, end.x), min(start.y, end.y)),
                    Position(max(start.x, end.x), max(start.y, end.y))
                )
            }
        }
    }
}
