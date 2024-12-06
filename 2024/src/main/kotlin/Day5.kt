class Day5(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val lines = input.split("\n\n")
    private val updates = lines[1].lines().map { it.split(",").map { it.toInt() } }
    private val pageRules = lines[0].lines().map { it.substringBefore("|").toInt() to it.substringAfter("|").toInt() }.toSet()

    override fun solvePart1(): Int {
        return updates
            .filter { it.obeysRules() }
            .sumOf { it[it.size / 2] }
    }

    override fun solvePart2(): Int {
        return updates
            .filter { it.obeysRules().not() }
            .map { it.fixByRules() }
            .sumOf { it[it.size / 2] }
    }

    private fun List<Int>.obeysRules(): Boolean {
        forEachIndexed { i, v ->
            subList(i + 1, size).forEach { v2 ->
                if (!(pageRules.contains(v to v2))) {
                    return false
                }
            }
        }
        return true
    }

    private fun List<Int>.fixByRules(): List<Int> {
        return this.sortedWith { o1, o2 -> if (pageRules.contains(o1 to o2)) 1 else -1 }
    }
}
