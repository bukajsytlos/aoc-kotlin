import domain.Position
import domain.valueFrom

class Day4(input: String) : StatefulPuzzle<Int, Int>(input) {
    val paperRollsPositions = input.lines().flatMapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (c == '.') null else Position(x, y)
        }
    }.filterNotNull().toMutableSet()

    override fun solvePart1(): Int {
        return paperRollsPositions.count { roll ->
            roll.adjacents().count { it in paperRollsPositions } < 4
        }
    }

    override fun solvePart2(): Int {
        var sum = 0
        do {
            val removableRolls = paperRollsPositions.filter { roll ->
                roll.adjacents().count { it in paperRollsPositions } < 4
            }.toSet()
           paperRollsPositions.removeAll(removableRolls)
            sum += removableRolls.size
        } while (removableRolls.isNotEmpty())
        return sum
    }
}