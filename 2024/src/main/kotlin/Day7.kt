class Day7(input: String) : StatefulPuzzle<Long, Long>(input) {
    val calibrations: List<Calibration> = input.lines().map {
        Calibration(it.substringBefore(":").toLong(), it.substringAfter(": ").split(" ").map { it.toLong() })
    }
    val operators1: List<(Long, Long) -> Long> = listOf(
        { a, b -> a + b },
        { a, b -> a * b }
    )
    val operators2: List<(Long, Long) -> Long> = operators1 + { a, b -> "$a$b".toLong() }
        
    override fun solvePart1(): Long = calibrations
        .filter { it.isSolvable(operators1) }
        .sumOf { it.expectedResult  }

    override fun solvePart2(): Long = calibrations
        .filter { it.isSolvable(operators2) }
        .sumOf { it.expectedResult  }
    
    data class Calibration(val expectedResult: Long, val values: List<Long>) {
        fun isSolvable(operators: List<(Long, Long) -> Long>) = isSolvable(values[0], values.subList(1, values.size), operators)
        
        private fun isSolvable(result: Long, values: List<Long>, operators: List<(Long, Long) -> Long>): Boolean {
            return when {
                values.isEmpty() -> result == expectedResult
                result > expectedResult -> false
                else -> {
                    operators.any { isSolvable(it.invoke(result, values[0]), values.subList(1, values.size), operators) }
                }
            }
        }
    }
}
