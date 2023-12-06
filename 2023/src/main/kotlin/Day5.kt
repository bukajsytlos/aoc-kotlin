class Day5(input: String) : StatefulPuzzle<Long, Long>(input) {
    private var seeds: List<Long>
    private var seedRanges: List<LongRange>
    private var seedToSoil: Category
    private var soilToFertilizer: Category
    private var fertilizerToWater: Category
    private var waterToLight: Category
    private var lightToTemperature: Category
    private var temperatureToHumidity: Category
    private var humidityToLocation: Category

    init {
        val blocks = input.split("\n\n")
        seeds = blocks[0].substringAfter("seeds: ").split(" ").map { it.toLong() }
        seedRanges = blocks[0].substringAfter("seeds: ").split(" ").chunked(2)
            .map { it.first().toLong()..<it.first().toLong() + it.last().toLong() }.sortedBy { it.first }

        seedToSoil = Category(blocks[1].lines().drop(1).map { it.mapToCategoryMap() })
        soilToFertilizer = Category(blocks[2].lines().drop(1).map { it.mapToCategoryMap() })
        fertilizerToWater = Category(blocks[3].lines().drop(1).map { it.mapToCategoryMap() })
        waterToLight = Category(blocks[4].lines().drop(1).map { it.mapToCategoryMap() })
        lightToTemperature = Category(blocks[5].lines().drop(1).map { it.mapToCategoryMap() })
        temperatureToHumidity = Category(blocks[6].lines().drop(1).map { it.mapToCategoryMap() })
        humidityToLocation = Category(blocks[7].lines().drop(1).map { it.mapToCategoryMap() })
    }

    override fun solvePart1(): Long = seeds
        .map { seedToSoil.map(it) }
        .map { soilToFertilizer.map(it) }
        .map { fertilizerToWater.map(it) }
        .map { waterToLight.map(it) }
        .map { lightToTemperature.map(it) }
        .map { temperatureToHumidity.map(it) }
        .map { humidityToLocation.map(it) }
        .min()

    override fun solvePart2(): Long = humidityToLocation
        .map(
            temperatureToHumidity
                .map(
                    lightToTemperature
                        .map(
                            waterToLight
                                .map(
                                    fertilizerToWater
                                        .map(
                                            soilToFertilizer
                                                .map(
                                                    seedToSoil
                                                        .map(seedRanges)
                                                )
                                        )
                                )
                        )
                )
        ).minOf { it.first }

    data class Category(val maps: List<CategoryMap>) {
        fun map(value: Long) = maps.firstOrNull { it.contains(value) }?.map(value) ?: value
        fun map(inputRanges: List<LongRange>): List<LongRange> {
            var unmappedInputRanges = inputRanges.toMutableList()
            val unprocessedMaps = ArrayDeque(maps)
            val processedInputRanges = mutableListOf<LongRange>()
            while (unprocessedMaps.isNotEmpty()) {
                val map = unprocessedMaps.removeFirst()
                val remainderInputRanges = mutableListOf<LongRange>()
                unmappedInputRanges.forEach { map.intersect(it).forEach { processedInputRanges.add(map.map(it)) } }
                unmappedInputRanges.forEach { map.remainders(it).forEach { remainderInputRanges.add(it) } }
                unmappedInputRanges = remainderInputRanges
            }
            return processedInputRanges + unmappedInputRanges
        }
    }

    data class CategoryMap(val sourceStart: Long, val destinationStart: Long, val range: Long) {
        private val diff = destinationStart - sourceStart

        fun map(value: Long) = value + diff
        fun contains(value: Long) = sourceStart <= value && value < sourceStart + range

        fun intersect(inputRange: LongRange): List<LongRange> = listOf(
            (maxOf(inputRange.first, sourceStart)..minOf(inputRange.last, sourceStart + range - 1))
        ).filter { !it.isEmpty() }

        fun remainders(inputRange: LongRange): List<LongRange> = listOf(
            (minOf(inputRange.first, sourceStart)..<minOf(inputRange.last + 1, sourceStart)),
            (maxOf(inputRange.first, sourceStart + range)..<maxOf(inputRange.last + 1, sourceStart + range)),
        )
            .filter { !(it.first == inputRange.first && it.last == inputRange.last) || !(it.first == sourceStart && it.last == sourceStart + range - 1) }
            .filter { !it.isEmpty() }

        fun map(range: LongRange): LongRange = range.first + diff..range.last + diff
    }

    fun String.mapToCategoryMap(): CategoryMap {
        val mapRange = this.split(" ")
        return CategoryMap(mapRange[1].toLong(), mapRange[0].toLong(), mapRange[2].toLong())
    }
}