class Day6(input: String) : StatefulPuzzle<Long, Long>(input) {
    val operatorsPositions = input.lines().last().withIndex().filter { it.value != ' ' }
    val numbersLines = input.lines().dropLast(1)
    val operatorsRanges = operatorsPositions.plus(IndexedValue(input.lines()[0].length + 1, 'x')).zipWithNext()
    val problems = operatorsRanges.map { op -> Problem(numbersLines.map { numbers -> numbers.substring(op.first.index, op.second.index - 1).trim().toLong() }, op.first.value) }
    val problems2 = operatorsRanges.map { op -> Problem(numbersLines.map { numbers -> numbers.substring(op.first.index, op.second.index - 1) }.transpose().map { it.trim().toLong() }, op.first.value) }

    override fun solvePart1(): Long {
        return problems.sumOf { it.solve() }
    }

    override fun solvePart2(): Long {
        return problems2.sumOf { it.solve() }
    }

    data class Problem(val numbers: List<Long>, val operator: Char) {
        fun solve(): Long =  if (operator == '*') numbers.reduce(Long::times) else { numbers.sum() }
    }

    private fun List<String>.transpose(): List<String> {
        return (0..<this[0].length).map { x -> (0..<size).map { y -> this[y][x] }.joinToString("") }
    }
}


