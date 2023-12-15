import java.util.LinkedList

class Day15(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val steps = input.split(",").map { when {
        it.endsWith("-") -> Step.Remove(it.substringBefore("-"))
        else -> Step.Insert(it.substringBefore("="), it.substringAfter("=").toInt())
    }}
    private val hashmap = Array(255) { LinkedList<Step.Insert>() }
    
    override fun solvePart1(): Int = steps.map { it.asString() }.sumOf { it.hash() }

    override fun solvePart2(): Int {
        steps.forEach { step -> 
            when(step) {
                is Step.Insert -> {
                    hashmap[step.id.hash()].also {
                        val i = it.indexOfFirst { it.id == step.id }
                        if (i != -1) {
                            it.removeAt(i)
                            it.add(i, step)
                        } else {
                            it.offerLast(step)
                        }
                    }
                }
                is Step.Remove -> hashmap[step.id.hash()].removeIf { it.id == step.id }
            }
        }
        return hashmap.foldIndexed(0) { boxIndex, acc, steps -> acc + steps.foldIndexed(0) { slotIndex, slot, step -> slot + (boxIndex + 1) * (slotIndex + 1) * step.focalLength }}
    }
    
    sealed class Step(val id: String) {
        abstract fun asString(): String
        class Remove(id: String): Step(id) {
            override fun asString() = "$id-"
        }

        class Insert(id: String, val focalLength: Int): Step(id) {
            override fun asString(): String = "$id=$focalLength"
        }
    }

    fun String.hash() = this.fold(0) {acc, c -> (acc + c.code) * 17 % 256 }
}