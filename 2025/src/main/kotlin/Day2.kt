class Day2(input: String) : StatefulPuzzle<Long, Long>(input) {
    val ids = input.split(",")
        .flatMap { it.asLongRange() }


    override fun solvePart1(): Long {
        val repeatDigitsPattern = """^(\d+)\1$""".toRegex()
        return ids.filter { repeatDigitsPattern.matches(it.toString()) }
            .sum()
    }

    override fun solvePart2(): Long {
        val repeatDigitsPattern = """^(\d+)\1+$""".toRegex()
        return ids.filter { repeatDigitsPattern.matches(it.toString()) }
            .sum()
    }
}