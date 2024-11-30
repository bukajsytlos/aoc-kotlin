class Day11(input: String) : StatefulPuzzle<Long, Long>(input) {
    //todo parse
    private val monkeysPart1 = listOf(
        Monkey(listOf(54, 89, 94).toItems(), { old -> old * 7 }, 17, 5, 3),
        Monkey(listOf(66, 71).toItems(), { old -> old + 4 }, 3, 0, 3),
        Monkey(listOf(76, 55, 80, 55, 55, 96, 78).toItems(), { old -> old + 2 }, 5, 7, 4),
        Monkey(listOf(93, 69, 76, 66, 89, 54, 59, 94).toItems(), { old -> old + 7 }, 7, 5, 2),
        Monkey(listOf(80, 54, 58, 75, 99).toItems(), { old -> old * 17 }, 11, 1, 6),
        Monkey(listOf(69, 70, 85, 83).toItems(), { old -> old + 8 }, 19, 2, 7),
        Monkey(listOf(89).toItems(), { old -> old + 6 }, 2, 0, 1),
        Monkey(listOf(62, 80, 58, 57, 93, 56).toItems(), { old -> old * old }, 13, 6, 4),
    )
    private val monkeysPart2 = monkeysPart1.map { it.copy() }

    override fun solvePart1(): Long = monkeysPart1.apply { evaluateRounds(20) { it / 3 }}.run { determineMonkeyBusiness() }

    override fun solvePart2(): Long { //todo wrong
        val ambiguousWorryLevelConstant = monkeysPart2.map { it.predicateDivisor }.reduce { acc, l -> acc * l }
        monkeysPart2.evaluateRounds(10000) { it % ambiguousWorryLevelConstant }
        return monkeysPart2.determineMonkeyBusiness()
    }


    data class Monkey(
        private val items: MutableList<Item>,
        private val operation: (Long) -> Long,
        val predicateDivisor: Long,
        private val successfulDestination: Int,
        private val unsuccessfulDestination: Int,
        var numberOfInspections: Long = 0,
    ) {
        fun inspectAndThrowTo(worryLevelReducer: (Long) -> Long): Pair<Item, Int>? {
            if (items.isEmpty()) {
                return null
            }
            numberOfInspections++
            val item = items.removeFirst()
            item.worryLevel = worryLevelReducer(operation(item.worryLevel))
            val dest = if ((item.worryLevel % predicateDivisor).toInt() == 0) {
                Pair(item, successfulDestination)
            } else {
                Pair(item, unsuccessfulDestination)
            }
            return dest
        }

        fun acceptItem(item: Item) {
            items.add(item)
        }
    }

    fun List<Monkey>.evaluateRounds(numberOfRounds: Int, worryLevelReducer: (Long) -> Long) {
        repeat(numberOfRounds) {
            this.forEach { monkey ->
                var currentThrow: Pair<Item, Int>?
                while (run {
                        currentThrow = monkey.inspectAndThrowTo(worryLevelReducer)
                        currentThrow != null
                    }) {
                    this[currentThrow?.second!!].acceptItem(currentThrow?.first!!)
                }
            }
        }
    }

    fun List<Monkey>.determineMonkeyBusiness() = this.map { it.numberOfInspections }.sortedDescending().take(2).reduce { acc, i -> acc * i }

    data class Item(var worryLevel: Long)

    fun List<Int>.toItems() = this.map { Item(it.toLong()) }.toMutableList()
}