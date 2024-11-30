import domain.Position
import java.io.File
import kotlin.math.abs
import kotlin.math.sign

class Day6(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val signal = input.lines().first()

    override fun solvePart1(): Int = signal.findMarker(4)

    override fun solvePart2(): Int = signal.findMarker(14)

    private fun String.findMarker(packetLength: Int) = this.windowed(packetLength).indexOfFirst {
        packetLength == it.toSet().size
    } + packetLength
}