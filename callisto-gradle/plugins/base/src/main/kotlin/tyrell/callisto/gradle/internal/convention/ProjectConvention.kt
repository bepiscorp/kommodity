package tyrell.callisto.gradle.internal.convention

import tyrell.callisto.gradle.internal.CallistoProject

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
public interface ProjectConvention<P : CallistoProject> {

    fun verify(project: P, mode: VerificationMode = VerificationMode.WARN)

    fun matches(project: P, mode: VerificationMode = VerificationMode.WARN): Boolean
}

public enum class VerificationMode { STRICT, WARN, IGNORE }
