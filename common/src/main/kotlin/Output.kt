fun <T: Any?> Array<Array<T>>.print() {
    this.forEach { 
        it.forEach { 
            print(it?.toString() ?: " ")
        }
        println()
    }
}