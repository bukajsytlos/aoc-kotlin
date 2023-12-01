val digitsMap = mapOf(
    "1" to 1,
    "2" to 2,
    "3" to 3,
    "4" to 4,
    "5" to 5,
    "6" to 6,
    "7" to 7,
    "8" to 8,
    "9" to 9,
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)

object Day1 : StatelessPuzzle<Int, Int>() {

    override fun solvePart1(input: String): Int = input.lines()
        .map { it.first { it.isDigit() } + "" + it.last { it.isDigit() } }
        .sumOf { it.toInt() }

    override fun solvePart2(input: String): Int = input.lines().sumOf {
        digitsMap[it.findAnyOf(digitsMap.keys)!!.second]!! * 10 + digitsMap[it.findLastAnyOf(digitsMap.keys)!!.second]!!
    }
}