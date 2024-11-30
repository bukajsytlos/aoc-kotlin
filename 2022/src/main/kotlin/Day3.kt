import kotlin.collections.map
import kotlin.text.toList

typealias Item = Char

object Day3 : StatelessPuzzle<Int, Int>() {
    val itemPriorities: Map<Item, Int> = (('a'..'z') + ('A'..'Z')).mapIndexed { index, c -> c to index + 1 }.toMap()

    override fun solvePart1(input: String): Int = input.lines().map { Rucksack(it.toList()) }
        .map { it.compartment1().intersect(it.compartment2().toSet()).first() }
        .sumOf { itemPriorities[it]!! }

    override fun solvePart2(input: String): Int = input.lines().map { Rucksack(it.toList()) }.chunked(3)
        .map { group ->
            group.map { it.content }
                .reduce { acc, content -> acc.intersect(content.toSet()) }
                .first()
        }
        .sumOf { itemPriorities[it]!! }
}

class Rucksack(val content: Iterable<Item>) {
    fun compartment1(): Iterable<Item> = content.take(content.count() / 2)
    fun compartment2(): Iterable<Item> = content.drop(content.count() / 2)
}