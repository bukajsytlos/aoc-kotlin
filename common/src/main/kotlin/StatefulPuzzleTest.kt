import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

open class StatefulPuzzleTest<DayPuzzle : StatefulPuzzle<Part1, Part2>, Part1, Part2>(
    day: KClass<DayPuzzle>,
    private val part1TestResult: Part1,
    private val part2TestResult: Part2,
    private val skipSolve: Boolean = false
) : AnnotationSpec() {
    private val puzzleInstance = day.primaryConstructor!!.call(day.input())
    private val puzzleTestInstance = day.primaryConstructor!!.call(day.testInput())

    @Test
    fun testInput() {
        puzzleTestInstance.solvePart1() shouldBe part1TestResult
        puzzleTestInstance.solvePart2() shouldBe part2TestResult
    }

    @Test
    fun puzzleInput() {
        if (skipSolve) return
        println(puzzleInstance.solvePart1())
        println(puzzleInstance.solvePart2())
    }
}