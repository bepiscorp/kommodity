package tyrell.callisto.gradle.plugin

import kotlin.test.Ignore
import kotlin.test.Test
import org.gradle.testfixtures.ProjectBuilder

/**
 * A simple unit test for the 'tyrell.callisto.base' plugin.
 */
@Ignore // TODO: Fix test
class CallistoBasePluginTest {

    @Test
    fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("tyrell.callisto.base")
    }
}
