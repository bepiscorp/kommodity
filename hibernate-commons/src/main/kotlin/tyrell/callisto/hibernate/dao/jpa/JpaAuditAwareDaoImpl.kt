package tyrell.callisto.hibernate.dao.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.provider.PersistenceProvider
import org.springframework.data.jpa.repository.query.QueryUtils
import org.springframework.data.jpa.repository.support.CrudMethodMetadata
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.util.ProxyUtils
import org.springframework.transaction.annotation.Transactional
import tyrell.callisto.base.definition.ExperimentalLibraryApi
import tyrell.callisto.data.enum.AuditState
import tyrell.callisto.hibernate.entity.AuditAwareEntity
import java.io.Serializable
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.persistence.Query
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Order
import javax.persistence.criteria.ParameterExpression
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

// TODO: Consider separating into GenericDao (base part) and JpaAuditAwareDao (audit part)
/**
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@ExperimentalLibraryApi
public open class JpaAuditAwareDaoImpl<T : AuditAwareEntity<ID>, ID : Serializable> @Autowired constructor(
    private val entityInformation: JpaEntityInformation<T, ID>,
    private val entityManager: EntityManager,
) : JpaAuditAwareDao<T, ID>, SimpleJpaRepository<T, ID>(entityInformation, entityManager) {

    private val provider: PersistenceProvider = PersistenceProvider.fromEntityManager(entityManager)

    /**
     * @see SimpleJpaRepository.getCountQueryString
     * @see QueryUtils.COUNT_QUERY_STRING
     */
    protected open val countQueryString: String = run {
        val countQuery: String = String.format(
            "select count(%s) from %s x " +
                "where x.auditState = tyrell.callisto.hibernate.enums.AuditState.ACTIVE",
            provider.countQueryPlaceholder, "%s",
        )

        QueryUtils.getQueryString(countQuery, entityInformation.entityName)
    }

    /**
     * @see SimpleJpaRepository.getDeleteAllQueryString
     */
    protected open val deleteAllQueryString: String = String.format(
        "update %s x " +
            "set x.auditState = tyrell.callisto.hibernate.enums.AuditState.REMOVED " +
            "where x.auditState != tyrell.callisto.hibernate.enums.AuditState.REMOVED", // Optimization
        entityInformation.entityName, entityInformation.idAttribute!!.name,
    )

    /**
     * @see QueryUtils.DELETE_ALL_QUERY_BY_ID_STRING
     */
    protected open val deleteAllByIdsQueryString: String = String.format(
        "update %s x " +
            "set x.auditState = tyrell.callisto.hibernate.enums.AuditState.REMOVED " +
            "where %s in :ids and" +
            "x.auditState != tyrell.callisto.hibernate.enums.AuditState.REMOVED", // Optimization
        entityInformation.entityName, entityInformation.idAttribute!!.name,
    )

    override fun <S : T> getQuery(spec: Specification<S>?, domainClass: Class<S>, sort: Sort): TypedQuery<S> {
        val cb: CriteriaBuilder = entityManager.criteriaBuilder
        val cq: CriteriaQuery<S> = cb.createQuery(domainClass)

        val root: Root<S> = applySpecificationToCriteria(spec, domainClass, cq)
        cq.select(root)

        if (sort.isSorted) {
            cq.orderBy(QueryUtils.toOrders(sort, root, cb))
        }

        return applyRepositoryMethodMetadata(entityManager.createQuery(cq))
    }

    /**
     * @see SimpleJpaRepository.getCountQuery
     */
    override fun <S : T> getCountQuery(spec: Specification<S>?, domainClass: Class<S>): TypedQuery<Long> {
        val cb: CriteriaBuilder = entityManager.criteriaBuilder
        val cq: CriteriaQuery<Long> = cb.createQuery(Long::class.java)
        val root: Root<S> = applySpecificationToCriteria(spec, domainClass, cq)

        when {
            cq.isDistinct -> cq.select(cb.countDistinct(root))
            else -> cq.select(cb.count(root))
        }

        // Remove all Orders the Specifications might have applied
        cq.orderBy(emptyList<Order>())

        return entityManager.createQuery(cq)
    }

    protected open fun <U : T> getAdditionalPredicates(
        root: Root<U>,
        cq: CriteriaQuery<*>,
        cb: CriteriaBuilder,
    ): Collection<Predicate> = emptyList()

    protected open fun <U : T> getAuditStatePredicate(
        root: Root<U>,
        cq: CriteriaQuery<*>,
        cb: CriteriaBuilder,
    ): Predicate =
        checkNotNull(auditStateSpecification.toPredicate(root as Root<AuditAwareEntity<*>>, cq, cb))

    /**
     * @see SimpleJpaRepository.applySpecificationToCriteria
     */
    private fun <S, U : T> applySpecificationToCriteria(
        spec: Specification<U>?,
        domainClass: Class<U>,
        cq: CriteriaQuery<S>,
    ): Root<U> {
        val root: Root<U> = cq.from(domainClass)
        val cb: CriteriaBuilder = entityManager.criteriaBuilder

        val compositePredicate: Predicate = run {
            val predicates: MutableList<Predicate> = mutableListOf()

            // Additional predicates
            predicates += getAdditionalPredicates(root, cq, cb)

            // Audit predicate
            predicates += getAuditStatePredicate(root, cq, cb)

            // Specification predicate
            spec?.toPredicate(root, cq, cb)
                ?.let { specificationPredicate: Predicate -> predicates += specificationPredicate }

            when (predicates.size) {
                0 -> return@applySpecificationToCriteria root // Nothing to apply
                1 -> predicates[0] // Optimization
                else -> cb.and(*predicates.toTypedArray())
            }
        }

        cq.where(compositePredicate)
        return root
    }

    /**
     * @see SimpleJpaRepository.applyRepositoryMethodMetadata
     */
    private fun <S> applyRepositoryMethodMetadata(query: TypedQuery<S>): TypedQuery<S> {
        val metadata: CrudMethodMetadata = this.repositoryMethodMetadata ?: return query

        val resolvedLockMode: LockModeType? = metadata.lockModeType

        return query.apply {
            if (resolvedLockMode != null) {
                this.lockMode = resolvedLockMode
            }

            this.applyQueryHints()
        }
    }

    /**
     * @see SimpleJpaRepository.applyQueryHints
     */
    private fun Query.applyQueryHints(): Unit =
        queryHints.withFetchGraphs(entityManager).forEach { hintName: String?, value: Any? ->
            setHint(hintName, value)
        }

    /**
     * @see org.springframework.data.repository.CrudRepository.count
     */
    override fun count(): Long = entityManager.createQuery(countQueryString, Long::class.java).singleResult

    /**
     * @see org.springframework.data.jpa.repository.JpaSpecificationExecutor.count
     */
    override fun count(spec: Specification<T>?): Long = executeCountQuery(getCountQuery(spec, domainClass))

    /**
     * @see SimpleJpaRepository.executeCountQuery
     */
    private fun executeCountQuery(query: TypedQuery<Long>): Long {
        // TODO: Is there really a possibility that [query.resultList] returns `null`?
        val totals: List<Long?> = query.resultList

        var total = 0L
        for (element in totals) {
            total += element ?: 0
        }

        return total
    }

    /**
     * @see org.springframework.data.repository.CrudRepository.delete
     */
    @Transactional
    override fun delete(entity: T) {
        if (entityInformation.isNew(entity)) {
            return // TODO: Shouldn't we throw here
        }

        val type: Class<*> = ProxyUtils.getUserClass(entity)

        @Suppress("VARIABLE_WITH_REDUNDANT_INITIALIZER") // If the entity to be deleted doesn't exist, delete is a NOOP
        var existingEntity: T = entityManager.find(type, entityInformation.getId(entity)) as T? ?: return
        existingEntity = if (entityManager.contains(entity)) entity else entityManager.merge(entity)

        entityManager.remove(existingEntity)
    }

    /**
     * @see org.springframework.data.repository.CrudRepository.deleteAllById
     */
    @Transactional
    override fun deleteAllById(ids: Iterable<ID>): Unit = ids.forEach { deleteById(it) }

    /**
     * @see org.springframework.data.jpa.repository.JpaRepository.deleteAllByIdInBatch
     */
    @Transactional
    override fun deleteAllByIdInBatch(ids: Iterable<ID>) {
        if (!ids.iterator().hasNext()) {
            return
        }

        val query: Query = entityManager.createQuery(this.deleteAllByIdsQueryString)
        query.setParameter("ids", ids)

        // TODO: We could possibly check if amount of [ids]
        //  equals to count of updated rows and log if they are not equal
        //  val rowsUpdated: Int = query.executeUpdate()
        query.executeUpdate()
    }

    /**
     * @see org.springframework.data.repository.CrudRepository
     */
    @Transactional
    override fun deleteAll(entities: Iterable<T>): Unit = entities.forEach { delete(it) }

    /**
     * @see org.springframework.data.jpa.repository.JpaRepository
     */
    @Transactional
    override fun deleteAllInBatch(entities: Iterable<T>) {
        if (!entities.iterator().hasNext()) {
            return
        }

        QueryUtils.applyAndBind(this.deleteAllQueryString, entities, entityManager).executeUpdate()
    }

    /**
     * @see org.springframework.data.repository.CrudRepository.deleteAll
     */
    @Transactional
    override fun deleteAll(): Unit = findAll().forEach { delete(it) }

    /**
     * @see org.springframework.data.jpa.repository.JpaRepository.deleteAllInBatch
     */
    @Transactional
    override fun deleteAllInBatch() {
        @Suppress("UNUSED_VARIABLE")
        val rowsUpdated: Int = entityManager.createQuery(this.deleteAllQueryString).executeUpdate()
    }

    /**
     * @see org.springframework.data.repository.CrudRepository.deleteById
     */
    override fun deleteById(id: ID): Unit = super.deleteById(id)

    private companion object {

        private val auditStateSpecification =
            Specification { root: Root<in AuditAwareEntity<*>>, cq: CriteriaQuery<*>, cb: CriteriaBuilder ->
                /**
                 * It is better to use parameter here instead of constants because of performance reasons.
                 * Hibernate will be able to perform more aggressive caching in case of parametrizing
                 */
                val auditStateParameter: ParameterExpression<AuditState> =
                    cb.parameter(AuditState::class.java, AuditAwareEntity.AUDIT_STATE)

                val auditStatePath: Path<AuditState> = root.get(AuditAwareEntity.AUDIT_STATE)

                cb.equal(auditStatePath, auditStateParameter)
            }
    }
}
