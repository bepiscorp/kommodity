package tyrell.callisto.app.banner

import org.springframework.boot.Banner
import org.springframework.boot.ansi.AnsiColor
import org.springframework.boot.ansi.AnsiOutput
import org.springframework.boot.ansi.AnsiStyle
import org.springframework.core.env.Environment
import tyrell.callisto.app.banner.autoconfigure.ApplicationBannerProperties
import tyrell.callisto.app.banner.autoconfigure.ApplicationBannerProperties.AnsiText
import tyrell.callisto.base.definition.InternalLibraryApi
import tyrell.callisto.base.kotlin.extension.identity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public class ApplicationBannerPrinter constructor(
    private val bannerProperties: ApplicationBannerProperties,
) : Banner {

    init {
        // Content resource exists
        if (bannerProperties.banner != null) {
            val contentResource = bannerProperties.banner.contentResource
            require(contentResource.exists()) { "Resource ${contentResource.filename} does not exist" }
        }
    }

    // Note: Optimization to avoid twice reading in further code
    private val bannerLines: List<AnsiText> by lazy { bannerProperties.banner?.readBannerLines().orEmpty() }

    override fun printBanner(environment: Environment, sourceClass: Class<*>, printStream: PrintStream) {
        printBannerLines(printStream)
        printTitle(printStream)
        printProperties(printStream)
    }

    private fun printBannerLines(printStream: PrintStream) {
        for (line: AnsiText in bannerLines) {
            printStream.println(toAnsiString(line))
        }
    }

    private fun printTitle(printStream: PrintStream) {
        val title = bannerProperties.title ?: return
        val paddingLength: Int = run {
            val maxLineLength = bannerLines.asSequence()
                .plusElement(checkNotNull(bannerProperties.title))
                .map { it.text.length }.maxOf(Int::identity)
            maxLineLength.minus(title.text.length)
        }
        val centeredTitleText: String = buildString {
            append(" ".repeat(paddingLength / 2))
            append(title.text)
            append(" ".repeat((paddingLength / 2) + (paddingLength % 2)))
        }
        printStream.println(
            toAnsiString(
                title.copy(text = centeredTitleText),
            ),
        )
    }

    private fun printProperties(printStream: PrintStream) {
        for ((name: AnsiText, value: AnsiText) in bannerProperties.properties) {
            printStream.println("${toAnsiString(name)}: ${toAnsiString(value)}")
        }
    }

    private fun (ApplicationBannerProperties.Banner).readBannerLines(): List<AnsiText> =
        contentResource.inputStream
            .let(::InputStreamReader)
            .let(::BufferedReader)
            .lineSequence()
            .map { line -> AnsiText(text = line, color = color) }
            .toList()

    private fun toAnsiString(ansiText: AnsiText): String = AnsiOutput.toString(
        ansiText.color, (ansiText.style ?: AnsiStyle.NORMAL),
        ansiText.text, AnsiColor.DEFAULT,
    )
}
