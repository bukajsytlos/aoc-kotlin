import domain.Direction

class Day14(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val platform: Array<Array<Rock?>> = input.asTyped2DArray { Rock.from(it) }
    private val knownPlatformStatesByCycle = mutableMapOf<Int, Int>()
    private val knownPlatformStatesLoads = mutableMapOf<Int, Int>()

    override fun solvePart1(): Int  {
        platform.tilt(Direction.N)
        return platform.totalLoadOnNorthSupportBeams()
    }

    override fun solvePart2(): Int {
        (1..1_000_000_000).forEach { cycle -> 
            platform.tilt(Direction.N)
            platform.tilt(Direction.W)
            platform.tilt(Direction.S)
            platform.tilt(Direction.E)
            val key = platform.contentDeepHashCode()
            if (knownPlatformStatesByCycle.contains(key)) {
                val cycleRepeatStartIndex = knownPlatformStatesByCycle.getValue(key)
                val cycleLength = cycle - cycleRepeatStartIndex
                return knownPlatformStatesLoads.getValue(((1_000_000_000 - cycleRepeatStartIndex) % cycleLength) + cycleRepeatStartIndex)
            }
            knownPlatformStatesByCycle[key] = cycle
            knownPlatformStatesLoads[cycle] = platform.totalLoadOnNorthSupportBeams()
        }
        return platform.totalLoadOnNorthSupportBeams()
    }

    private fun Array<Array<Rock?>>.tilt(direction: Direction) {
        when (direction) {
            Direction.N -> {
                this[0].indices.forEach { x ->
                    var lastFilledCoordinate = -1
                    this.indices.forEach { y ->
                        when(platform[y][x]) {
                            Rock.ROUNDED -> {
                                lastFilledCoordinate++
                                platform[lastFilledCoordinate][x] = Rock.ROUNDED
                                if (lastFilledCoordinate != y) platform[y][x] = null
                            }
                            Rock.CUBED -> lastFilledCoordinate = y
                            null -> null
                        }
                    }
                }
            }
            Direction.W -> {
                this.indices.forEach { y ->
                    var lastFilledCoordinate = -1
                    this[0].indices.forEach { x ->
                        when(platform[y][x]) {
                            Rock.ROUNDED -> {
                                lastFilledCoordinate++
                                platform[y][lastFilledCoordinate] = Rock.ROUNDED
                                if (lastFilledCoordinate != x) platform[y][x] = null
                            }
                            Rock.CUBED -> lastFilledCoordinate = x
                            null -> null
                        }
                    }
                }
            }
            Direction.S -> {
                this[0].indices.forEach { x ->
                    var lastFilledCoordinate = this.size
                    this.indices.reversed().forEach { y ->
                        when(platform[y][x]) {
                            Rock.ROUNDED -> {
                                lastFilledCoordinate--
                                platform[lastFilledCoordinate][x] = Rock.ROUNDED
                                if (lastFilledCoordinate != y) platform[y][x] = null
                            }
                            Rock.CUBED -> lastFilledCoordinate = y
                            null -> null
                        }
                    }
                }
            }
            Direction.E -> {
                this.indices.forEach { y ->
                    var lastFilledCoordinate = this[0].size
                    this[0].indices.reversed().forEach { x ->
                        when(platform[y][x]) {
                            Rock.ROUNDED -> {
                                lastFilledCoordinate--
                                platform[y][lastFilledCoordinate] = Rock.ROUNDED
                                if (lastFilledCoordinate != x) platform[y][x] = null
                            }
                            Rock.CUBED -> lastFilledCoordinate = x
                            null -> null
                        }
                    }
                }
            }
            else -> error("imposibru")
        }
    }
    
    private fun Array<Array<Rock?>>.totalLoadOnNorthSupportBeams() = this.foldIndexed(0) { index: Int, sum: Int, rocks: Array<Rock?> ->
        sum + rocks.count { it == Rock.ROUNDED } * (platform.size - index)
    }
    
    private enum class Rock(val symbol: Char) {
        ROUNDED('O'),
        CUBED('#'),
        ;

        companion object {
            fun from(symbol: Char): Rock? = entries.firstOrNull { it.symbol == symbol }
        }

        override fun toString(): String {
            return symbol.toString()
        }
    }
}