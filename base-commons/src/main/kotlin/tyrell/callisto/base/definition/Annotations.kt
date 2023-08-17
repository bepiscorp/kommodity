package tyrell.callisto.base.definition

/**
 * Annotation [LibraryApi] is applied to source code to indicate that the given code is used as API
 * by other modules and backward compatibility **MUST** be supported on changing source codes
 * marked by annotation [LibraryApi]
 *
 * Developer **MAY** use [LibraryApi] annotation together with annotations:
 * - [kotlin.DslMarker];
 * - [kotlin.ExtensionFunctionType];
 * - [kotlin.PublishedApi];
 * - [kotlin.ParameterName].
 *
 * Developer **MUST NOT** break backward compatibility (source and binary)
 * of declarations marked with [LibraryApi] annotation.
 *
 * Developer **SHOULD NOT** apply [LibraryApi] annotation functions and properties of a class
 * if [LibraryApi] annotation is applied to the class.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see kotlin.DslMarker
 * @see kotlin.ExtensionFunctionType
 * @see kotlin.PublishedApi
 * @see kotlin.ParameterName
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.TYPEALIAS, AnnotationTarget.PROPERTY)
public annotation class LibraryApi

/**
 * Marks declarations in the library that are **delicate** &mdash;
 * they have limited use-case and shall be used with care in general code.
 *
 * Any use of a delicate declaration has to be carefully reviewed to make sure
 * it is properly used and does not create problems (e.g. memory and resource leaks).
 *
 * Carefully read documentation of any declaration marked as [DelicateLibraryApi].
 *
 * Developer **SHOULD** mark declaration with [DelicateLibraryApi] annotation
 * if user of declaration has significant probability of using declaration in inappropriate way.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see kotlinx.coroutines.DelicateCoroutinesApi
 */
@MustBeDocumented
@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "This is a delicate API and its use requires care." +
        " Make sure you fully read and understand documentation of the declaration that is marked as a delicate API.",
)
public annotation class DelicateLibraryApi

/**
 * Marks declarations that are experimental features.
 *
 * Experimental feature has **no** backward compatibility guarantees, including both binary and source compatibility.
 * Its API and semantics can and will be changed in next releases.
 *
 * Experimental feature can be used to evaluate its real-world strengths and weaknesses, gather and provide feedback.
 * According to the feedback, feature will be refined on its road to stabilization and promotion to a stable API.
 *
 * The best way to speed up preview feature promotion is providing the feedback on the feature.
 *
 * Developer **MUST** mark declaration with [ExperimentalLibraryApi] annotation
 * if the declaration published as API but there is no backward compatibility for it (source or binary)
 *
 * Developer **SHOULD NOT** apply [ExperimentalLibraryApi] annotation to functions, properties, etc. of a class
 * if [ExperimentalLibraryApi] annotation is applied to the class.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see kotlinx.coroutines.ExperimentalCoroutinesApi
 */
@MustBeDocumented
@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "This is an experimental API. It may be changed or removed in the future.",
)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.TYPEALIAS,
)
public annotation class ExperimentalLibraryApi

/**
 * Annotation [InternalLibraryApi] is applied to source code
 * to indicate that the given code is exposed for module internal needs.
 * Source code of external modules (other domains) **MUST NOT** use source code marked with [InternalLibraryApi]
 * as backward compatibility for sources is not guaranteed.
 *
 * Developer **MUST** mark declaration with [InternalLibraryApi] annotation
 * if it is not published as API (simply saying — it is internal)
 *
 * Developer **SHOULD NOT** apply [InternalLibraryApi] annotation to functions and properties of a class
 * if [InternalLibraryApi] annotation is applied to the class.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see kotlinx.coroutines.InternalCoroutinesApi
 */
@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.TYPEALIAS, AnnotationTarget.PROPERTY)
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "This is an internal API that should not be used from outside of the library. " +
        "No compatibility guarantees are provided. " +
        "It is recommended to report your use-case of internal API to the owner's issue tracker, " +
        "so stable API could be provided instead",
)
public annotation class InternalLibraryApi

/**
 * Marks declarations that are **obsolete** in some API, which means that the design of the corresponding
 * declarations has serious known flaws, and they will be redesigned in the future.
 *
 * Roughly speaking, these declarations will be deprecated in the future but there is no replacement for them yet,
 * so they cannot be deprecated right away.
 *
 * Developer **SHOULD** mark declaration with [ObsoleteLibraryApi] annotation
 * if declaration is obsolete but there is no replacement for it.
 *
 * Developer **MUST NOT** mark declaration with [kotlin.Deprecated] annotation
 * when it is marked with [ObsoleteLibraryApi] annotation.
 *
 * Developer **SHOULD NOT** apply [ObsoleteLibraryApi] annotation to functions, properties, etc. of a class
 * if [ObsoleteLibraryApi] annotation is applied to the class.
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 * @see kotlinx.coroutines.ObsoleteCoroutinesApi
 * @see kotlin.Deprecated
 */
@MustBeDocumented
@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
public annotation class ObsoleteLibraryApi
