import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kotlin.reflect.KClass

open class StatelessPuzzleTest<DayPuzzle: StatelessPuzzle<Part1, Part2>, Part1, Part2>(
    private val day: KClass<DayPuzzle>,
    private val part1TestResult: Part1,
    private val part2TestResult: Part2,
    private val skipSolve: Boolean = false
) : AnnotationSpec() {
    private val puzzleInstance = day.objectInstance!!

    @Test
    fun part1TestInput() {
        puzzleInstance.solvePart1(day.testInput()) shouldBe part1TestResult
    }
    @Test
    fun part2TestInput() {
        puzzleInstance.solvePart2(day.testInput2()) shouldBe part2TestResult
    }

    @Test
    fun part1() {
        if (skipSolve) return
        println(puzzleInstance.solvePart1(day.input()))
    }
    @Test
    fun part2() {
        if (skipSolve) return
        println(puzzleInstance.solvePart2(day.input()))
    }
}