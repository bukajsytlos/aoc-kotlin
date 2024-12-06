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
        val tmpArray = this.reversed().toTypedArray()
        for (i in 0..< size - 1) {
            for (j in 0..< size - 1 - i) {
                val v1 = tmpArray[j]
                val v2 = tmpArray[j + 1]
                if (pageRules.contains(v1 to v2)) {
                    tmpArray[j + 1] = v1
                    tmpArray[j] = v2
                }
            }
        }
        return tmpArray.toList()
    }
}
