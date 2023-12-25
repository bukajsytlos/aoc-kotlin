import domain.Position
import domain.Position3D

class Day22(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val highestZForBrickId = mutableMapOf<Position, Pair<Int, Int>>()
    private val brickSupportedBy = mutableMapOf<Int, Set<Int>>()
    private val brickSupports = mutableMapOf<Int, Set<Int>>()
    private val brickLinesSnapshot = input.lines().mapIndexed { index, brickDef ->
        val (startDef, endDef) = brickDef.split("~")
        BrickLine(index, Position3D.from(startDef), Position3D.from(endDef))
    }.sortedBy { it.startPosition.z }.onEach { brickLine ->
        val brickId = brickLine.id
        val xyPositions = brickLine.xYpositions()
        val highestZOwnedBysForLines = xyPositions.map { highestZForBrickId[it] ?: (0 to -1) }
        val highestZUnder = highestZOwnedBysForLines.maxOf { it.first }
        val supportedByIds = highestZOwnedBysForLines.filter { it.first == highestZUnder }.map { it.second }.toSet()
        brickSupportedBy[brickId] = supportedByIds
        supportedByIds.forEach {
            brickSupports.compute(it) { _, v -> if (v == null) setOf(brickId) else v + brickId }
        }
        val newHighestZForBrickId = (highestZUnder + brickLine.zHeigh()) to brickId
        xyPositions.forEach {
            highestZForBrickId[it] = newHighestZForBrickId
        }
    }
    private val indisposableBrickIds = brickSupportedBy.values.filter { it.size == 1 && it.single() != -1 }.flatten().toSet()
    
    override fun solvePart1(): Int {
        return brickLinesSnapshot.count() - (indisposableBrickIds.size)
    }

    override fun solvePart2(): Int {
        return indisposableBrickIds.sumOf {
            fallBrickCount(it)
        }
    }
    
    private fun fallBrickCount(id: Int): Int {
        val fallen = mutableSetOf<Int>()
        val toFallBricks = ArrayDeque<Int>()
        toFallBricks.add(id)
        while (toFallBricks.isNotEmpty()) {
            val toFallBrick = toFallBricks.removeFirst()
            if (fallen.isNotEmpty() && !fallen.containsAll(brickSupportedBy.getValue(toFallBrick))) {
                continue
            }
            fallen.add(toFallBrick)
            brickSupports[toFallBrick]?.let { toFallBricks.addAll(it) }
        }
        
        return fallen.size - 1
    }
    
    data class BrickLine(val id: Int, val startPosition: Position3D, val endPosition: Position3D) {
        fun xYpositions() = (startPosition.x .. endPosition.x).flatMap { x -> (startPosition.y .. endPosition.y).map { Position(x, it) } }
        fun zHeigh() = endPosition.z - startPosition.z + 1
    }
}

