class Day2(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val games = input.lines()
        .map {
            val gameCubesSplit = it.split(": ")
            val gameId = gameCubesSplit[0].substringAfter("Game ").toInt()
            val reveals = gameCubesSplit[1].split("; ")
                .map { reveal ->
                    reveal.split(", ")
                        .associate { cubeInfo ->
                            cubeInfo.substringAfter(" ") to cubeInfo.substringBefore(" ").toInt()
                        }
                }.map { revealMap ->
                    Reveal(revealMap["red"]?: 0, revealMap["green"]?: 0, revealMap["blue"]?: 0)
                }
            Game(gameId, reveals)
        }

    override fun solvePart1(): Int = games.filter { game ->
        val (maxRed, maxGreen, maxBlue) = game.maxPossibleReveal()
        maxRed <= 12 && maxGreen <= 13 && maxBlue <= 14
    }.sumOf { it.id }

    override fun solvePart2(): Int = games.sumOf { game ->
        val (maxRed, maxGreen, maxBlue) = game.maxPossibleReveal()
            maxRed * maxGreen *maxBlue
    }
}

data class Game(val id: Int, val reveals: List<Reveal>) {
    fun maxPossibleReveal() = Reveal(
        reveals.maxOf { it.redCount },
        reveals.maxOf { it.greenCount },
        reveals.maxOf { it.blueCount }
    )
}

data class Reveal(val redCount: Int, val greenCount: Int, val blueCount: Int)