package tyrell.callisto.gradle.internal.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathExpression
import javax.xml.xpath.XPathFactory

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
/*internal*/ object JupiterMoonsScrapper {

    // val logger: KLogger = LoggerFactory.getLogger(JupiterMoonsScrapper::class).toKLogger()

    private val nameRegex: Regex = "^[a-zA-Z]+\$".toRegex()

    /**
     * Fetches Jupiter moon names [published](https://en.wikipedia.org/wiki/Moons_of_Jupiter)
     * in the article "_Moons of Jupiter_" on Wikipedia.
     */
    const val sourceUrl = "https://en.wikipedia.org/wiki/Moons_of_Jupiter"

    /**
     * XPath expression that returns text nodes with Jupyter Moon name
     */
    private const val namesColumnXpath = "//*/table[1]/tbody/tr[*]/td[3]//a/text()"

    /**
     * Fetches Jupiter moon names and returns the ones consisting of one word with characters from English alphabet.
     *
     * See description of source and content layout in documentation of [sourceUrl].
     *
     * @see doFetchJupiterMoonNames
     * @see sourceUrl
     * @throws java.net.UnknownHostException if host could not be resolved (e.g. no internet connection)
     */
    suspend fun fetchJupiterMoonNames(): List<String> = Dispatchers.IO { doFetchJupiterMoonNames().toList() }

    /**
     * Fetches Jupiter moon names from article "[Moons of Jupiter](https://en.wikipedia.org/wiki/Moons_of_Jupiter)"
     * published on Wikipedia.
     */
    private fun doFetchJupiterMoonNames(): Sequence<String> {
        val xmlDocument: Document = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(URL(sourceUrl).openStream().buffered())

        val xpathExpression: XPathExpression = XPathFactory.newInstance()
            .newXPath()
            .compile(namesColumnXpath)

        val nameNodes: Sequence<Node> = run {
            val nodeList = xpathExpression.evaluate(xmlDocument, XPathConstants.NODESET) as NodeList
            (0 until nodeList.length).asSequence().map { idx -> nodeList.item(idx) }
        }

        return nameNodes
            .map(Node::getNodeValue)
            .filter { it.matches(nameRegex) }
    }
}
