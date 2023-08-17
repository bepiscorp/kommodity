package tyrell.callisto.gradle.internal.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import tyrell.callisto.gradle.internal.util.JupiterMoonsScrapper.sourceUrl
import java.net.URL

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
/*internal*/ object StarNamesScrapper {

    private val nameRegex: Regex = "^[a-zA-Z]+\$".toRegex()

    /**
     * URL to the table in text format with all star names [published](https://www.pas.rochester.edu/~emamajek/WGSN/IAU-CSN.txt) by
     * [IAU WGSN](https://en.wikipedia.org/wiki/IAU_Working_Group_on_Star_Names) (International Astronomical Union, Working Group on Star Names).
     *
     * Each line starting with [commentMarker] is treated as comment.
     * Other lines are treated as text table lines with fixed width columns.
     *
     * Content of URL consists of text table with fixed width columns. See example below:
     * ```txt
     * #(1)              (2)               (3)          (4)   (5)   (6) (7)  (8)         (9)    (10)   (11) (12)       (13)       (14)       (15)
     * #Name/ASCII       Name/Diacritics   Designation  ID    ID    Con #    WDS_J       Vmag    HIP     HD RA(J2000)  Dec(J2000) Date       Notes
     * Absolutno         Absolutno         XO-5         _     _     Lyn _    _          12.13      _      _ 116.716506  39.094572 2019-12-17
     * Acamar            Acamar            HR 897       tet01 Î¸1    Eri A    02583-4018  2.88  13847  18622  44.565311 -40.304672 2016-07-20 *
     * Achernar          Achernar          HR 472       alf   Î±     Eri A    _           0.45   7588  10144  24.428523 -57.236753 2016-06-30
     * ```
     *
     * Column #1 ([asciiNameColumnBounds]) — Proper name adopted by IAU WGSN (ASCII version)
     */
    const val sourceUrl = "https://www.pas.rochester.edu/~emamajek/WGSN/IAU-CSN.txt"

    /**
     * Sequence that identifies line in [sourceUrl] content as comment.
     */
    private const val commentMarker = "#"

    /**
     * Left and right bounds of `ASCII/Width` column in [sourceUrl]
     */
    private val asciiNameColumnBounds = IntRange(start = 0, endInclusive = 17)

    /**
     * Fetches star names and returns the ones consisting of one word with characters from English alphabet.
     *
     * See description of source and content layout in documentation of [sourceUrl].
     *
     * @see sourceUrl
     * @throws java.net.UnknownHostException if host could not be resolved (e.g. no internet connection)
     */
    suspend fun fetchStarNames(): List<String> = Dispatchers.IO { doFetchStarNames().toList() }

    /**
     * Fetches non escaped star names.
     * See description of source and content layout in documentation of [sourceUrl].
     *
     * Proper name adopted by IAU WGSN (ASCII version)
     */
    private fun doFetchStarNames(): Sequence<String> = URL(sourceUrl).openStream()
        .bufferedReader()
        .lineSequence()
        .filterNot { line -> line.startsWith(commentMarker) }
        .filter { line -> line.length >= asciiNameColumnBounds.run { last - first } }
        .map { line -> line.subSequence(asciiNameColumnBounds) }
        .map(CharSequence::trim)
        .map(CharSequence::toString)
        .filter { line -> line.matches(nameRegex) }
}
