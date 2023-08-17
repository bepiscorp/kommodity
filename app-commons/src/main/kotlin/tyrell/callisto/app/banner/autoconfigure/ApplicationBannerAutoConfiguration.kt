package tyrell.callisto.app.banner.autoconfigure

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tyrell.callisto.app.banner.ApplicationBannerPrinter
import tyrell.callisto.app.banner.BannerPrinterEventListener

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@Configuration
@ConditionalOnProperty(
    prefix = "callisto.application.banner", name = ["enabled"],
    havingValue = "true", matchIfMissing = false,
)
@EnableConfigurationProperties(ApplicationBannerProperties::class)
public open class ApplicationBannerAutoConfiguration {

    @set:Autowired
    internal lateinit var context: ApplicationContext

    @Bean
    public open fun applicationBannerPrinter(
        bannerProperties: ApplicationBannerProperties,
    ): ApplicationBannerPrinter = ApplicationBannerPrinter(bannerProperties)

    @Bean
    public open fun bannerPrinterEventListener(
        applicationBannerPrinter: ApplicationBannerPrinter,
    ): BannerPrinterEventListener = BannerPrinterEventListener(applicationBannerPrinter)
}
