import kotlin.math.abs

class Day1(input: String) : StatefulPuzzle<Int, Int>(input) {
    val turns = input.lines().map { (if (it[0] == 'L') -1 else 1) * it.substring(1).toInt() }

    override fun solvePart1(): Int {
        var pos = 50
        var zeroPosCount = 0
        for (turn in turns) {
            pos += turn
            if (pos % 100 == 0) zeroPosCount++
        }
        return zeroPosCount
    }

    override fun solvePart2(): Int {
        var pos = 50
        var zerosClickCount = 0
        for (turn in turns) {
            zerosClickCount += abs(turn) / 100
            val newPos = pos + turn % 100
            if (newPos <= 0 && pos != 0 || newPos >= 100) zerosClickCount++
            pos = (newPos + 100) % 100
        }
        return zerosClickCount
    }
}