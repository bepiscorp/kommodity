package tyrell.callisto.hibernate.entity

import org.hibernate.envers.Audited
import tyrell.callisto.base.definition.LibraryApi
import tyrell.callisto.data.type.CodeAware
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
public abstract class CodeAwareEntity<ID : Serializable> : AuditUserAwareEntity<ID>, CodeAware<String> {

    @get:Basic
    @get:Column(name = "code")
    override var code: String? = null

    public constructor(code: String) {
        this.code = code
    }

    protected constructor() : super()

    public companion object {

        public const val CODE: String = "code"
    }
}
