import domain.Direction
import domain.Position

class Day4(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val lines = input.lines()
    private val mapSize = lines.size
    private val wordSearch: Array<Array<Char>> = Array(mapSize) { i -> lines[i].map { it }.toTypedArray() }

    override fun solvePart1(): Int {
        var xmasCount = 0
        wordSearch.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char == 'X') {
                    xmasCount += Direction.entries.count { findXmas(wordSearch, Position(x, y), it) }
                }
            }
        }
        return xmasCount
    }

    override fun solvePart2(): Int {
        var xmasCount = 0
        wordSearch.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char == 'A') {
                    xmasCount += if (`findX-mas`(wordSearch, Position(x, y))) 1 else 0
                }
            }
        }
        return xmasCount
    }

    private fun findXmas(wordSearch: Array<Array<Char>>, xPosition: Position, direction: Direction): Boolean {
        return xPosition.adjacentIn(direction, 1).valueFrom(wordSearch) == 'M'
                && xPosition.adjacentIn(direction, 2).valueFrom(wordSearch) == 'A'
                && xPosition.adjacentIn(direction, 3).valueFrom(wordSearch) == 'S'
    }

    private fun `findX-mas`(wordSearch: Array<Array<Char>>, aPosition: Position): Boolean {
        val nwValue = aPosition.adjacentIn(Direction.NW, 1).valueFrom(wordSearch)
        val neValue = aPosition.adjacentIn(Direction.NE, 1).valueFrom(wordSearch)
        val seValue = aPosition.adjacentIn(Direction.SE, 1).valueFrom(wordSearch)
        val swValue = aPosition.adjacentIn(Direction.SW, 1).valueFrom(wordSearch)
        return nwValue == 'M' && neValue == 'M' && seValue == 'S' && swValue == 'S'
                    || nwValue == 'S' && neValue == 'M' && seValue == 'M' && swValue == 'S'
                    || nwValue == 'S' && neValue == 'S' && seValue == 'M' && swValue == 'M'
                    || nwValue == 'M' && neValue == 'S' && seValue == 'S' && swValue == 'M'         
    }
}