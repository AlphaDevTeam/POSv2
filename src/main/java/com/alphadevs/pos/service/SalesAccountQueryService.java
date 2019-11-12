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

import com.alphadevs.pos.domain.SalesAccount;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.SalesAccountRepository;
import com.alphadevs.pos.service.dto.SalesAccountCriteria;

/**
 * Service for executing complex queries for {@link SalesAccount} entities in the database.
 * The main input is a {@link SalesAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SalesAccount} or a {@link Page} of {@link SalesAccount} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SalesAccountQueryService extends QueryService<SalesAccount> {

    private final Logger log = LoggerFactory.getLogger(SalesAccountQueryService.class);

    private final SalesAccountRepository salesAccountRepository;

    public SalesAccountQueryService(SalesAccountRepository salesAccountRepository) {
        this.salesAccountRepository = salesAccountRepository;
    }

    /**
     * Return a {@link List} of {@link SalesAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SalesAccount> findByCriteria(SalesAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SalesAccount> specification = createSpecification(criteria);
        return salesAccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SalesAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SalesAccount> findByCriteria(SalesAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SalesAccount> specification = createSpecification(criteria);
        return salesAccountRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SalesAccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SalesAccount> specification = createSpecification(criteria);
        return salesAccountRepository.count(specification);
    }

    /**
     * Function to convert {@link SalesAccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SalesAccount> createSpecification(SalesAccountCriteria criteria) {
        Specification<SalesAccount> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SalesAccount_.id));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), SalesAccount_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionDescription(), SalesAccount_.transactionDescription));
            }
            if (criteria.getTransactionAmountDR() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmountDR(), SalesAccount_.transactionAmountDR));
            }
            if (criteria.getTransactionAmountCR() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmountCR(), SalesAccount_.transactionAmountCR));
            }
            if (criteria.getTransactionBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionBalance(), SalesAccount_.transactionBalance));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(SalesAccount_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(SalesAccount_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
        }
        return specification;
    }
}
