class Day4(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val cards = input.lines()
        .map { cardLine ->
            val cardString = cardLine.split(":")
            val numbersString = cardString[1].split(" |")
            Card(
                cardString[0].substringAfter("Card ").trim().toInt(),
                numbersString[0].chunked(3).map { it.trim() }.map { it.toInt() }.toSet(),
                numbersString[1].chunked(3).map { it.trim() }.map { it.toInt() }.toSet()
            )
        }

    override fun solvePart1(): Int = cards.sumOf { it.calculatePoints() }

    override fun solvePart2(): Int {
        val cardCount = Array(cards.size) { 1 }.also {
            cards.forEach { card ->
                repeat(card.winningNumbersCount()) { i ->
                    it[card.id + i] += it[card.id - 1]
                }
            }
        }
        return cardCount.sum()
    }

    data class Card(val id: Int, private val winningNumbers: Set<Int>, private val numbers: Set<Int>) {
        fun calculatePoints(): Int = if (winningNumbersCount() > 0) 1 shl (winningNumbersCount() - 1) else 0

        fun winningNumbersCount() = winningNumbers.intersect(numbers).size
    }
}