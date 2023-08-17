package tyrell.callisto.data.liquibase

import tyrell.callisto.base.definition.InternalLibraryApi
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.name

/**
 * [LogicalResourceComparator] class used to sort Liquibase changelogs.
 * It supports logical comparison when '10.0.0' is bigger than '1.0.0', that is not true for lexicographical comparison
 *
 * @author Mikhail Gostev
 * @since 0.1.0
 */
@InternalLibraryApi
public class LogicalResourceComparator : Comparator<String> {

    override fun compare(
        firstResource: String?,
        secondResource: String?,
    ): Int = when {
        (firstResource === secondResource) -> 0
        (firstResource == null) -> -1
        (secondResource == null) -> 1
        else -> comparePaths(
            firstPath = Path(firstResource),
            secondPath = Path(secondResource),
        )
    }

    private fun comparePaths(
        firstPath: Path,
        secondPath: Path,
    ): Int {
        val firstPathElements = firstPath.toList().map(Path::name)
        val secondPathElements = secondPath.toList().map(Path::name)
        for ((firstElement, secondElement) in firstPathElements.zip(secondPathElements)) {
            val elementComparison = compareFileNamesLogically(firstElement, secondElement)
            if (elementComparison != 0) return elementComparison
        }

        return firstPath.compareTo(secondPath)
    }

    private fun compareFileNamesLogically(
        firstFileName: String,
        secondFileName: String,
    ): Int {
        fun extractNumbers(value: String) = "\\d+".toRegex()
            .findAll(value)
            .map(MatchResult::value)
            .map(String::toInt)
            .toList()

        val firstFileNameNumbers: List<Int> = extractNumbers(firstFileName)
        val secondFileNameNumbers: List<Int> = extractNumbers(secondFileName)
        for ((first, second) in firstFileNameNumbers.zip(secondFileNameNumbers)) {
            val comparison = first.compareTo(second)
            if (comparison != 0) return comparison
        }

        return firstFileNameNumbers.size
            .compareTo(secondFileNameNumbers.size)
    }
}
