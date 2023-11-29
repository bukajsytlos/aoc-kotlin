abstract class StatefulPuzzle<Part1, Part2>(
    val input: String
) {
    abstract fun solvePart1(): Part1
    abstract fun solvePart2(): Part2
}