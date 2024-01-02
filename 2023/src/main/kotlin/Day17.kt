import domain.Direction
import domain.Position
import java.util.PriorityQueue

class Day17(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val heatLossMap = input.asTyped2DArray { it.digitToInt() }
    private val destination = Position(heatLossMap[0].size - 1, heatLossMap.size - 1)

    override fun solvePart1(): Int = findMinimumHeatLossAt(heatLossMap, destination, 3, 0)

    override fun solvePart2(): Int = findMinimumHeatLossAt(heatLossMap, destination, 10, 4)

    private fun findMinimumHeatLossAt(
        heatLossGrid: Array<Array<Int>>,
        destinationPosition: Position,
        maxVelocity: Int,
        minVelocity: Int
    ): Int {
        val knownStates: MutableSet<CrucibleMomentum> = mutableSetOf()
        val toEvaluate: PriorityQueue<Pair<CrucibleMomentum, Int>> = PriorityQueue { o1, o2 -> o1.second - o2.second }
        val memo = mutableMapOf<CrucibleMomentum, Int>()
        val initMomentumEast = CrucibleMomentum(Position(0, 0), Direction.E, 0)
        toEvaluate.add(initMomentumEast to 0)
        memo[initMomentumEast] = 0

        while (toEvaluate.isNotEmpty()) {
            val (momentum, accumulatedHeatLoss) = toEvaluate.poll()
            knownStates.add(momentum)
            for (direction in Direction.perpendicular()) {
                if (momentum.direction.opposite() == direction 
                    || momentum.direction == direction && momentum.velocity >= maxVelocity
                    || momentum.direction != direction && momentum.velocity < minVelocity && momentum.velocity > 0
                    ) continue
                val newMomentum = momentum.moveIn(direction)
                if (newMomentum in knownStates) continue
                val positionCandidate = newMomentum.position
                val newHeatLoss = positionCandidate.valueFrom(heatLossGrid) ?: continue
                val newAccumulatedHeatLoss = accumulatedHeatLoss + newHeatLoss
                if ((memo[newMomentum]?: Int.MAX_VALUE) <= newAccumulatedHeatLoss) continue
                toEvaluate.offer(newMomentum to newAccumulatedHeatLoss)
                memo[newMomentum] = newAccumulatedHeatLoss
            }
        }
        return memo.filter { it.key.position == destinationPosition }.minOf { it.value }
    }

    data class CrucibleMomentum(val position: Position, val direction: Direction, val velocity: Int) {
        fun moveIn(direction: Direction) = CrucibleMomentum(
            position.adjacentIn(direction),
            direction,
            if (direction == this.direction) this.velocity + 1 else 1
        )
    }
}