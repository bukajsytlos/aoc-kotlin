import domain.Position

class Day3(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val itemsByPosition: MutableMap<Position, Item> = mutableMapOf()
    private val positionsByItem: MutableMap<Item, Set<Position>> = mutableMapOf()

    init {
        input.lines().forEachIndexed { y, line ->
            var numberString = ""
            var numberPositions = mutableSetOf<Position>()
            line.forEachIndexed { x, c ->
                if (c.isDigit()) {
                    numberString += c
                    numberPositions.add(Position(x, y))
                    if (x == line.lastIndex) {
                        val partNumber = Item.PartNumber(numberString.toInt())
                        numberPositions.forEach { itemsByPosition[it] = partNumber }
                        positionsByItem[partNumber] = numberPositions
                        numberString = ""
                        numberPositions = mutableSetOf()
                    }
                } else {
                    var item: Item? = null
                    if (c == '*') {
                        item = Item.Gear()
                    } else if (c != '.') {
                        item = Item.Other()
                    }
                    if (numberString.isNotEmpty()) {
                        val partNumber = Item.PartNumber(numberString.toInt())
                        numberPositions.forEach { itemsByPosition[it] = partNumber }
                        positionsByItem[partNumber] = numberPositions
                        numberString = ""
                        numberPositions = mutableSetOf()
                    }
                    item?.let {
                        val position = Position(x, y)
                        itemsByPosition[position] = it
                        positionsByItem[it] = setOf(position)
                    }
                }
            }
        }
    }

    override fun solvePart1(): Int = positionsByItem
        .filter { it.key is Item.PartNumber }
        .filter { it.value.flatMap { partNumberPositions -> partNumberPositions.adjacents() }.distinct().any { pos -> itemsByPosition[pos] is Item.Gear || itemsByPosition[pos] is Item.Other } }
        .map { (it.key as Item.PartNumber).number }
        .sum()

    override fun solvePart2(): Int = positionsByItem
        .filter { it.key is Item.Gear }
        .map { it.value }
        .map { gearPositions ->
            gearPositions.flatMap { gearPosition -> gearPosition.adjacents() }
                .filter { adjacentPosition -> itemsByPosition[adjacentPosition] is Item.PartNumber }
                .map { itemsByPosition[it] as Item.PartNumber }
                .toSet()
        }
        .filter { it.size == 2 }
        .map { partNumbers -> partNumbers.map { it.number }}
        .sumOf { it.reduce(Int::times) }

    sealed class Item {
        class PartNumber(val number: Int) : Item()
        class Gear : Item()
        class Other : Item()
    }
}