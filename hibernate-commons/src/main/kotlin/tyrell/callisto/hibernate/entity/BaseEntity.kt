package tyrell.callisto.hibernate.entity

import org.springframework.data.domain.Persistable
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.data.type.WithId
import java.io.Serializable
import java.util.Objects
import javax.persistence.MappedSuperclass
import javax.persistence.Transient

/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
@MappedSuperclass
public abstract class BaseEntity<ID : Serializable> : WithId<ID>, Persistable<ID>, Serializable {

    @Transient
    abstract override fun getId(): ID?

    public abstract fun setId(id: ID)

    @Transient
    override fun isNew(): Boolean = (id == null)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseEntity<*>) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun toString(): String = (this::class.simpleName + "(id=$id)")

    public companion object {

        public const val ID: String = "id"
    }
}
