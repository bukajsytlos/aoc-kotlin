import domain.Position

class Day10(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val lines = input.lines()
    private val mapSize = lines.size
    private val heightMap: Array<Array<Int>> = Array(mapSize) { i -> lines[i].map { it.digitToInt() }.toTypedArray() }
    private val trailHeads: Set<Position> = heightMap
        .flatMapIndexed { y, line -> line.mapIndexed { x, height -> if (height == 0) Position(x, y) else null }
        .filterNotNull() }.toSet()
    
    override fun solvePart1(): Int = trailHeads.sumOf { it.score(false) }

    override fun solvePart2(): Int = trailHeads.sumOf { it.score(true) }

    private fun Position.score(distinct: Boolean): Int {
        val visitedPeaks = mutableSetOf(this)
        val queue = ArrayDeque<Position>()
        queue.addLast(this)
        var score = 0
        while (queue.isNotEmpty()) {
            val pos = queue.removeFirst()
            val currentHeightValue = pos.valueFrom(heightMap)
            if (currentHeightValue == 9) {
                if (distinct || pos !in visitedPeaks) {
                    visitedPeaks.add(pos)
                    score++
                }
                continue
            }
            for (adjacent in pos.perpendicularAdjacents()) {
                val adjacentValue = adjacent.valueFrom(heightMap)
                if (adjacentValue == null || adjacentValue - 1 != currentHeightValue) continue
                queue.addLast(adjacent)
            }
        }
        return score
    }
}