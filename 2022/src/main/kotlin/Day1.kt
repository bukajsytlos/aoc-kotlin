object Day1 : StatelessPuzzle<Int, Int>() {
    override fun solvePart1(input: String): Int = input
        .split(System.lineSeparator() + System.lineSeparator())
        .map { it.lines().sumOf { it.toInt() } }
        .max()

    override fun solvePart2(input: String): Int = input
        .split(System.lineSeparator() + System.lineSeparator())
        .map { it.lines().sumOf { it.toInt() } }
        .sortedDescending().take(3).sum()
}