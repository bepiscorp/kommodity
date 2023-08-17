package tyrell.callisto.gradle.internal.exception

class RecoverableRepositoryException : RuntimeException {

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)
}
