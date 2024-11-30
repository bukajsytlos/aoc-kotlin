import java.lang.StringBuilder
import kotlin.math.abs

class Day10(input: String) : StatefulPuzzle<Int, String>(input) {
    private val crt = CRT(input.lines().map {
        when {
            it.startsWith("addx ") -> Instruction.Add(it.substringAfter("addx ").toInt())
            else -> Instruction.Noop
        }
    })

    override fun solvePart1(): Int = crt.signalStrengths

    override fun solvePart2(): String = crt.imageRenderer.toString()

    class CRT(instructions: List<Instruction>) {
        private val instructionsQueue = ArrayDeque(instructions)
        var signalStrengths = 0
        val imageRenderer = StringBuilder()

        init {
            var registerX = 1
            var ticks = 1
            var currentInstruction: Instruction? = null
            while (instructionsQueue.isNotEmpty()) {
                if ((ticks - 20) % 40 == 0) {
                    signalStrengths += ticks * registerX
                }
                if (abs((ticks - 1) % 40 - registerX) < 2) {
                    imageRenderer.append("█")
                    print("█")
                } else {
                    imageRenderer.append(" ")
                    print(" ")
                }
                if ((ticks) % 40 == 0) {
                    imageRenderer.append(System.lineSeparator())
                    println()
                }
                if (currentInstruction == null) {
                    currentInstruction = instructionsQueue.removeFirst()
                }
                if (currentInstruction.cycles > 1) {
                    currentInstruction.cycles--
                } else {
                    if (currentInstruction is Instruction.Add) {
                        registerX += currentInstruction.amount
                    }
                    currentInstruction = instructionsQueue.removeFirst()
                }
                ticks++
            }
            imageRenderer.append(" ")
        }
    }

    sealed class Instruction(var cycles: Int) {
        data object Noop : Instruction(1)
        class Add(val amount: Int) : Instruction(2)
    }
}