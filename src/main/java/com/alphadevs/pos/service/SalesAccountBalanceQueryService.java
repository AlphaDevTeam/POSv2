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

import com.alphadevs.pos.domain.SalesAccountBalance;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.SalesAccountBalanceRepository;
import com.alphadevs.pos.service.dto.SalesAccountBalanceCriteria;

/**
 * Service for executing complex queries for {@link SalesAccountBalance} entities in the database.
 * The main input is a {@link SalesAccountBalanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SalesAccountBalance} or a {@link Page} of {@link SalesAccountBalance} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SalesAccountBalanceQueryService extends QueryService<SalesAccountBalance> {

    private final Logger log = LoggerFactory.getLogger(SalesAccountBalanceQueryService.class);

    private final SalesAccountBalanceRepository salesAccountBalanceRepository;

    public SalesAccountBalanceQueryService(SalesAccountBalanceRepository salesAccountBalanceRepository) {
        this.salesAccountBalanceRepository = salesAccountBalanceRepository;
    }

    /**
     * Return a {@link List} of {@link SalesAccountBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SalesAccountBalance> findByCriteria(SalesAccountBalanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SalesAccountBalance> specification = createSpecification(criteria);
        return salesAccountBalanceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SalesAccountBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SalesAccountBalance> findByCriteria(SalesAccountBalanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SalesAccountBalance> specification = createSpecification(criteria);
        return salesAccountBalanceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SalesAccountBalanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SalesAccountBalance> specification = createSpecification(criteria);
        return salesAccountBalanceRepository.count(specification);
    }

    /**
     * Function to convert {@link SalesAccountBalanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SalesAccountBalance> createSpecification(SalesAccountBalanceCriteria criteria) {
        Specification<SalesAccountBalance> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SalesAccountBalance_.id));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), SalesAccountBalance_.balance));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(SalesAccountBalance_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}
