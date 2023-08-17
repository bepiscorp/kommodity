package tyrell.callisto.hibernate.enums

import tyrell.callisto.data.enum.AuditState
import javax.persistence.Converter

@Suppress("unused")
@Converter(autoApply = true)
internal class AuditStateJpaConverter : JpaEnumConverter<AuditState>()
