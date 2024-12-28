import domain.Direction
import domain.Position

class Day12(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val plantsMap: Array<Array<Char>> = input.asTyped2DArray { it }
    private val mapSize = plantsMap.size

    val scannedPositions = mutableSetOf<Position>()
    val regionsList = mutableListOf<PlantRegion>()
    init {
        (0..<mapSize).forEach { y ->
            (0..<mapSize).forEach { x ->
                val pos = Position(x, y)
                if (pos !in scannedPositions) {
                    val type = pos.valueFrom(plantsMap)!!
                    val plantRegion = PlantRegion(type, pos.flood(type) + pos)
                    regionsList.add(plantRegion)
                    scannedPositions.addAll(plantRegion.positions)
                }
            }
        }
    }

    override fun solvePart1(): Int = regionsList.sumOf { it.area() * it.perimeter() }

    override fun solvePart2(): Int = regionsList.sumOf { it.area() * it.numberOfCorners() }
    
    data class PlantRegion(val type: Char, val positions: Set<Position>) {
        fun area() = positions.size
        fun perimeter() = positions.sumOf { it.perpendicularAdjacents().filter { p -> p !in positions }.size }
        fun numberOfCorners(): Int {
            val possibleCornerPositions = positions.filter { it.adjacents().any { p -> p !in positions } }
            return possibleCornerPositions.sumOf {
                val nPos = it.adjacentIn(Direction.N)
                val nePos = it.adjacentIn(Direction.NE)
                val ePos = it.adjacentIn(Direction.E)
                val esPos = it.adjacentIn(Direction.SE)
                val sPos = it.adjacentIn(Direction.S)
                val swPos = it.adjacentIn(Direction.SW)
                val wPos = it.adjacentIn(Direction.W)
                val wnPos = it.adjacentIn(Direction.NW)
                var corners = 0
                if (nPos !in positions && ePos !in positions
                    || nPos in possibleCornerPositions && ePos in possibleCornerPositions && nePos !in positions) corners++
                if (ePos !in positions && sPos !in positions
                    || ePos in possibleCornerPositions && sPos in possibleCornerPositions && esPos !in positions) corners++
                if (sPos !in positions && wPos !in positions
                    || sPos in possibleCornerPositions && wPos in possibleCornerPositions && swPos !in positions) corners++
                if (wPos !in positions && nPos !in positions
                    || wPos in possibleCornerPositions && nPos in possibleCornerPositions && wnPos !in positions) corners++
                corners
            }
        }
    }

    private fun Position.flood(type: Char): Set<Position> {
        val visited = mutableSetOf<Position>()
        val toVisit = ArrayDeque<Position>()
        toVisit.add(this)
        while (toVisit.isNotEmpty()) {
            val pos = toVisit.removeFirst()
            if (pos !in visited) {
                visited.add(pos)
                pos.perpendicularAdjacents().filter { p -> p.valueFrom(plantsMap) == type }.forEach { p -> toVisit.add(p) }
            }
        }
        return visited
    }
}

