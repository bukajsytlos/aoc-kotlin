class Day12(input: String) : StatefulPuzzle<Int, Int>(input) {

    override fun solvePart1(): Int {
        return input.lines()
            .filter { 'x' in it }
            .map { it.split("""\D+""".toRegex()).map(String::toInt) }
            .count { it[0] * it[1] > it.drop(2).sum() * 8 }
    }

    override fun solvePart2(): Int {
        return 0
    }
}
