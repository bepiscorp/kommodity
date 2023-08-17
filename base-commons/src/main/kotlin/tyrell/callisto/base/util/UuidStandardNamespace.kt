package tyrell.callisto.base.util

import java.util.UUID

/**
 * Name Space IDs from standard [UUID RFC](https://www.rfc-editor.org/rfc/rfc4122#appendix-C)
 * @author Mikhail Gostev
 * @since 0.0.1
 */
public object UuidStandardNamespace {

    /**
     * Name string is a fully-qualified domain name
     */
    public val DNS: UUID = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8")

    /**
     * Name string is a URL
     */
    public val URL: UUID = UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8")

    /**
     * Name string is an ISO OID
     */
    public val OID: UUID = UUID.fromString("6ba7b812-9dad-11d1-80b4-00c04fd430c8")

    /**
     * Name string is an X.500 DN (in DER or a text output format)
     */
    public val X500: UUID = UUID.fromString("a7b814-9dad-11d1-80b4-00c04fd430c8")
}
