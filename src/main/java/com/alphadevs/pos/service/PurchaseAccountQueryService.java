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

import com.alphadevs.pos.domain.PurchaseAccount;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.PurchaseAccountRepository;
import com.alphadevs.pos.service.dto.PurchaseAccountCriteria;

/**
 * Service for executing complex queries for {@link PurchaseAccount} entities in the database.
 * The main input is a {@link PurchaseAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PurchaseAccount} or a {@link Page} of {@link PurchaseAccount} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PurchaseAccountQueryService extends QueryService<PurchaseAccount> {

    private final Logger log = LoggerFactory.getLogger(PurchaseAccountQueryService.class);

    private final PurchaseAccountRepository purchaseAccountRepository;

    public PurchaseAccountQueryService(PurchaseAccountRepository purchaseAccountRepository) {
        this.purchaseAccountRepository = purchaseAccountRepository;
    }

    /**
     * Return a {@link List} of {@link PurchaseAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PurchaseAccount> findByCriteria(PurchaseAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PurchaseAccount> specification = createSpecification(criteria);
        return purchaseAccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PurchaseAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseAccount> findByCriteria(PurchaseAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PurchaseAccount> specification = createSpecification(criteria);
        return purchaseAccountRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PurchaseAccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PurchaseAccount> specification = createSpecification(criteria);
        return purchaseAccountRepository.count(specification);
    }

    /**
     * Function to convert {@link PurchaseAccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PurchaseAccount> createSpecification(PurchaseAccountCriteria criteria) {
        Specification<PurchaseAccount> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PurchaseAccount_.id));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), PurchaseAccount_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionDescription(), PurchaseAccount_.transactionDescription));
            }
            if (criteria.getTransactionAmountDR() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmountDR(), PurchaseAccount_.transactionAmountDR));
            }
            if (criteria.getTransactionAmountCR() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmountCR(), PurchaseAccount_.transactionAmountCR));
            }
            if (criteria.getTransactionBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionBalance(), PurchaseAccount_.transactionBalance));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(PurchaseAccount_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(PurchaseAccount_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
        }
        return specification;
    }
}
