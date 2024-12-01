import kotlin.math.abs

class Day1(input: String) : StatefulPuzzle<Int, Int>(input) {
    val locationIds = input.lines().map { line -> line.split("   ").let { it[0].toInt() to it[1].toInt()} }.unzip()
    val list1 = locationIds.first.sorted()
    val list2 = locationIds.second.sorted()

    override fun solvePart1(): Int {
        return list1.zip(list2).sumOf { abs(it.first - it.second) }
    }

    override fun solvePart2(): Int {
        val weightMap = list2.groupingBy { it }.eachCount()
        return list1.sumOf { it * (weightMap[it] ?: 0) }
    }
}