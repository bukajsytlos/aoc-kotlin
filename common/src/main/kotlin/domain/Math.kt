package domain

fun Iterable<Long>.lcm(): Long = this.reduce { lcm, num -> lcm(lcm, num) }

fun lcm(a: Long, b: Long): Long {
    return a * b / gcd(a, b)
}

tailrec fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}