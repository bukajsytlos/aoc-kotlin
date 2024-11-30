class Day21(input: String) : StatefulPuzzle<Long, Long>(input) {
    private val monkeyReferences = input.lines().associate {
        it.substringBefore(":") to it.substringAfter(": ")
    }
    private val monkeys = mutableMapOf<String, Monkey>()
    init {
        monkeyReferences.forEach {
            getMonkey(it.key, monkeys, monkeyReferences)
        }
    }

    override fun solvePart1(): Long {
        val rootMonkey = monkeys["root"]!! as Monkey.Operation
        return rootMonkey.evaluate()
    }

    override fun solvePart2(): Long {
        val rootMonkey = monkeys["root"]!! as Monkey.Operation
        return if (rootMonkey.left.dependsOn("humn")) {
            resolve(rootMonkey.left, rootMonkey.right.evaluate())
        } else {
            resolve(rootMonkey.right, rootMonkey.left.evaluate())
        }
    }
}

fun resolve(monkey: Monkey, value: Long): Long {
    if (monkey is Monkey.Operation) {
        return if (monkey.left.dependsOn("humn")) {
            val otherValue = monkey.right.evaluate()
            when (monkey) {
                is Monkey.Operation.Sum -> resolve(monkey.left, value - otherValue)
                is Monkey.Operation.Subtract -> resolve(monkey.left, value + otherValue)
                is Monkey.Operation.Product -> resolve(monkey.left, value / otherValue)
                is Monkey.Operation.Division -> resolve(monkey.left, value * otherValue)
                else -> error("waht")
            }
        } else {
            val otherValue = monkey.left.evaluate()
            when (monkey) {
                is Monkey.Operation.Sum -> resolve(monkey.right, value - otherValue)
                is Monkey.Operation.Subtract -> resolve(monkey.right, otherValue - value)
                is Monkey.Operation.Product -> resolve(monkey.right, value / otherValue)
                is Monkey.Operation.Division -> resolve(monkey.right, otherValue / value)
                else -> error("waht")
            }
        }
    } else {
        return value
    }
}

fun getMonkey(name: String, monkeys: MutableMap<String, Monkey>, monkeyReferences: Map<String, String>): Monkey {
    var monkey = monkeys[name]
    if (monkey != null) {
        return monkey
    }
    val operation = monkeyReferences[name]!!
    val value = operation.toLongOrNull()
    if (value != null) {
        monkey = Monkey.Number(name, value)
    } else {
        monkey = when {
            operation.contains(" + ") -> Monkey.Operation.Sum(
                name,
                getMonkey(operation.substringBefore(" + "), monkeys, monkeyReferences),
                getMonkey(operation.substringAfter(" + "), monkeys, monkeyReferences)
            )

            operation.contains(" - ") -> Monkey.Operation.Subtract(
                name,
                getMonkey(operation.substringBefore(" - "), monkeys, monkeyReferences),
                getMonkey(operation.substringAfter(" - "), monkeys, monkeyReferences)
            )

            operation.contains(" * ") -> Monkey.Operation.Product(
                name,
                getMonkey(operation.substringBefore(" * "), monkeys, monkeyReferences),
                getMonkey(operation.substringAfter(" * "), monkeys, monkeyReferences)
            )

            operation.contains(" / ") -> Monkey.Operation.Division(
                name,
                getMonkey(operation.substringBefore(" / "), monkeys, monkeyReferences),
                getMonkey(operation.substringAfter(" / "), monkeys, monkeyReferences)
            )

            else -> error("unknown monkey type")
        }
    }
    monkeys[name] = monkey
    return monkey
}

sealed class Monkey(val name: String) {
    abstract fun evaluate(): Long
    abstract fun dependsOn(name: String): Boolean

    class Number(name: String, var value: Long) : Monkey(name) {
        override fun evaluate(): Long = value
        override fun dependsOn(name: String) = this.name == name
    }

    abstract class Operation(name: String, val left: Monkey, val right: Monkey, private val operation: (Long, Long) -> Long) : Monkey(name) {
        override fun evaluate(): Long = operation(left.evaluate(), right.evaluate())
        override fun dependsOn(name: String): Boolean = left.name == name || right.name == name || left.dependsOn(name) || right.dependsOn(name)
        class Sum(name: String, left: Monkey, right: Monkey) : Operation(name, left, right, { l, r -> l + r })

        class Subtract(name: String, left: Monkey, right: Monkey) : Operation(name, left, right, { l, r -> l - r })

        class Product(name: String, left: Monkey, right: Monkey) : Operation(name, left, right, { l, r -> l * r })

        class Division(name: String, left: Monkey, right: Monkey) : Operation(name, left, right, { l, r -> l / r })
    }
}