import domain.Position3D
import kotlin.collections.map

class Day8(input: String) : StatefulPuzzle<Int, Long>(input) {
    val circuits = input.lines().map { Position3D.from(it) }.map { Circuit().addJunctionBoxAt(it) }
    val boxes = circuits.map { it.boxes.first() }
    val boxesByDistance = (0..<boxes.lastIndex).flatMap { b1 ->
        (b1 + 1..boxes.lastIndex).map {
            val box1 = boxes[b1]
            val box2 = boxes[it]
            box1 to box2 to box1.position.distanceTo(box2.position)
        }
    }.sortedBy { it.second }
    val boxesToConnect = if (boxes.size == 20) 10 else 1000

    override fun solvePart1(): Int {
        boxesByDistance.take(boxesToConnect).forEach { it.first.first.circuit.connect(it.first.second) }
        return circuits.map { it.boxes.size }.sortedDescending().take(3).reduce(Int::times)
    }

    override fun solvePart2(): Long {
        var result = 0L
        for (pair in boxesByDistance.drop(boxesToConnect)) {
            pair.first.first.circuit.connect(pair.first.second)
            val box = pair.first.first
            if (box.circuit.boxes.size == boxes.size) {
                result = box.position.x.toLong() * pair.first.second.position.x
                break
            }
        }
        return result;
    }

    class Circuit() {
        val boxes = mutableSetOf<JunctionBox>()
        fun addJunctionBoxAt(position: Position3D): Circuit {
            boxes.add(JunctionBox(position, this))
            return this
        }
        fun addJunctionBox(junctionBox: JunctionBox): Circuit {
            boxes.add(junctionBox)
            junctionBox.moveToCircuit(this)
            return this
        }
        fun contains(junctionBox: JunctionBox): Boolean = junctionBox in boxes
        fun connect(junctionBox: JunctionBox) {
            if (contains(junctionBox)) { return }
            val boxes1 = junctionBox.circuit.boxes
            boxes1.forEach { addJunctionBox(it) }
            boxes1.clear()
        }
    }

    class JunctionBox(val position: Position3D, var circuit: Circuit) {
        fun moveToCircuit(circuit: Circuit) {
            this.circuit = circuit
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as JunctionBox

            return position == other.position
        }

        override fun hashCode(): Int {
            return position.hashCode()
        }

    }
}


