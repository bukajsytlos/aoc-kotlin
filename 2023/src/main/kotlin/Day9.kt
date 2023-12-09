class Day9(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val oasisHistories = input.lines()
        .map { it.split(" ").map { it.toInt() } }

    override fun solvePart1(): Int = oasisHistories.sumOf { it.nextValue() }

    override fun solvePart2(): Int = oasisHistories.map { it.reversed() }.sumOf { it.nextValue() }

    private fun List<Int>.nextValue(): Int = if (this.all { it == 0 }) 0 else
        this.last() + this.zipWithNext { a, b -> b - a }.nextValue()
}