package tyrell.callisto.hibernate.entity

import org.hibernate.envers.Audited
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.data.type.NameAware
import java.io.Serializable
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.MappedSuperclass

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
@Audited @MappedSuperclass
public abstract class NameAwareEntity<ID : Serializable> : CodeAwareEntity<ID>, NameAware {

    @get:Basic
    @get:Column(name = "name", nullable = false)
    override var name: String? = null

    @get:Basic
    @get:Column(name = "description", length = 1024)
    public var description: String? = null

    public constructor(
        code: String,
        name: String,
        description: String?,
    ) : super(code = code) {
        this.name = name
        this.description = description
    }

    public constructor() : super()

    public companion object {

        public const val NAME: String = "name"

        public const val DESCRIPTION: String = "description"
    }
}
