import kotlin.math.floor

class Day13(input: String) : StatefulPuzzle<Long, Long>(input) {
    val machines = input.split("\n\n").map {
        val lines = it.lines()
        val ax = lines[0].substringAfter("+").substringBefore(",").toLong()
        val ay = lines[0].substringAfterLast("+").toLong()
        val bx = lines[1].substringAfter("+").substringBefore(",").toLong()
        val by = lines[1].substringAfterLast("+").toLong()
        val x = lines[2].substringAfter("=").substringBefore(",").toLong()
        val y = lines[2].substringAfterLast("=").toLong()
        MachineParams(ax, ay, bx, by) to (x to y)
    }

    override fun solvePart1(): Long = machines
        .mapNotNull { it.first.ab(it.second.first, it.second.second) }
        .sumOf {
            val (a, b) = it
            a * 3 + b
        }

    override fun solvePart2(): Long = machines
        .mapNotNull { it.first.ab(it.second.first + 10000000000000L, it.second.second + 10000000000000L) }
        .sumOf {
            val (a, b) = it
            a * 3 + b
        }

    data class MachineParams(val ax: Long, val ay: Long, val bx: Long, val by: Long) {
        fun ab(x: Long, y: Long): Pair<Long, Long>? {
            val b: Double = (x.toDouble() * ay - y * ax) / (bx * ay - by * ax)
            val a: Double = (y.toDouble() - b * by) / ay
            return if (floor(b) == b && floor(a) == a) Pair(a.toLong(), b.toLong()) else null
        }
    }
}

