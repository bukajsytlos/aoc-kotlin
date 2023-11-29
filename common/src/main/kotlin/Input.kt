import kotlin.reflect.KClass

fun KClass<*>.input(): String = input("${this.simpleName}.txt")

fun KClass<*>.testInput(): String = input("${this.simpleName}test.txt")
fun KClass<*>.testInput2(): String = runCatching { input("${this.simpleName}test2.txt") }.getOrElse { this.testInput() }

fun input(fileName: String): String = ClassLoader.getSystemClassLoader().getResource(fileName).readText()