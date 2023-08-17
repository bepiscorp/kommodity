package tyrell.callisto.app.banner.autoconfigure

import org.springframework.boot.ansi.AnsiColor
import org.springframework.boot.ansi.AnsiStyle
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.core.io.Resource

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "callisto.application.banner")
public data class ApplicationBannerProperties(

    val enabled: Boolean = false,

    val banner: Banner? = null,

    val title: AnsiText? = null,

    val properties: List<BannerProperty> = emptyList(),
) {

    public data class AnsiText(

        val text: String,

        val color: AnsiColor = AnsiColor.DEFAULT,

        val style: AnsiStyle? = null,
    )

    public data class Banner(

        val contentResource: Resource,

        val color: AnsiColor = AnsiColor.DEFAULT,
    )

    public data class BannerProperty(

        val name: AnsiText,

        val value: AnsiText,
    )
}
