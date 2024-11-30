import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

class Day13(input: String) : StatefulPuzzle<Int, Int>(input) {
    private val packets = input.lines()
        .filter { it.isNotBlank() }
        .map { Json.parseToJsonElement(it) }
        .toMutableList()

    override fun solvePart1(): Int = packets.asSequence().chunked(2).withIndex().filter {
        PacketComparator().compare(it.value[0], it.value[1]) == -1
    }.map { it.index + 1 }.reduce(Int::plus)

    override fun solvePart2(): Int {
        packets.add( Json.parseToJsonElement("[[2]]"))
        packets.add( Json.parseToJsonElement("[[6]]"))

        packets.sortWith(PacketComparator())
        val decoderKey = packets.withIndex()
            .filter {
                val packetAsString = Json.encodeToString(it.value)
                packetAsString == "[[2]]" || packetAsString == "[[6]]"
            }
            .map { it.index + 1 }
            .reduce(Int::times)

        return decoderKey
    }
}

class PacketComparator : Comparator<JsonElement> {
    override fun compare(first: JsonElement, second: JsonElement): Int {
        if (first is JsonPrimitive && second is JsonPrimitive) {
            val firstValue = first.content.toInt()
            val secondValue = second.content.toInt()
            if (firstValue < secondValue) {
                return -1
            } else if (firstValue > secondValue) {
                return 1
            } else {
                return 0
            }
        } else if (first is JsonArray && second is JsonArray) {
            val commonLength = minOf(first.size, second.size)
            var compareResult = 0
            for (i in (0 until commonLength)) {
                val arrayElementCompareResult = compare(first[i], second[i])
                if (arrayElementCompareResult != 0) {
                    compareResult = arrayElementCompareResult
                    break
                }
            }
            if (compareResult == 0) {
                if (first.size == second.size) {
                    compareResult = 0
                } else if (second.size == commonLength) {
                    compareResult = 1
                } else {
                    compareResult = -1
                }
            }
            return compareResult
        } else if (first is JsonPrimitive && second is JsonArray) {
            return compare(JsonArray(mutableListOf(first)), second)
        } else if (first is JsonArray && second is JsonPrimitive) {
            return compare(first, JsonArray(mutableListOf(second)))
        } else {
            return 0
        }
    }
}