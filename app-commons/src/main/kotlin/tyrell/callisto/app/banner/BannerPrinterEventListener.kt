package tyrell.callisto.app.banner

import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextStartedEvent
import tyrell.callisto.base.definition.InternalLibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@InternalLibraryApi
public class BannerPrinterEventListener(
    private val applicationBannerPrinter: ApplicationBannerPrinter,
) : ApplicationListener<ContextStartedEvent> {

    override fun onApplicationEvent(event: ContextStartedEvent) {
        applicationBannerPrinter.printBanner(
            environment = event.applicationContext.environment,
            sourceClass = SpringApplication::class.java,
            printStream = System.out,
        )
    }
}
