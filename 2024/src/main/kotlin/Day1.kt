import kotlin.math.abs

class Day1(input: String) : StatefulPuzzle<Int, Int>(input) {
    val locationIds = input.lines().map { line -> line.split("   ") }.map { Pair(it[0].toInt(), it[1].toInt()) }.unzip()
    val list1 = locationIds.first.sorted()
    val list2 = locationIds.second.sorted()

    override fun solvePart1(): Int {
        return list1.zip(list2).sumOf { abs(it.first - it.second) }
    }

    override fun solvePart2(): Int {
        val weightMap = list2.groupBy { it }.mapValues { it.value.size }
        return list1.sumOf { it * weightMap.getOrElse(it) { 0 } }
    }
}