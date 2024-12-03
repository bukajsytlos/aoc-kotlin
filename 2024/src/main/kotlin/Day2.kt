class Day2(input: String) : StatefulPuzzle<Int, Int>(input) {
    val reports = input.lines().map { line -> line.split(" ").map { it.toInt() } }

    override fun solvePart1(): Int {
        return reports.count { it.isSafe() || it.reversed().isSafe() }
    }

    override fun solvePart2(): Int = reports.count { it.omitBadLevel().toList().any { it.isSafe() } || it.reversed().omitBadLevel().toList().any { it.isSafe() } }
}

private fun List<Int>.isSafe(): Boolean {
    return zipWithNext().any { it.isUnsafe() }.not()
}

private fun Pair<Int, Int>.isUnsafe(): Boolean = second - first !in (1..3)
    
private fun List<Int>.omitBadLevel(): Pair<List<Int>, List<Int>> {
    val badLevelPosition = zipWithNext().indexOfFirst { it.isUnsafe() }
    return omitAtIndex(badLevelPosition) to omitAtIndex(badLevelPosition + 1)
}

private fun List<Int>.omitAtIndex(index: Int): List<Int> = filterIndexed { i, _ -> i != index }