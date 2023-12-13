class Day13(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val patterns: List<Pattern> = input.split("\n\n")
        .map { Pattern(it) }

    override fun solvePart1(): Int = patterns.sumOf { it.summarize(0) }

    override fun solvePart2(): Int = patterns.sumOf { it.summarize(1) }


    class Pattern(rawData: String) {
        private val data: Array<Array<Boolean>> =
            rawData.lines().let { Array(it.size) { i -> it[i].map { it == '#' }.toTypedArray() } }

        fun summarize(maxDelta: Int): Int = scanVertical(maxDelta) ?: (scanHorizontal(maxDelta) * 100)
        
        private fun scanHorizontal(maxDelta: Int): Int {
            val height = data.size
            val width = data[0].size
            return (1..<height).first { lineIndex ->
                var delta = 0
                for (lineOffset in (1..minOf(lineIndex, height - lineIndex))) {
                    delta += (0..<width).count { data[lineIndex - lineOffset][it] != data[lineIndex + lineOffset - 1][it] }
                    if (delta > maxDelta) return@first false
                }
                delta == maxDelta
            }
        }

        private fun scanVertical(maxDelta: Int): Int? {
            val height = data.size
            val width = data[0].size
            return (1..<width).firstOrNull { lineIndex ->
                var delta = 0
                for (lineOffset in (1..minOf(lineIndex, width - lineIndex))) {
                    delta += (0..<height).count { data[it][lineIndex - lineOffset] != data[it][lineIndex + lineOffset - 1] }
                    if (delta > maxDelta) return@firstOrNull false
                }
                delta == maxDelta
            }
        }
    }
}