import kotlin.reflect.KClass

fun KClass<*>.input(): String = input("${this.simpleName}.txt")

fun KClass<*>.testInput(): String = input("${this.simpleName}test.txt")
fun KClass<*>.testInput2(): String = runCatching { input("${this.simpleName}test2.txt") }.getOrElse { this.testInput() }

fun input(fileName: String): String = ClassLoader.getSystemClassLoader().getResource(fileName)?.readText() ?: error("$fileName not found")

fun String.asIntRange(delimiter: Char = '-'): IntRange = IntRange(this.substringBefore(delimiter).toInt(), this.substringAfter(delimiter).toInt())
fun String.asLongRange(delimiter: Char = '-'): LongRange = LongRange(this.substringBefore(delimiter).toLong(), this.substringAfter(delimiter).toLong())

inline fun <reified T: Any?> String.asTyped2DArray(mapper: (Char) -> T): Array<Array<T>> { 
    val lines = this.lines()
    return Array(lines.size) { i -> lines[i].map { mapper(it) }.toTypedArray() }
}