class Day4(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val sectionAssignmentsPairs = input.lines().map { line ->
        line.split(",").map { pair ->
            pair.split("-").let {
                SectionAssignment(
                    it[0].toInt(),
                    it[1].toInt()
                )
            }
        }
    }

    override fun solvePart1() = sectionAssignmentsPairs.count { it[0].contains(it[1]) || it[0].isContainedBy(it[1]) }

    override fun solvePart2() = sectionAssignmentsPairs.count { it[0].overlap(it[1]) }
}

class SectionAssignment(private val start: Int, private val end: Int) {
    fun contains(other: SectionAssignment): Boolean = other.start >= start && other.end <= end
    fun isContainedBy(other: SectionAssignment): Boolean = start >= other.start && end <= other.end
    fun overlap(other: SectionAssignment): Boolean = start <= other.end && end >= other.start
}