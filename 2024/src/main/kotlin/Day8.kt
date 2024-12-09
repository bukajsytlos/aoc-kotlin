import domain.Position

class Day8(input: String) : StatefulPuzzle<Int, Int>(input) {
    val antenasPositionsByFrequencies: Map<Char, Set<Position>> = input.lines().flatMapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (c == '.') null else Pair(c, Position(x, y))
        }
    }.filterNotNull().groupBy { it.first }.mapValues { it.value.map { it.second }.toSet() }
    val mapSize = input.lines().size
    
    override fun solvePart1(): Int {
        val antiNodes = mutableSetOf<Position>()
        antenasPositionsByFrequencies.forEach { (_, positions) ->
            antiNodes.addAll(findAntiNodes(positions, mapSize))
        }
        return antiNodes.count()
    }

    override fun solvePart2(): Int {
        val antiNodes = mutableSetOf<Position>()
        antenasPositionsByFrequencies.forEach { (_, positions) ->
            antiNodes.addAll(findAntiNodes2(positions, mapSize))
        }
        return antiNodes.count()
    }

    private fun findAntiNodes(positions: Set<Position>, mapSize:Int): Set<Position> {
        val result = mutableSetOf<Position>()
        for (p1 in positions) {
            for (p2 in positions) {
                if (p1 != p2) {
                    val vector = p1.vector(p2)
                    val antiNode = p2.plus(vector)
                    if (antiNode.isOutOfBound(mapSize).not()) result.add(antiNode)
                }
            }
        }
        return result
    }

    private fun findAntiNodes2(positions: Set<Position>, mapSize:Int): Set<Position> {
        val result = mutableSetOf<Position>()
        for (p1 in positions) {
            for (p2 in positions) {
                if (p1 != p2) {
                    val vector = p1.vector(p2)
                    var antiNode = p2.plus(vector)
                    while (antiNode.isOutOfBound(mapSize).not()) {
                        result.add(antiNode)
                        antiNode = antiNode.plus(vector)
                    }
                } else result.add(p1)
            }
        }
        return result
    }
}
