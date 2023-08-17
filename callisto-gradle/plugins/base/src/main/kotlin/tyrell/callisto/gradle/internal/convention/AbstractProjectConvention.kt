package tyrell.callisto.gradle.internal.convention

import org.gradle.api.GradleException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tyrell.callisto.gradle.internal.CallistoProject

public abstract class AbstractProjectConvention<P : CallistoProject> : ProjectConvention<P> {

    protected var logger: Logger = LoggerFactory.getLogger(this::class.java)
        private set

    private val conventionName: String = checkNotNull(this::class.simpleName)

    public final override fun verify(project: P, mode: VerificationMode) {
        if (mode == VerificationMode.IGNORE) return

        val result: VerificationResult = verifyWithResult(project)
        val warningMessages = result.warnings
        if (mode != VerificationMode.STRICT && result.warnings.isNotEmpty()) {
            warningMessages.asSequence()
                .mapIndexed { idx, msg -> if (result.errors.size > 1) "(${idx + 1}): $msg" else msg }
                .joinToString(prefix = "Convention [$conventionName] verification warning:\n", separator = "\n\n")
                .let(logger::warn)
        }

        val errorMessages = result.run { if (mode == VerificationMode.STRICT) errors + warnings else errors }
        if (errorMessages.isNotEmpty()) {
            throw errorMessages.asSequence()
                .mapIndexed { idx, msg -> if (result.errors.size > 1) "(${idx + 1}): $msg" else msg }
                .joinToString(prefix = "Convention [$conventionName] verification error:\n", separator = "\n\n")
                .let(::GradleException)
        }
    }

    public final override fun matches(project: P, mode: VerificationMode): Boolean {
        val result: VerificationResult = verifyWithResult(project)
        return when (mode) {
            VerificationMode.STRICT -> result.errors.isEmpty() && result.warnings.isEmpty()
            VerificationMode.WARN -> result.errors.isEmpty()
            VerificationMode.IGNORE -> true
        }
    }

    protected abstract fun VerificationResultBuilder.doVerify(project: P)

    private fun verifyWithResult(project: P): VerificationResult =
        VerificationResultBuilder().apply { doVerify(project) }.build()

    protected class VerificationResult(
        val warnings: List<String>,
        val errors: List<String>
    )

    protected class VerificationResultBuilder {

        private val warnings: MutableList<String> = ArrayList()

        private val errors: MutableList<String> = ArrayList()

        fun warning(message: String) = synchronized(this) { warnings += message }

        fun error(message: String) = synchronized(this) { errors += message }

        fun errorIf(predicate: () -> Boolean, message: String) {
            if (predicate()) error(message)
        }

        fun warningIf(predicate: () -> Boolean, message: String) {
            if (predicate()) warning(message)
        }

        fun build(): VerificationResult = VerificationResult(warnings = warnings, errors = errors)
    }
}
