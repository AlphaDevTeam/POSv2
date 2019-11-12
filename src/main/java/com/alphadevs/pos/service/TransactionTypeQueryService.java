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

import com.alphadevs.pos.domain.TransactionType;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.TransactionTypeRepository;
import com.alphadevs.pos.service.dto.TransactionTypeCriteria;

/**
 * Service for executing complex queries for {@link TransactionType} entities in the database.
 * The main input is a {@link TransactionTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionType} or a {@link Page} of {@link TransactionType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionTypeQueryService extends QueryService<TransactionType> {

    private final Logger log = LoggerFactory.getLogger(TransactionTypeQueryService.class);

    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionTypeQueryService(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
    }

    /**
     * Return a {@link List} of {@link TransactionType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionType> findByCriteria(TransactionTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TransactionType> specification = createSpecification(criteria);
        return transactionTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TransactionType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionType> findByCriteria(TransactionTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransactionType> specification = createSpecification(criteria);
        return transactionTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TransactionType> specification = createSpecification(criteria);
        return transactionTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransactionType> createSpecification(TransactionTypeCriteria criteria) {
        Specification<TransactionType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TransactionType_.id));
            }
            if (criteria.getTransactionypeCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionypeCode(), TransactionType_.transactionypeCode));
            }
            if (criteria.getTransactionType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionType(), TransactionType_.transactionType));
            }
        }
        return specification;
    }
}
