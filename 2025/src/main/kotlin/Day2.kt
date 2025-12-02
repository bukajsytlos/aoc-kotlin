class Day2(input: String) : StatefulPuzzle<Long, Long>(input) {
    val ids = input.split(",")
        .flatMap { LongRange(it.substringBefore("-").toLong(), it.substringAfter("-").toLong()).asIterable() }


    override fun solvePart1(): Long {
        val repeatDigitsPattern = """^(\d+)\1$""".toPattern()
        return ids.filter { repeatDigitsPattern.matcher(it.toString()).matches() }
            .sum()
    }

    override fun solvePart2(): Long {
        val repeatDigitsPattern = """^(\d+)\1+$""".toPattern()
        return ids.filter { repeatDigitsPattern.matcher(it.toString()).matches() }
            .sum()
    }
}