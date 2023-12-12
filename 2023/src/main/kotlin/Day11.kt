import domain.Point

class Day11(input: String) : StatefulPuzzle<Long, Long>(input) {
    private val lines = input.lines()
    private val mapSize = lines.size
    private val galaxyMap: Array<Array<Boolean>> = Array(mapSize) { i -> lines[i].map { it == '#' }.toTypedArray() }
    private val emptyRows: Set<Int> = galaxyMap.mapIndexedNotNull { i, row -> if (row.all { !it }) i else null }.toSet()
    private val emptyColumns: Set<Int> = galaxyMap[0].indices.mapNotNull { col -> if (galaxyMap.indices.all { !galaxyMap[it][col] }) col else null }.toSet()
    
    override fun solvePart1(): Long = galaxyPairs(galaxyPoints(2)).sumOf { it.first.manhattanDistanceTo(it.second) }

    override fun solvePart2(): Long = galaxyPairs(galaxyPoints(1_000_000)).sumOf { it.first.manhattanDistanceTo(it.second) }
    
    private fun galaxyPoints(expansionRatio: Long): List<Point> {
        return galaxyMap.flatMapIndexed { y, row -> row.mapIndexedNotNull { x, isGalaxy -> 
            if (isGalaxy) Point(emptyColumns.count { it < x } * (expansionRatio - 1) + x, emptyRows.count { it < y } * (expansionRatio - 1) + y) else null 
        }}
    }
    
    private fun galaxyPairs(points: List<Point>): List<Pair<Point, Point>> =
        points.flatMapIndexed { i, g1 -> points.subList(i + 1, points.size).map { g2 -> g1 to g2 } }    
}