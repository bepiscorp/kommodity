package tyrell.callisto.signal.unix.model

import tyrell.callisto.base.definition.LibraryApi

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public fun UnixSignalCode.asJvmSignal(): JvmSignal = JvmSignal(code)

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public enum class UnixSignalCode(public val code: String, public val action: UnixSignalAction) {

    /**
     * Abort signal from abort(3)
     */
    SIGABRT("ABRT", UnixSignalAction.CORE),

    /**
     * Timer signal from alarm(2)
     */
    SIGALRM("ALRM", UnixSignalAction.TERMINATE),

    /**
     * Bus error (bad memory access)
     */
    SIGBUS("BUS", UnixSignalAction.CORE),

    /**
     * Child stopped or terminated
     */
    SIGCHLD("CHLD", UnixSignalAction.IGNORE),

    /**
     * A synonym for SIGCHLD
     */
    SIGCLD("CLD", UnixSignalAction.IGNORE),

    /**
     * Continue if stopped
     */
    SIGCONT("CONT", UnixSignalAction.CONTINUE),

    /**
     * Emulator trap
     */
    SIGEMT("EMT", UnixSignalAction.TERMINATE),

    /**
     * Floating-point exception
     */
    SIGFPE("FPE", UnixSignalAction.CORE),

    /**
     * Hangup detected on controlling terminal or death of controlling process
     */
    SIGHUP("HUP", UnixSignalAction.TERMINATE),

    /**
     * Illegal Instruction
     */
    SIGILL("ILL", UnixSignalAction.CORE),

    /**
     * Interrupt from keyboard
     */
    SIGINT("INT", UnixSignalAction.TERMINATE),

    /**
     * I/O now possible (4.2BSD)
     */
    SIGIO("IO", UnixSignalAction.TERMINATE),

    /**
     * IOT trap. A synonym for SIGABRT
     */
    SIGIOT("IOT", UnixSignalAction.CORE),

    /**
     * Kill signal
     */
    SIGKILL("KILL", UnixSignalAction.TERMINATE),

    /**
     * File lock lost (unused)
     */
    SIGLOST("LOST", UnixSignalAction.TERMINATE),

    /**
     * Broken pipe: write to pipe with no readers; see pipe(7)
     */
    SIGPIPE("PIPE", UnixSignalAction.TERMINATE),

    /**
     * Pollable event (Sys V); synonym for SIGIO
     */
    SIGPOLL("POLL", UnixSignalAction.TERMINATE),

    /**
     * Profiling timer expired
     */
    SIGPROF("PROF", UnixSignalAction.TERMINATE),

    /**
     * Power failure (System V)
     */
    SIGPWR("PWR", UnixSignalAction.TERMINATE),

    /**
     * A synonym for SIGPWR
     */
    SIGINFO("INFO", SIGPWR.action),

    /**
     * Quit from keyboard
     */
    SIGQUIT("QUIT", UnixSignalAction.CORE),

    /**
     * Invalid memory reference
     */
    SIGSEGV("SEGV", UnixSignalAction.CORE),

    /**
     * Stack fault on coprocessor (unused)
     */
    SIGSTKFLT("STKFLT", UnixSignalAction.TERMINATE),

    /**
     * Stop process
     */
    SIGSTOP("STOP", UnixSignalAction.STOP),

    /**
     * Stop typed at terminal
     */
    SIGTSTP("TSTP", UnixSignalAction.STOP),

    /**
     * Bad system call (SVr4); see also seccomp(2)
     */
    SIGSYS("SYS", UnixSignalAction.CORE),

    /**
     * Termination signal
     */
    SIGTERM("TERM", UnixSignalAction.TERMINATE),

    /**
     * Trace/breakpoint trap
     */
    SIGTRAP("TRAP", UnixSignalAction.CORE),

    /**
     * Terminal input for background process
     */
    SIGTTIN("TTIN", UnixSignalAction.STOP),

    /**
     * Terminal output for background process
     */
    SIGTTOU("TTOU", UnixSignalAction.STOP),

    /**
     * Synonymous with SIGSYS
     */
    SIGUNUSED("UNUSED", UnixSignalAction.CORE),

    /**
     * Urgent condition on socket (4.2BSD)
     */
    SIGURG("URG", UnixSignalAction.IGNORE),

    /**
     * User-defined signal 1
     */
    SIGUSR1("USR1", UnixSignalAction.TERMINATE),

    /**
     * User-defined signal 2
     */
    SIGUSR2("USR2", UnixSignalAction.TERMINATE),

    /**
     * Virtual alarm clock (4.2BSD)
     */
    SIGVTALRM("VTALRM", UnixSignalAction.TERMINATE),

    /**
     * CPU time limit exceeded (4.2BSD); see setrlimit(2)
     */
    SIGXCPU("XCPU", UnixSignalAction.CORE),

    /**
     * File size limit exceeded (4.2BSD); see setrlimit(2)
     */
    SIGXFSZ("XFSZ", UnixSignalAction.CORE),

    /**
     * Window resize signal (4.3BSD, Sun)
     */
    SIGWINCH("WINCH", UnixSignalAction.IGNORE);

    public companion object {

        @JvmStatic
        private val valueByCode: Map<String, UnixSignalCode> = values().associateBy { it.code }

        @JvmStatic
        public fun fromCode(value: String): UnixSignalCode {
            val actualValue = if (!value.startsWith("SIG")) "SIG$value" else value
            return valueByCode.getValue(actualValue)
        }
    }
}
