import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

object Day6 : StatelessPuzzle<Int, Int>() {

    override fun solvePart1(input: String): Int {
        val races = input.lines()
            .map {
                Regex("""\d+""").findAll(it.substringAfter(":"))
                    .map { it.value.toLong() }
            }.zipWithNext { l1, l2 ->
                l1.zip(l2)
            }.flatMap { it.map { Race(it.first, it.second) } }

        return races
            .map { it.winningTimesCount() }
            .reduce(Int::times)
    }

    override fun solvePart2(input: String): Int {
        val race = input.lines()
            .map {
                Regex("""\d+""").findAll(it.substringAfter(":").replace(" ", ""))
                    .map { it.value.toLong() }
            }.zipWithNext { l1, l2 ->
                l1.zip(l2)
            }.flatMap { it.map { Race(it.first, it.second) } }
            .first()

        return race.winningTimesCount()
    }

    data class Race(val time: Long, val distance: Long) {
        fun winningTimesCount(): Int {
            val winningFromTime =
                ceil((time - sqrt((time * time - 4 * (distance + 1)).toDouble())) / 2)
            val winningToTime =
                floor((time + sqrt((time * time - 4 * (distance + 1)).toDouble())) / 2)

            return (winningToTime - winningFromTime + 1).toInt()
        }
    }
}