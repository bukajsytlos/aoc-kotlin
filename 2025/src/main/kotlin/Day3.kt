class Day3(input: String) : StatefulPuzzle<Long, Long>(input) {
    val batteryPacks = input.lines().map { it.map { c ->  c.digitToInt() } }


    override fun solvePart1(): Long {
        return batteryPacks.sumOf { pack ->
            findHighestJoltageOfSize(pack, 2)
        }
    }

    override fun solvePart2(): Long {
        return batteryPacks.sumOf { pack ->
            findHighestJoltageOfSize(pack, 12)
        }
    }

    private fun findHighestJoltageOfSize(pack: List<Int>, size: Int): Long {
        var startIndex = 0
        return (size downTo 1).map {
            val restOfPack = pack.subList(startIndex, pack.size - it + 1)
            val max = restOfPack.withIndex().maxBy { it.value }
            startIndex += max.index + 1
            max.value
        }.joinToString("").toLong()
    }
}