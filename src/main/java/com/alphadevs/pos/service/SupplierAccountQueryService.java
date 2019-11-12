package com.alphadevs.pos.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.alphadevs.pos.domain.SupplierAccount;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.SupplierAccountRepository;
import com.alphadevs.pos.service.dto.SupplierAccountCriteria;

/**
 * Service for executing complex queries for {@link SupplierAccount} entities in the database.
 * The main input is a {@link SupplierAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SupplierAccount} or a {@link Page} of {@link SupplierAccount} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierAccountQueryService extends QueryService<SupplierAccount> {

    private final Logger log = LoggerFactory.getLogger(SupplierAccountQueryService.class);

    private final SupplierAccountRepository supplierAccountRepository;

    public SupplierAccountQueryService(SupplierAccountRepository supplierAccountRepository) {
        this.supplierAccountRepository = supplierAccountRepository;
    }

    /**
     * Return a {@link List} of {@link SupplierAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SupplierAccount> findByCriteria(SupplierAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SupplierAccount> specification = createSpecification(criteria);
        return supplierAccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SupplierAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierAccount> findByCriteria(SupplierAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SupplierAccount> specification = createSpecification(criteria);
        return supplierAccountRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierAccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SupplierAccount> specification = createSpecification(criteria);
        return supplierAccountRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierAccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SupplierAccount> createSpecification(SupplierAccountCriteria criteria) {
        Specification<SupplierAccount> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SupplierAccount_.id));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), SupplierAccount_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionDescription(), SupplierAccount_.transactionDescription));
            }
            if (criteria.getTransactionAmountDR() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmountDR(), SupplierAccount_.transactionAmountDR));
            }
            if (criteria.getTransactionAmountCR() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmountCR(), SupplierAccount_.transactionAmountCR));
            }
            if (criteria.getTransactionBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionBalance(), SupplierAccount_.transactionBalance));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(SupplierAccount_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(SupplierAccount_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
            if (criteria.getSupplierId() != null) {
                specification = specification.and(buildSpecification(criteria.getSupplierId(),
                    root -> root.join(SupplierAccount_.supplier, JoinType.LEFT).get(Supplier_.id)));
            }
        }
        return specification;
    }
}
