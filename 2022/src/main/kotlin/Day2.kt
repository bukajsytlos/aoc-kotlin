object Day2 : StatelessPuzzle<Int, Int>() {
    override fun solvePart1(input: String): Int = input.lines()
        .map { Round(it.substringBefore(" ").asElfHandShape(), it.substringAfter(" ").asMyHandShape()) }
        .map { it.evaluatePoints() }.sumOf { it.second }

    override fun solvePart2(input: String): Int = input.lines()
        .map {
            val player1HandShape = it.substringBefore(" ").asElfHandShape()
            val player2HandShape = it.substringAfter(" ").asResultHandShape(player1HandShape)
            Round(player1HandShape, player2HandShape)
        }
        .map { it.evaluatePoints() }
        .sumOf { it.second }
}

sealed class HandShape(val points: Int) {
    abstract fun evaluatePoints(other: Rock): Pair<Int, Int>
    abstract fun evaluatePoints(other: Paper): Pair<Int, Int>
    abstract fun evaluatePoints(other: Scissors): Pair<Int, Int>
    abstract fun evaluateWinningHandShape(): HandShape
    abstract fun evaluateDrawHandShape(): HandShape
    abstract fun evaluateLoseHandShape(): HandShape
}

class Rock : HandShape(1) {
    override fun evaluatePoints(other: Rock): Pair<Int, Int> = Pair(3, 3)
    override fun evaluatePoints(other: Paper): Pair<Int, Int> = Pair(0, 6)
    override fun evaluatePoints(other: Scissors): Pair<Int, Int> = Pair(6, 0)
    override fun evaluateWinningHandShape(): HandShape = Scissors()
    override fun evaluateDrawHandShape(): HandShape = Rock()
    override fun evaluateLoseHandShape(): HandShape = Paper()
}

class Paper : HandShape(2) {
    override fun evaluatePoints(other: Rock): Pair<Int, Int> = Pair(6, 0)
    override fun evaluatePoints(other: Paper): Pair<Int, Int> = Pair(3, 3)
    override fun evaluatePoints(other: Scissors): Pair<Int, Int> = Pair(0, 6)
    override fun evaluateWinningHandShape(): HandShape = Rock()
    override fun evaluateDrawHandShape(): HandShape = Paper()
    override fun evaluateLoseHandShape(): HandShape = Scissors()
}

class Scissors : HandShape(3) {
    override fun evaluatePoints(other: Rock): Pair<Int, Int> = Pair(0, 6)
    override fun evaluatePoints(other: Paper): Pair<Int, Int> = Pair(6, 0)
    override fun evaluatePoints(other: Scissors): Pair<Int, Int> = Pair(3, 3)
    override fun evaluateWinningHandShape(): HandShape = Paper()
    override fun evaluateDrawHandShape(): HandShape = Scissors()
    override fun evaluateLoseHandShape(): HandShape = Rock()
}

fun String.asElfHandShape(): HandShape = when (this) {
    "A" -> Rock()
    "B" -> Paper()
    "C" -> Scissors()
    else -> error("unknown hand shape")
}

fun String.asMyHandShape(): HandShape = when (this) {
    "X" -> Rock()
    "Y" -> Paper()
    "Z" -> Scissors()
    else -> error("unknown hand shape")
}

fun String.asResultHandShape(player1HandShape: HandShape): HandShape = when (this) {
    "X" -> player1HandShape.evaluateWinningHandShape()
    "Y" -> player1HandShape.evaluateDrawHandShape()
    "Z" -> player1HandShape.evaluateLoseHandShape()
    else -> error("unknown result hand shape")
}

class Round(val player1HandShape: HandShape, val player2HandShape: HandShape) {
    fun evaluatePoints(): Pair<Int, Int> {
        var player1Points = player1HandShape.points
        var player2Points = player2HandShape.points
        val (p1Points, p2Points) = when (player2HandShape) {
            is Rock -> player1HandShape.evaluatePoints(player2HandShape)
            is Paper -> player1HandShape.evaluatePoints(player2HandShape)
            is Scissors -> player1HandShape.evaluatePoints(player2HandShape)
        }
        player1Points += p1Points
        player2Points += p2Points
        return Pair(player1Points, player2Points)
    }
}