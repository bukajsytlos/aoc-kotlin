import domain.Position

class Day8(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val lines = input.lines()
    private val mapSize = lines.size
    private val treeHeightMap: Array<IntArray> = Array(mapSize) { i -> lines[i].map { it.digitToInt() }.toIntArray() }

    override fun solvePart1(): Int {
        val visibleTreeCoordinates = HashSet<Position>()
        treeHeightMap.scanDirectionForVisibleTrees(visibleTreeCoordinates)
        return visibleTreeCoordinates.size
    }

    override fun solvePart2(): Int = treeHeightMap.scanTreesForScenicScore().maxBy { it.value }.value

    private fun Array<IntArray>.scanDirectionForVisibleTrees(
        alreadyVisibleTreePosition: MutableSet<Position>
    ): Set<Position> {
        for (row in indices) {
            var highestTreeSize = -1
            for (col in 0..<this[row].size) {
                val treeHeight = this[row][col]
                if (treeHeight > highestTreeSize) {
                    highestTreeSize = treeHeight
                    alreadyVisibleTreePosition.add(Position(col, row))
                }
            }
            highestTreeSize = -1
            for (col in this[row].size - 1 downTo 0) {
                val treeHeight = this[row][col]
                if (treeHeight > highestTreeSize) {
                    highestTreeSize = treeHeight
                    alreadyVisibleTreePosition.add(Position(col, row))
                }
            }
        }
        for (col in 0..<this[0].size) {
            var highestTreeSize = -1
            for (row in indices) {
                val treeHeight = this[row][col]
                if (treeHeight > highestTreeSize) {
                    highestTreeSize = treeHeight
                    alreadyVisibleTreePosition.add(Position(col, row))
                }
            }
            highestTreeSize = -1
            for (row in this.size - 1 downTo 0) {
                val treeHeight = this[row][col]
                if (treeHeight > highestTreeSize) {
                    highestTreeSize = treeHeight
                    alreadyVisibleTreePosition.add(Position(col, row))
                }
            }
        }
        return alreadyVisibleTreePosition
    }

    private fun Array<IntArray>.scanTreesForScenicScore(): Map<Position, Int> {
        val scenicScoreByTreeCoordinate = HashMap<Position, Int>(this.size * this[0].size)
        for (row in indices) {
            for (col in 0..<this[row].size) {
                val treeHeight = this[row][col]
                val scenicScore = if (col == 0 || col == this[row].size - 1 || row == 0 || row == this.size - 1) 0 else {
                    val leftScenicScore = (col - 1 downTo 0).map { this[row][it] }.indexOfFirst { it >= treeHeight }.let { if (it == -1) col else it + 1 }
                    val rightScenicScore = (col + 1..<this[row].size).map { this[row][it] }.indexOfFirst { it >= treeHeight }.let { if (it == -1) this[row].size - 1 - col else it + 1 }
                    val upScenicScore = (row - 1 downTo 0).map { this[it][col] }.indexOfFirst { it >= treeHeight }.let { if (it == -1) row else it + 1 }
                    val downScenicScore = (row + 1..<this.size).map { this[it][col] }.indexOfFirst { it >= treeHeight }.let { if (it == -1) this.size - 1 - row else it + 1 }
                    leftScenicScore * rightScenicScore * upScenicScore * downScenicScore
                }
                scenicScoreByTreeCoordinate[Position(col, row)] = scenicScore
            }
        }
        return scenicScoreByTreeCoordinate
    }
}