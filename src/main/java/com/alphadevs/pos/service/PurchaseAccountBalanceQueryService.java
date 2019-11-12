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

import com.alphadevs.pos.domain.PurchaseAccountBalance;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.PurchaseAccountBalanceRepository;
import com.alphadevs.pos.service.dto.PurchaseAccountBalanceCriteria;

/**
 * Service for executing complex queries for {@link PurchaseAccountBalance} entities in the database.
 * The main input is a {@link PurchaseAccountBalanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PurchaseAccountBalance} or a {@link Page} of {@link PurchaseAccountBalance} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PurchaseAccountBalanceQueryService extends QueryService<PurchaseAccountBalance> {

    private final Logger log = LoggerFactory.getLogger(PurchaseAccountBalanceQueryService.class);

    private final PurchaseAccountBalanceRepository purchaseAccountBalanceRepository;

    public PurchaseAccountBalanceQueryService(PurchaseAccountBalanceRepository purchaseAccountBalanceRepository) {
        this.purchaseAccountBalanceRepository = purchaseAccountBalanceRepository;
    }

    /**
     * Return a {@link List} of {@link PurchaseAccountBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PurchaseAccountBalance> findByCriteria(PurchaseAccountBalanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PurchaseAccountBalance> specification = createSpecification(criteria);
        return purchaseAccountBalanceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PurchaseAccountBalance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseAccountBalance> findByCriteria(PurchaseAccountBalanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PurchaseAccountBalance> specification = createSpecification(criteria);
        return purchaseAccountBalanceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PurchaseAccountBalanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PurchaseAccountBalance> specification = createSpecification(criteria);
        return purchaseAccountBalanceRepository.count(specification);
    }

    /**
     * Function to convert {@link PurchaseAccountBalanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PurchaseAccountBalance> createSpecification(PurchaseAccountBalanceCriteria criteria) {
        Specification<PurchaseAccountBalance> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PurchaseAccountBalance_.id));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), PurchaseAccountBalance_.balance));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(PurchaseAccountBalance_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}
