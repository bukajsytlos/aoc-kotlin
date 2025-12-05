import domain.merge
import domain.overlap

class Day5(input: String) : StatefulPuzzle<Long, Long>(input) {
    val inputParts = input.split("\n\n")
    val freshIdRanges = inputParts[0].lines().map { it.asLongRange() }
    val ingredientIds = inputParts[1].lines().map { it.toLong() }

    override fun solvePart1(): Long {
        return ingredientIds.count { freshIdRanges.any { f -> it in f} }.toLong()
    }

    override fun solvePart2(): Long {
        val sortedByStart = freshIdRanges.sortedBy { it.first() }
        val compactedRanges = mutableListOf<LongRange>()
        var processedRange: LongRange = LongRange.EMPTY
        sortedByStart.forEach { range ->
            if (range.overlap(processedRange)) {
                processedRange = processedRange.merge(range)
            } else {
                compactedRanges.add(processedRange)
                processedRange = range
            }
        }
        compactedRanges.add(processedRange)
        return compactedRanges.sumOf { it.last - it.first + 1 }
    }
}