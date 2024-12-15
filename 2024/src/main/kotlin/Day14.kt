import domain.Position
import domain.Vector

class Day14(input: String) : StatefulPuzzle<Long, Long>(input) {
//    val maxX = 11
//    val maxY = 7
    val maxX = 101
    val maxY = 103
    val robots = input.lines().map { """p=(.+),(.+) v=(.+),(.+)""".toRegex().matchEntire(it).let {
        check(it != null)
        Robot(
            Position(it.groups[1]!!.value.toInt(), it.groups[2]!!.value.toInt()),
            Vector(it.groups[3]!!.value.toInt(), it.groups[4]!!.value.toInt()))
    }}

    override fun solvePart1(): Long {
        var tmpRobots = robots
        (1..100).forEach {
            tmpRobots = tmpRobots.map { it.move(maxX, maxY) }
        }
        return tmpRobots.count { it.position.x < maxX /2 && it.position.y < maxY /2 }.toLong() *
                tmpRobots.count { it.position.x > maxX /2 && it.position.y > maxY /2 }.toLong() *
                tmpRobots.count { it.position.x > maxX /2 && it.position.y < maxY /2 }.toLong() *
                tmpRobots.count { it.position.x < maxX /2 && it.position.y > maxY /2 }.toLong()
    }

    override fun solvePart2(): Long {
        var foundLine = false
        var seconds = 0
        var tmpRobots = robots
        while (foundLine.not()) {
            seconds++
            tmpRobots = tmpRobots.map { it.move(maxX, maxY) }
            foundLine = tmpRobots.containsLine()
        }
        tmpRobots.map { it.position }.toSet().print(maxX, maxY)
        return seconds.toLong()
    }

    data class Robot(val position: Position, val vector: Vector) {
        fun move(maxX: Int, maxY: Int): Robot = Robot(position.plus(vector).let { Position((maxX + (it.x % maxX)) % maxX, (maxY + (it.y % maxY)) % maxY )}, vector)
    }

    private fun List<Robot>.containsLine(): Boolean {
        val robotPositions = map { it.position }.toSet()
        var numberOfRobotsInLine = 0
        var inLine = false
        for (y in (0..maxY)) {
            for (x in (0..maxX)) {
                if (Position(x, y) in robotPositions) {
                    inLine = true
                    numberOfRobotsInLine++
                } else {
                    inLine = false
                    numberOfRobotsInLine = 0
                }
                if (numberOfRobotsInLine > 10) return true
            }
        }
        return false
    }
}


