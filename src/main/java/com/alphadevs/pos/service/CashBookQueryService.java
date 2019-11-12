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

import com.alphadevs.pos.domain.CashBook;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.CashBookRepository;
import com.alphadevs.pos.service.dto.CashBookCriteria;

/**
 * Service for executing complex queries for {@link CashBook} entities in the database.
 * The main input is a {@link CashBookCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CashBook} or a {@link Page} of {@link CashBook} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CashBookQueryService extends QueryService<CashBook> {

    private final Logger log = LoggerFactory.getLogger(CashBookQueryService.class);

    private final CashBookRepository cashBookRepository;

    public CashBookQueryService(CashBookRepository cashBookRepository) {
        this.cashBookRepository = cashBookRepository;
    }

    /**
     * Return a {@link List} of {@link CashBook} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CashBook> findByCriteria(CashBookCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CashBook> specification = createSpecification(criteria);
        return cashBookRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CashBook} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CashBook> findByCriteria(CashBookCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CashBook> specification = createSpecification(criteria);
        return cashBookRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CashBookCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CashBook> specification = createSpecification(criteria);
        return cashBookRepository.count(specification);
    }

    /**
     * Function to convert {@link CashBookCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CashBook> createSpecification(CashBookCriteria criteria) {
        Specification<CashBook> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CashBook_.id));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), CashBook_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionDescription(), CashBook_.transactionDescription));
            }
            if (criteria.getTransactionAmountDR() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmountDR(), CashBook_.transactionAmountDR));
            }
            if (criteria.getTransactionAmountCR() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionAmountCR(), CashBook_.transactionAmountCR));
            }
            if (criteria.getTransactionBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionBalance(), CashBook_.transactionBalance));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(CashBook_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getTransactionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionTypeId(),
                    root -> root.join(CashBook_.transactionType, JoinType.LEFT).get(TransactionType_.id)));
            }
        }
        return specification;
    }
}
