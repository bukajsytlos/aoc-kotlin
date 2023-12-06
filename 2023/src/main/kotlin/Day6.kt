object Day6 : StatelessPuzzle<Int, Int>() {

    override fun solvePart1(input: String): Int {
        val boat = Boat(1)
        val races = input.lines()
            .map {
                Regex("""\d+""").findAll(it.substringAfter(":"))
                    .map { it.value.toLong() }
            }.zipWithNext { l1, l2 ->
                l1.zip(l2)
            }.flatMap { it.map { Race(it.first, it.second) } }
        return races
            .map { race ->
                (0..race.time).count { race.isWinning(boat, it) }
            }
            .reduce(Int::times)
    }

    override fun solvePart2(input: String): Int {
        val boat = Boat(1)
        val race = input.lines()
            .map {
                Regex("""\d+""").findAll(it.substringAfter(":").replace(" ", ""))
                    .map { it.value.toLong() }
            }.zipWithNext { l1, l2 ->
                l1.zip(l2)
            }.flatMap { it.map { Race(it.first, it.second) } }
            .first()
        val winningFromTime = (0..race.time)
            .first { race.isWinning(boat, it) }
        val winningToTime = (race.time downTo 0)
            .first { race.isWinning(boat, it) }
        return (winningToTime - winningFromTime + 1).toInt()
    }

    data class Race(val time: Long, val distance: Long) {
        fun isWinning(boat: Boat, chargeTime: Long) = boat.distance(chargeTime, time - chargeTime) > distance
    }
    data class Boat(val acceleration: Int) {
        fun distance(accelerationTime: Long, travelTime: Long): Long = accelerationTime * acceleration * travelTime
    }
}