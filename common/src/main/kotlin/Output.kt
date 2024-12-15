import domain.Position

fun <T: Any?> Array<Array<T>>.print() {
    this.forEach { 
        it.forEach { 
            print(it?.toString() ?: " ")
        }
        println()
    }
}
fun Set<Position>.print(maxX: Int, maxY: Int) {
    for (y in (0..maxY)) {
        for (x in (0..maxX)) {
            print(if (contains(Position(x, y))) "#" else " ")
        }
        println()
    }
}