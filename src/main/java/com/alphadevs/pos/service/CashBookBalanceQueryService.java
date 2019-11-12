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

import com.alphadevs.pos.domain.CashBookBalance;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.CashBookBalanceRepository;
import com.alphadevs.pos.service.dto.CashBookBalanceCriteria;

/**
 * Service for executing complex queries for {@link CashBookBalance} entities in the database.
 * The main input is a {@link CashBookBalanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CashBookBalance} or a {@link Page} of {@link CashBookBalance} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CashBookBalanceQueryService extends QueryService<CashBookBalance> {

    private final Logger log = LoggerFactory.getLogger(CashBookBalanceQueryService.class);

    private final CashBookBalanceRepository cashBookBalanceRepository;

    public CashBookBalanceQueryService(CashBookBalanceRepository cashBookBalanceRepository) {
        this.cashBookBalanceRepository = cashBookBalanceRepository;
    }

    /**
     * Return a {@link List} of {@link CashBookBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CashBookBalance> findByCriteria(CashBookBalanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CashBookBalance> specification = createSpecification(criteria);
        return cashBookBalanceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CashBookBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CashBookBalance> findByCriteria(CashBookBalanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CashBookBalance> specification = createSpecification(criteria);
        return cashBookBalanceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CashBookBalanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CashBookBalance> specification = createSpecification(criteria);
        return cashBookBalanceRepository.count(specification);
    }

    /**
     * Function to convert {@link CashBookBalanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CashBookBalance> createSpecification(CashBookBalanceCriteria criteria) {
        Specification<CashBookBalance> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CashBookBalance_.id));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), CashBookBalance_.balance));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(CashBookBalance_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}
