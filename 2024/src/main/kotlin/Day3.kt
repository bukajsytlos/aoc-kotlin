object Day3 : StatelessPuzzle<Int, Int>() {
    val mulPattern = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
    val doPattern = """do\(\)""".toRegex()
    val dontPattern = """don't\(\)""".toRegex()

    override fun solvePart1(input: String): Int {
        return mulPattern.findAll(input).sumOf { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
    }

    override fun solvePart2(input: String): Int {
        val mulResultByPosition = mulPattern.findAll(input)
            .associate { it.range.first to it.groupValues[1].toInt() * it.groupValues[2].toInt() }.toMap()
        val doPositions = doPattern.findAll(input).map { it.range.first }.toSet()
        val dontPositions = dontPattern.findAll(input).map { it.range.first }.toSet()
        return input.foldIndexed(0 to true) { index, acc, _ ->
            val accumulatedValue = if (acc.second && mulResultByPosition.contains(index)) {
                acc.first + mulResultByPosition[index]!!
            } else {
                acc.first
            }
            val doIt = (doPositions.contains(index) || acc.second) && dontPositions.contains(index).not()
            accumulatedValue to doIt
        }.first
    }
}