class Day7(input: String) : StatefulPuzzle<Int, Int>(input) {
    private var handBids = input.lines()
        .map {
            Hand(it.substringBefore(" ").map { Card.valueOf(it.toString()) }) to it.substringAfter(" ").toInt()
        }

    override fun solvePart1(): Int = handBids
        .sortedWith { o1, o2 -> Hand.rank1Comparator.compare(o1.first, o2.first) }
        .mapIndexed { index, handBid -> handBid.second * (index + 1) }
        .sum()

    override fun solvePart2(): Int = handBids
        .sortedWith { o1, o2 -> Hand.rank2Comparator.compare(o1.first, o2.first) }
        .mapIndexed { index, handBid -> handBid.second * (index + 1) }
        .sum()

    data class Hand(val cards: List<Card>) {
        private val cardsFrequency: Map<Card, Int> = cards.groupingBy { it }.eachCount()
        private val cardsFrequencyWithoutJokers: Map<Card, Int> =
            cards.filter { it != Card.J }.groupingBy { it }.eachCount()
        private val numberOfJokers: Int = cards.count { it == Card.J }

        fun rank1(): Int {
            return when {
                cardsFrequency.containsValue(5) -> 20
                cardsFrequency.containsValue(4) -> 19
                cardsFrequency.containsValue(3) && cardsFrequency.containsValue(2) -> 18
                cardsFrequency.containsValue(3) -> 17
                cardsFrequency.filter { it.value == 2 }.size == 2 -> 16
                cardsFrequency.containsValue(2) -> 15
                else -> 14
            }
        }

        fun rank2(): Int {
            return when {
                cardsFrequency.containsValue(5)
                        || cardsFrequencyWithoutJokers.containsValue(4) && numberOfJokers == 1
                        || cardsFrequencyWithoutJokers.containsValue(3) && numberOfJokers == 2
                        || cardsFrequencyWithoutJokers.containsValue(2) && numberOfJokers == 3
                        || numberOfJokers == 4 -> 20

                cardsFrequency.containsValue(4)
                        || cardsFrequencyWithoutJokers.containsValue(3) && numberOfJokers == 1
                        || cardsFrequencyWithoutJokers.containsValue(2) && numberOfJokers == 2
                        || numberOfJokers == 3 -> 19

                cardsFrequency.containsValue(3) && cardsFrequency.containsValue(2)
                        || cardsFrequencyWithoutJokers.filter { it.value == 2 }.size == 2 && numberOfJokers == 1 -> 18

                cardsFrequency.containsValue(3)
                        || cardsFrequencyWithoutJokers.containsValue(2) && numberOfJokers == 1
                        || numberOfJokers == 2 -> 17

                cardsFrequency.filter { it.value == 2 }.size == 2 -> 16
                cardsFrequency.containsValue(2)
                        || numberOfJokers == 1 -> 15

                else -> 14
            }
        }
        private fun firstDifferentCards(otherHand: Hand): Pair<Card, Card>? = cards.zip(otherHand.cards)
            .firstOrNull { it.first != it.second }

        companion object {
            val rank1Comparator = Comparator<Hand> { o1, o2 -> o1.rank1() - o2.rank1() }
                .thenComparing { o1, o2 -> o1.firstDifferentCards(o2)
                        ?.let { it.first.rank1 - it.second.rank1 }
                        ?: 0
                }
            val rank2Comparator = Comparator<Hand> { o1, o2 -> o1.rank2() - o2.rank2() }
                .thenComparing { o1, o2 -> o1.firstDifferentCards(o2)
                        ?.let { it.first.rank2 - it.second.rank2 }
                        ?: 0
                }
        }
    }

    enum class Card(val rank1: Int, val rank2: Int) : Comparable<Card> {
        `2`(2, 2),
        `3`(3, 3),
        `4`(4, 4),
        `5`(5, 5),
        `6`(6, 6),
        `7`(7, 7),
        `8`(8, 8),
        `9`(9, 9),
        T(10, 10),
        J(11, 1),
        Q(12, 12),
        K(13, 13),
        A(14, 14)
    }
}