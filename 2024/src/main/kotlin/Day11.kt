class Day11(input: String) : StatefulPuzzle<Long, Long>(input) {
    var stonesCountByNumber: Map<Long, Long> = input.split(" ").map { it.toLong() }.groupBy { it }.mapValues { it.value.size.toLong() }
    
    override fun solvePart1(): Long {
        repeat(25) {
            stonesCountByNumber = stonesCountByNumber.evolve()
        }
        return stonesCountByNumber.values.sum()
    }

    override fun solvePart2(): Long {
        repeat(50) {
            stonesCountByNumber = stonesCountByNumber.evolve()
        }
        return stonesCountByNumber.values.sum()
    }
}

private fun Map<Long, Long>.evolve(): Map<Long, Long> {
    val tmpMap = mutableMapOf<Long, Long>()
    entries.forEach {
        val newKeys: List<Long> = it.key.evaluateRules()
        newKeys.forEach { newKey -> tmpMap.merge(newKey, it.value) { v1, v2 -> v1 + v2 } }
    }
    return tmpMap
}

private fun Long.evaluateRules(): List<Long> {
    val sValue = this.toString()
    val sLength = sValue.length
    return when {
        this == 0L -> listOf(1L)
        sLength % 2 == 0 -> listOf(sValue.substring(0, sLength / 2).toLong(), sValue.substring(sLength / 2, sLength).toLong())
        else -> listOf(this * 2024L)
    }
}
