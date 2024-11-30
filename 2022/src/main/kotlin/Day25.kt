typealias Snafu = String

object Day25 : StatelessPuzzle<String, Unit>() {
    override fun solvePart1(input: String): String = input.lines().sumOf { line -> line.toDec() }.toSnafu()

    override fun solvePart2(input: String) = println()
}

fun Snafu.toDec() = fold(0L) { acc, c ->
    5 * acc + when (c) {
        '=' -> -2
        '-' -> -1
        else -> c.digitToInt()
    }
}

fun Long.toSnafu(): String = buildString {
    var remainder = this@toSnafu
    while (remainder != 0L) {
        append("012=-"[remainder.mod(5)])
        remainder = (remainder + 2L).floorDiv(5)
    }
    reverse()
}