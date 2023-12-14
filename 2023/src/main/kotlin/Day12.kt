class Day12(input: String) : StatefulPuzzle<Long, Long>(input) {
    private val records = input.lines().map { Record(it.substringBefore(" "), it.substringAfter(" ").split(",").map { it.toInt() }) }
    
    override fun solvePart1(): Long = records.sumOf { it.countArrangements() }

    override fun solvePart2(): Long = records
        .map { foldedRecord ->
            Record(
                (foldedRecord.springs + '?').repeat(4) + foldedRecord.springs,
                List(5) { foldedRecord.checksum }.flatten()
            )
        }
        .sumOf { it.countArrangements() }
    
    data class Record(val springs: String, val checksum: List<Int>) {
        private val operationalTypes = setOf('.', '?')
        private val damagedTypes = setOf('?', '#')
        private val cache = mutableMapOf<Pair<String, List<Int>>, Long>()
        
        fun countArrangements(): Long = countArrangements(springs, checksum)

        private fun countArrangements(springsPart: String, checksumPart: List<Int>): Long {
            return cache.getOrPut(springsPart to checksumPart) {
                if (checksumPart.isEmpty()) {
                    return@getOrPut if (springsPart.all { it in operationalTypes }) 1 else 0
                }
                val springsPartWithoutOperational =
                    springsPart.indexOfFirst { it != '.' }.takeIf { it != -1 }?.let { springsPart.substring(it) }
                        ?: springsPart
                if (checksumPart.sum() + checksumPart.size - 1 > springsPartWithoutOperational.length) return@getOrPut 0
                var count = 0L
                if (springsPartWithoutOperational.first() == '?') count += countArrangements(
                    springsPartWithoutOperational.substring(1),
                    checksumPart
                )
                val numberOfDamaged = checksumPart.first()
                val fits = springsPartWithoutOperational.subSequence(0, numberOfDamaged).all { it in damagedTypes }
                if (!fits) return@getOrPut count
                if (springsPartWithoutOperational.length == numberOfDamaged)
                    count += countArrangements(
                        springsPartWithoutOperational.substring(numberOfDamaged),
                        checksumPart.subList(1, checksumPart.size)
                    )
                else if (springsPartWithoutOperational[numberOfDamaged] in operationalTypes)
                    count += countArrangements(
                        springsPartWithoutOperational.substring(numberOfDamaged + 1),
                        checksumPart.subList(1, checksumPart.size)
                    )
                return@getOrPut count
            }
        }
    }
}