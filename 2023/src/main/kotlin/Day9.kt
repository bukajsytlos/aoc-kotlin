class Day9(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val oasisHistories = input.lines()
        .map { it.split(" ").map { it.toInt() } }

    override fun solvePart1(): Int = oasisHistories.sumOf { it.nextValue() }

    override fun solvePart2(): Int = oasisHistories.map { it.reversed() }.sumOf { it.nextValue() }

    private fun List<Int>.nextValue(): Int {
        val lastDiffs = mutableListOf<Int>()
        lastDiffs.add(this.last())
        var diffSequence: List<Int> = this
        while (diffSequence.any { it != 0 }) {
            diffSequence = diffSequence.zipWithNext { a, b -> b - a }
            lastDiffs.add(diffSequence.last())
        }

        return lastDiffs.sum()
    }
}