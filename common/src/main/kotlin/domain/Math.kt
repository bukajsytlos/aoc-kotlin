package domain

import kotlin.math.max
import kotlin.math.min

fun Iterable<Long>.lcm(): Long = this.reduce { lcm, num -> lcm(lcm, num) }

fun lcm(a: Long, b: Long): Long {
    return a * b / gcd(a, b)
}

tailrec fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}

fun LongRange.overlap(other: LongRange): Boolean {
    return first <= other.last && last >= other.first
}

fun LongRange.merge(other: LongRange): LongRange {
    return LongRange(min(first, other.first), max(last, other.last))
}