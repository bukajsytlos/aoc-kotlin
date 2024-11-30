import domain.Position3D

class Day18(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val lavaPoints = input.lines().map {
        val parts = it.split(",")
        Position3D(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
    }.toSet()

    override fun solvePart1(): Int = lavaPoints.sumOf { it.perpendicularAdjacents().count { it !in lavaPoints } }

    override fun solvePart2(): Int {
        val xRange = lavaPoints.extendedRange { it.x }
        val yRange = lavaPoints.extendedRange { it.y }
        val zRange = lavaPoints.extendedRange { it.z }

        val toVisit = ArrayDeque<Position3D>().apply { add(Position3D(xRange.first, yRange.first, zRange.first)) }
        val visited = mutableSetOf<Position3D>()
        var exteriorSurfaceArea = 0
        while (toVisit.isNotEmpty()) {
            val cube = toVisit.removeFirst()
            if (cube !in visited) {
                visited += cube
                cube.perpendicularAdjacents()
                    .filter { it.x in xRange && it.y in yRange && it.z in zRange }
                    .forEach { neighbor ->
                        if (neighbor in lavaPoints) exteriorSurfaceArea++
                        else toVisit.add(neighbor)
                    }
            }
        }
        return exteriorSurfaceArea
    }
}

fun Set<Position3D>.extendedRange(filter: (Position3D) -> Int): IntRange = this.minOf(filter) - 1 .. this.maxOf(filter) + 1

